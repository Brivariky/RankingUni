package com.example.rankinguni

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.rankinguni.databinding.ActivityPredictBinding // Ganti nama binding jika perlu

// Anda mungkin perlu membuat data class untuk UniversityDetail atau menerimanya via Intent
data class UniversityDetail( // Contoh sederhana, sesuaikan dengan kebutuhan data Anda
    val id: String, // ID unik untuk mengambil data lengkap jika perlu
    val name: String,
    val logoUrl: String,
    val currentRank: Int,
    val websiteUrl: String
    // Tambahkan field lain yang relevan untuk prediksi
)

class PredictActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPredictBinding
    private lateinit var university: UniversityDetail // Untuk menyimpan data universitas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aktifkan tombol kembali di ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Prediction Details" // Judul Halaman

        // Ambil data universitas dari Intent
        // Anda perlu mengirim data ini dari MainActivity saat item RecyclerView diklik
        val name = intent.getStringExtra("UNIVERSITY_NAME") ?: "N/A"
        val logoUrl = intent.getStringExtra("UNIVERSITY_LOGO_URL") ?: ""
        val currentRank = intent.getIntExtra("UNIVERSITY_CURRENT_RANK", 0)
        val websiteUrl = intent.getStringExtra("UNIVERSITY_WEBSITE_URL") ?: ""
        val universityId = intent.getStringExtra("UNIVERSITY_ID") ?: "" // Jika Anda menggunakan ID

        university = UniversityDetail(universityId, name, logoUrl, currentRank, websiteUrl)

        loadUniversityData()
        predictRankForUniversity() // Panggil fungsi prediksi Anda

        binding.universityWebsiteButtonPredict.setOnClickListener {
            openUniversityWebsite(university.websiteUrl)
        }
    }

    private fun loadUniversityData() {
        supportActionBar?.title = university.name // Set judul ActionBar dengan nama universitas

        Glide.with(this)
            .load(university.logoUrl)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher_round)
            .into(binding.universityLogoHeaderPredict)

        binding.currentRankTextPredict.text = university.currentRank.toString()
        // Anda juga bisa memuat logo untuk tombol website jika berbeda
        Glide.with(this)
            .load(university.logoUrl) // Atau logo lain
            .into(binding.universityWebsiteButtonPredict)
    }

    private fun predictRankForUniversity() {
        // Di sinilah logika untuk memanggil model ML Anda akan ditempatkan.
        // Untuk sekarang, kita buat simulasi:
        val simulatedPredictedRank = university.currentRank + (1..5).random() // Contoh prediksi acak
        binding.predictedRankTextPredict.text = simulatedPredictedRank.toString()

        // TODO: Implementasikan UniversityRankingPredictor Anda di sini
        // val predictor = UniversityRankingPredictor("path_model.tflite")
        // val inputData = ... // Siapkan data input untuk model dari 'university'
        // val prediction = predictor.predict_ranking(inputData)
        // binding.predictedRankTextPredict.text = prediction.roundToInt().toString()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun openUniversityWebsite(url: String) {
        if (url.isBlank()) return

        // Anda bisa memilih untuk membuka di WebView internal atau browser eksternal
        // Menggunakan WebView internal:
        binding.webviewUniversityPredict.visibility = View.VISIBLE
        binding.webviewUniversityPredict.webViewClient = WebViewClient()
        binding.webviewUniversityPredict.settings.javaScriptEnabled = true
        binding.webviewUniversityPredict.loadUrl(url)

        // Atau buka di browser eksternal:
        // val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        // startActivity(intent)
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

    // Handle tombol kembali di ActionBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed() // Cara modern untuk handle back
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}