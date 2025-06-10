package com.example.rankinguni

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        title = "Profile"
        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser
        val email = user?.email ?: "Not Logged In"
        val username = email.substringBefore("@")

        findViewById<TextView>(R.id.textViewEmail).text = "Email: $email"
        findViewById<TextView>(R.id.textViewUsername).text = "Username: $username"

        findViewById<Button>(R.id.buttonLogout).setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        val changePassBtn = findViewById<Button>(R.id.buttonChangePassword)
        val prefs = getSharedPreferences("password_reset_prefs", MODE_PRIVATE)
        val lastResetTime = prefs.getLong("last_reset_time", 0)
        val cooldownMillis = 30 * 60 * 1000L // 30 minutes

        changePassBtn.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            val timeSinceLast = currentTime - lastResetTime

            if (timeSinceLast < cooldownMillis) {
                val minutesLeft = ((cooldownMillis - timeSinceLast) / 60000).toInt()
                Toast.makeText(this, "Please wait $minutesLeft min before requesting again.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        prefs.edit().putLong("last_reset_time", currentTime).apply()
                        Toast.makeText(this, "Reset link sent to $email", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Failed to send reset email.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}