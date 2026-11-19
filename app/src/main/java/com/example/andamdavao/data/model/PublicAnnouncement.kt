package com.example.andamdavao.data.model

/**
 * Data model for public emergency announcements and advisories.
 */
data class PublicAnnouncement(
    val id: String,
    val title: String,
    val body: String,
    val severity: String, // "Urgent", "Advisory", "Info"
    val affectedAreas: String,
    val issuingAgency: String,
    val sourceUrl: String,
    val publishedAt: String,
    val expiresAt: String,
    val isHistory: Boolean = false
)
