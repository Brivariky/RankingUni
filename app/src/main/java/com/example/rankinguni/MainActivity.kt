package com.example.rankinguni

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rankinguni.databinding.ActivityMainBinding
import org.json.JSONArray
import java.io.BufferedReader

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var universityAdapter: UniversityAdapter
    private var allUniversities: List<UniversityListItem> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadUniversitiesData()
        setupSearchView()
    }

    private fun setupRecyclerView() {
        universityAdapter = UniversityAdapter(emptyList()) { selectedUniversity ->
            val intent = Intent(this, PredictActivity::class.java).apply {
                putExtra("UNIVERSITY_DATA", selectedUniversity)
            }
            startActivity(intent)
        }

        binding.recyclerViewUniversities.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = universityAdapter
        }
    }

    private fun loadUniversitiesData() {
        val inputStream = assets.open("universities.json")
        val bufferedReader = BufferedReader(inputStream.reader())
        val jsonString = bufferedReader.use { it.readText() }
        val jsonArray = JSONArray(jsonString)

        val packageName = applicationContext.packageName
        val universityList = mutableListOf<UniversityListItem>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val id = jsonObject.getString("id")
            val name = jsonObject.getString("university_name")
            val description = jsonObject.getString("current_ranking") // fixed!
            val currentRank = jsonObject.getInt("current_ranking")
            val logoName = jsonObject.getString("logo")
            val website = jsonObject.getString("website")
            val predictedRank = jsonObject.optInt("predicted_ranking", currentRank)

            val logoResId = resources.getIdentifier(logoName, "drawable", packageName)
            val logoUri = "android.resource://$packageName/$logoResId"

            val university = UniversityListItem(
                id = id,
                name = name,
                description = description,
                logoUrl = logoUri,
                websiteUrl = website,
                currentRank = currentRank,
                predictedRank = predictedRank,
            )
            universityList.add(university)
        }

        allUniversities = universityList
        universityAdapter.updateData(allUniversities)
    }

    private fun setupSearchView() {
        binding.editTextSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                filterUniversities(v.text.toString())
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                return@setOnEditorActionListener true
            }
            false
        }

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterUniversities(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterUniversities(query: String) {
        val filteredList = if (query.isEmpty()) {
            allUniversities
        } else {
            allUniversities.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        universityAdapter.updateData(filteredList)

        if (filteredList.isEmpty() && query.isNotEmpty()) {
            Toast.makeText(this, "Universitas tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }
}
