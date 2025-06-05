package com.example.rankinguni

import android.content.Intent
import android.os.Bundle
import android.text.Editable // Tambahkan impor ini
import android.text.TextWatcher // Tambahkan impor ini
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rankinguni.databinding.ActivityMainBinding

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
                putExtra("UNIVERSITY_ID", selectedUniversity.id)
                putExtra("UNIVERSITY_NAME", selectedUniversity.name)
                putExtra("UNIVERSITY_LOGO_URL", selectedUniversity.logoUrl)
                putExtra("UNIVERSITY_CURRENT_RANK", selectedUniversity.currentRank)
                putExtra("UNIVERSITY_WEBSITE_URL", selectedUniversity.websiteUrl)
            }
            startActivity(intent)
        }

        binding.recyclerViewUniversities.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = universityAdapter
        }
    }

    private fun loadUniversitiesData() {
        // Ganti nama package jika berbeda, tapi "com.example.rankinguni" seharusnya sudah benar.
        val packageName = applicationContext.packageName

        allUniversities = listOf(
            UniversityListItem("UMS001", "Universitas Muhammadiyah Surakarta", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_ums", // Contoh untuk li_ums
                "https://www.ums.ac.id", // Ganti dengan URL website UMS yang benar
                601),
            UniversityListItem("UA002", "Universitas Airlangga", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_airlangga", // Contoh untuk li_airlangga
                "https://www.unair.ac.id", // Ganti dengan URL website UNAIR yang benar
                345),
            UniversityListItem("UAJ003", "Universitas Katolik Indonesia Atma Jaya", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_ajk", // Contoh untuk li_ajk
                "https://www.atmajaya.ac.id", // Ganti dengan URL website Atma Jaya yang benar
                1201),
            UniversityListItem("ITB004", "Institut Teknologi Bandung", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_itb", // Contoh untuk li_itb
                "https://www.itb.ac.id", // Ganti dengan URL website ITB yang benar
                281),
            UniversityListItem("BINUS005", "Universitas Bina Nusantara", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_binus",
                "https://binus.ac.id/",
                1001),
            UniversityListItem("UNDIP006", "Universitas Diponegoro", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_undip",
                "https://www.undip.ac.id",
                801),
            UniversityListItem("UGM007", "Universitas Gadjah Mada", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_ugm",
                "https://www.ugm.ac.id",
                230),
            UniversityListItem("ITS008", "Institut Teknologi Sepuluh Nopember", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_its",
                "https://www.its.ac.id",
                621),
            UniversityListItem("IPB009", "Institut Pertanian Bogor", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_ipb",
                "https://ipb.ac.id/",
                489),
            UniversityListItem("UKP010", "Universitas Kristen Petra", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_ukp",
                "https://petra.ac.id/",
                1401),
            UniversityListItem("UM011", "Universitas Negeri Malang", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_unm",
                "https://um.ac.id/",
                1201),
            UniversityListItem("TELU012", "Universitas Telkom", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_telkom",
                "https://telkomuniversity.ac.id/",
                1001),
            UniversityListItem("UNUD013", "Universitas Udayana", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_udayana",
                "https://www.unud.ac.id/",
                1201),
            UniversityListItem("UNAND014", "Universitas Andalas", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_andalas",
                "https://www.unand.ac.id/",
                1201),
            UniversityListItem("UB015", "Universitas Brawijaya", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_unbraw", // Anda menggunakan li_unbraw di XML lama
                "https://ub.ac.id/",
                801),
            UniversityListItem("UNHAS016", "Universitas Hasanuddin", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_unhas",
                "https://unhas.ac.id/",
                1001),
            UniversityListItem("UI017", "Universitas Indonesia", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_ui",
                "https://www.ui.ac.id/",
                237),
            UniversityListItem("UMY018", "Universitas Muhammadiyah Yogyakarta", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_umy",
                "https://www.umy.ac.id/",
                1001),
            UniversityListItem("UII019", "Universitas Islam Indonesia", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_uii",
                "https://www.uii.ac.id/",
                1201),
            UniversityListItem("UNY020", "Universitas Negeri Yogyakarta", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_uny",
                "https://www.uny.ac.id/",
                1201),
            UniversityListItem("UNPAD021", "Universitas Padjadjaran", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_unpad",
                "https://www.unpad.ac.id/",
                661),
            UniversityListItem("UPI022", "Universitas Pendidikan Indonesia", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_upi",
                "https://www.upi.edu/",
                1201),
            UniversityListItem("UNS023", "Universitas Sebelas Maret", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_uns",
                "https://uns.ac.id/",
                801),
            UniversityListItem("USU024", "Universitas Sumatera Utara", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_unsu", // Sesuai nama drawable di XML lama Anda
                "https://www.usu.ac.id/",
                1201),
            UniversityListItem("UNILA025", "Universitas Lampung", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_unla", // Sesuai nama drawable di XML lama Anda
                "https://www.unila.ac.id/",
                1401),
            UniversityListItem("UNRAM026", "Universitas Mataram", "World Rank - 2024",
                "android.resource://$packageName/drawable/li_unmat", // Sesuai nama drawable di XML lama Anda
                "https://unram.ac.id/",
                1401)
        )
        universityAdapter.updateData(allUniversities)
    }

    private fun setupSearchView() {
        // Menghapus atau mengomentari setOnEditorActionListener jika Anda hanya ingin live search

        binding.editTextSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                filterUniversities(v.text.toString())
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                return@setOnEditorActionListener true
            }
            false
        }


        // Aktifkan TextWatcher untuk live search
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Tidak perlu melakukan apa-apa di sini
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Panggil filterUniversities setiap kali teks di EditText berubah
                filterUniversities(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // Tidak perlu melakukan apa-apa di sini
            }
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

        // Opsional: Tampilkan pesan jika tidak ada hasil dan query tidak kosong
         if (filteredList.isEmpty() && query.isNotEmpty()) {
             Toast.makeText(this, "Universitas tidak ditemukan", Toast.LENGTH_SHORT).show()
         }
    }
}