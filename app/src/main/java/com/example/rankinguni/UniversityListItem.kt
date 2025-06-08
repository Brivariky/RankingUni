package com.example.rankinguni

import java.io.Serializable

data class UniversityListItem(
    val id: String,
    val name: String,
    val description: String,
    val logoUrl: String,
    val websiteUrl: String,
    val currentRank: Int,
    val predictedRank: Int
) : Serializable
