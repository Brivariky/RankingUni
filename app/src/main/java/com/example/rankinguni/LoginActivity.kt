package com.example.rankinguni

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleClient: GoogleSignInClient
    private val RC_SIGN_IN = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val emailInput = findViewById<EditText>(R.id.editUsername)
        val passwordInput = findViewById<EditText>(R.id.editPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val createAccount = findViewById<TextView>(R.id.createAccountText)
        val forgotPasswordText = findViewById<TextView>(R.id.textForgotPassword)

        // Google sign-in config
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleClient = GoogleSignIn.getClient(this, gso)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.error = "Invalid email format"
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user?.isEmailVerified == true) {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            showAlertDialog("Email Not Verified", "Please verify your email before logging in.")
                        }
                    } else {
                        showAlertDialog("Login Failed", "Incorrect email or password. Please try again.")
                    }
                }
        }

        forgotPasswordText.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_reset_password, null)
            val resetEmailInput = dialogView.findViewById<EditText>(R.id.resetEmailInput)
            val sendButton = dialogView.findViewById<Button>(R.id.sendResetButton)

            val alertDialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()

            sendButton.setOnClickListener {
                val email = resetEmailInput.text.toString().trim()
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    resetEmailInput.error = "Enter a valid email"
                    return@setOnClickListener
                }

                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            alertDialog.dismiss()
                            showAlertDialog("Email Sent", "A password reset link has been sent to your email.")
                        } else {
                            showAlertDialog("Error", "Failed to send reset email. Please try again.")
                        }
                    }
            }

            alertDialog.show()
        }

        findViewById<ImageView>(R.id.google).setOnClickListener {
            startActivityForResult(googleClient.signInIntent, RC_SIGN_IN)
        }

        createAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun showAlertDialog(title: String, message: String, positiveText: String = "OK") {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveText, null)

        val dialog = builder.create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.apply {
            setTextColor(resources.getColor(R.color.purple_500, theme))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                val account = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener(this) { authTask ->
                    if (authTask.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        showAlertDialog("Google Sign-In Failed", "Could not sign in with Google. Please try again.")
                    }
                }
            }
        }
    }
}
