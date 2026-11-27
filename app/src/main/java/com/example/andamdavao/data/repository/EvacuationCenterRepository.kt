package com.example.andamdavao.data.repository

import com.example.andamdavao.data.model.EvacuationCenter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class EvacuationCenterRepository {

    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    /**
     * Failure means "could not reach or read the server" (including Firebase not set up yet).
     * Callers decide what to show; this class never invents data.
     */
    suspend fun fetchPublicEvacuationCenters(): Result<List<EvacuationCenter>> {
        return try {
            val snapshot = firestore.collection("evacuation_centers")
                .whereIn("verificationStatus", listOf("Verified", "Approved"))
                .get()
                .await()

            val centers = snapshot.documents.map { doc ->
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
                    verificationStatus = doc.getString("verificationStatus") ?: "",
                    verifiedAt = doc.getString("verifiedAt") ?: "Recently",
                    verifiedBy = doc.getString("verifiedBy") ?: "Official DRRMO",
                    contactNumber = doc.getString("contactNumber") ?: ""
                )
            }.filter { it.isUsable() }

            Result.success(centers)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // A center with no name or no coordinates would show up as a pin at 0,0.
    private fun EvacuationCenter.isUsable(): Boolean =
        centerName.isNotBlank() && !(latitude == 0.0 && longitude == 0.0)
}
