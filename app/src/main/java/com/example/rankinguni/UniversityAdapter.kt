package com.example.rankinguni

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rankinguni.databinding.ListItemUniversityBinding // Pastikan nama binding ini benar

class UniversityAdapter(
    private var universities: List<UniversityListItem>,
    private val onItemClick: (UniversityListItem) -> Unit // Lambda untuk menangani klik
) : RecyclerView.Adapter<UniversityAdapter.UniversityViewHolder>() {

    // Fungsi untuk memperbarui data di adapter
    fun updateData(newUniversities: List<UniversityListItem>) {
        universities = newUniversities
        notifyDataSetChanged() // Memberitahu RecyclerView bahwa data telah berubah
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversityViewHolder {
        val binding = ListItemUniversityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UniversityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UniversityViewHolder, position: Int) {
        val university = universities[position]
        holder.bind(university)
        holder.itemView.setOnClickListener {
            onItemClick(university) // Panggil lambda saat item diklik
        }
    }

    override fun getItemCount(): Int = universities.size

    class UniversityViewHolder(private val binding: ListItemUniversityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(university: UniversityListItem) {
            binding.textUniversityNameItem.text = university.name
            binding.textUniversityRankItem.text = university.description
            Glide.with(binding.root.context)
                .load(university.logoUrl)
                .placeholder(R.mipmap.ic_launcher) // Placeholder
                .into(binding.imageUniversityLogoItem)

            // Contoh untuk handle klik favorit (jika ada)
            // binding.imageFavoriteIcon.setOnClickListener { /* handle favorit */ }
        }
    }
}