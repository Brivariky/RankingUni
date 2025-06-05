package com.example.rankinguni // Pastikan package ini sesuai

data class UniversityListItem(
    val id: String,
    val name: String,
    val shortRankInfo: String,
    val logoUrl: String,
    val websiteUrl: String,
    val currentRank: Int
)