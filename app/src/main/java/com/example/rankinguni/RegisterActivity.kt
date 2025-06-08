package com.example.rankinguni

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val emailInput = findViewById<EditText>(R.id.editEmail)
        val passwordInput = findViewById<EditText>(R.id.editPassword)
        val confirmPasswordInput = findViewById<EditText>(R.id.editConfirmPassword)
        val registerButton = findViewById<Button>(R.id.btnRegister)
        val backToLoginText = findViewById<TextView>(R.id.textBackToLogin)

        registerButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            var valid = true

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.error = "Invalid email"
                valid = false
            }

            val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!.]).{8,}\$")

            if (!passwordRegex.matches(password)) {
                passwordInput.error = "Use 8+ chars, upper & lowercase, number & special char"
                valid = false
            }

            if (password != confirmPassword) {
                confirmPasswordInput.error = "Passwords don't match"
                valid = false
            }

            if (!valid) return@setOnClickListener

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        auth.currentUser?.sendEmailVerification()
                        showAlertDialog(
                            "Registration Successful",
                            "Check your email for verification before logging in.",
                            positiveText = "OK",
                            onPositive = {
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                        )
                    } else {
                        showAlertDialog(
                            "Registration Failed",
                            "Account may already exist or registration failed. Please try again."
                        )
                    }
                }
        }

        backToLoginText.setOnClickListener {
            finish()
        }
    }

    private fun showAlertDialog(
        title: String,
        message: String,
        positiveText: String = "OK",
        onPositive: (() -> Unit)? = null
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveText) { dialog, _ ->
            dialog.dismiss()
            onPositive?.invoke()
        }
        val dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.apply {
            setTextColor(resources.getColor(R.color.purple_500, theme))
        }
    }
}
