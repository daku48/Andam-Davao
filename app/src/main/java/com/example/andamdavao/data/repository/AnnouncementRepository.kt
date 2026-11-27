package com.example.andamdavao.data.repository

import com.example.andamdavao.data.model.PublicAnnouncement
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class AnnouncementRepository {

    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    suspend fun fetchPublicAnnouncements(): Result<List<PublicAnnouncement>> {
        return try {
            val snapshot = firestore.collection("announcements")
                .whereEqualTo("status", "published")
                .get()
                .await()

            val items = snapshot.documents.map { doc ->
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
            }.filter { it.title.isNotBlank() }

            Result.success(items)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
