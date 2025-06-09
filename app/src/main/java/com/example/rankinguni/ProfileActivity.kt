package com.example.rankinguni

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        title = "Profile"

        val email = intent.getStringExtra("USER_EMAIL")

        val emailTextView = findViewById<TextView>(R.id.textViewEmail)
        emailTextView.text = "Email: $email"
    }
}
