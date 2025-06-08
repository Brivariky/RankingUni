package com.example.rankinguni

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.rankinguni.databinding.ActivityPredictBinding

class PredictActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPredictBinding
    private lateinit var university: UniversityListItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Prediction Details"

        val universityData = intent.getSerializableExtra("UNIVERSITY_DATA") as? UniversityListItem
        if (universityData == null) {
            finish()
            return
        }

        university = universityData
        loadUniversityData()

        binding.universityWebsiteButtonPredict.setOnClickListener {
            openUniversityWebsite(university.websiteUrl)
        }
    }

    private fun loadUniversityData() {
        supportActionBar?.title = university.name

        Glide.with(this)
            .load(university.logoUrl)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher_round)
            .into(binding.universityLogoHeaderPredict)

        binding.currentRankTextPredict.text = university.currentRank.toString()
        binding.predictedRankTextPredict.text = university.predictedRank.toString()

        Glide.with(this)
            .load(university.logoUrl)
            .into(binding.universityWebsiteButtonPredict)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun openUniversityWebsite(url: String) {
        if (url.isBlank()) return

        binding.webviewUniversityPredict.visibility = View.VISIBLE
        binding.webviewUniversityPredict.webViewClient = WebViewClient()
        binding.webviewUniversityPredict.settings.javaScriptEnabled = true
        binding.webviewUniversityPredict.loadUrl(url)
    }

    override fun onBackPressed() {
        if (binding.webviewUniversityPredict.canGoBack() && binding.webviewUniversityPredict.visibility == View.VISIBLE) {
            binding.webviewUniversityPredict.goBack()
        } else if (binding.webviewUniversityPredict.visibility == View.VISIBLE) {
            binding.webviewUniversityPredict.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
