package com.example.andamdavao.data.repository

import com.example.andamdavao.data.model.PublicAnnouncement
import com.example.andamdavao.data.model.SampleData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AnnouncementRepository {

    private val firestore: FirebaseFirestore? by lazy {
        try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun fetchPublicAnnouncements(): List<PublicAnnouncement> {
        return try {
            val db = firestore ?: return SampleData.sampleAnnouncements
            val snapshot = db.collection("announcements")
                .whereEqualTo("status", "published")
                .get()
                .await()

            if (snapshot.isEmpty) {
                SampleData.sampleAnnouncements
            } else {
                snapshot.documents.mapNotNull { doc ->
                    PublicAnnouncement(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        body = doc.getString("body") ?: "",
                        severity = doc.getString("severity") ?: "Advisory",
                        affectedAreas = doc.getString("affectedAreas") ?: "Davao City",
                        issuingAgency = doc.getString("issuingAgency") ?: "DRRMO",
                        sourceUrl = doc.getString("sourceUrl") ?: "https://www.davaocity.gov.ph",
                        publishedAt = doc.getString("publishedAt") ?: "Recently",
                        expiresAt = doc.getString("expiresAt") ?: "",
                        isHistory = doc.getBoolean("isHistory") ?: false
                    )
                }
            }
        } catch (e: Exception) {
            SampleData.sampleAnnouncements
        }
    }
}
