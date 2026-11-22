package com.example.andamdavao.data.model

data class PublicAnnouncement(
    val id: String,
    val title: String,
    val body: String,
    val severity: String,
    val affectedAreas: String,
    val issuingAgency: String,
    val sourceUrl: String,
    val publishedAt: String,
    val expiresAt: String,
    val isHistory: Boolean = false
)
