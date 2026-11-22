package com.example.andamdavao.data.repository

import com.example.andamdavao.data.model.EvacuationCenter
import com.example.andamdavao.data.model.SampleData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EvacuationCenterRepository {

    private val firestore: FirebaseFirestore? by lazy {
        try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun fetchPublicEvacuationCenters(): List<EvacuationCenter> {
        return try {
            val db = firestore ?: return SampleData.sampleCenters
            val snapshot = db.collection("evacuation_centers")
                .whereIn("verificationStatus", listOf("Verified", "Approved"))
                .get()
                .await()

            if (snapshot.isEmpty) {
                SampleData.sampleCenters
            } else {
                snapshot.documents.mapNotNull { doc ->
                    EvacuationCenter(
                        id = doc.id,
                        centerName = doc.getString("centerName") ?: "",
                        address = doc.getString("address") ?: "",
                        barangay = doc.getString("barangay") ?: "",
                        district = doc.getString("district") ?: "",
                        latitude = doc.getDouble("latitude") ?: 0.0,
                        longitude = doc.getDouble("longitude") ?: 0.0,
                        facilities = (doc.get("facilities") as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                        capacity = doc.getLong("capacity")?.toInt() ?: 0,
                        currentOccupancy = doc.getLong("currentOccupancy")?.toInt() ?: 0,
                        status = doc.getString("status") ?: "Status unknown",
                        verificationStatus = doc.getString("verificationStatus") ?: "Verified",
                        verifiedAt = doc.getString("verifiedAt") ?: "Recently",
                        verifiedBy = doc.getString("verifiedBy") ?: "Official DRRMO",
                        contactNumber = doc.getString("contactNumber") ?: ""
                    )
                }
            }
        } catch (e: Exception) {
            SampleData.sampleCenters
        }
    }
}
