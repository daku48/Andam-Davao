package com.example.andamdavao.data.model

/**
 * Data model for Evacuation Center records in Davao City.
 */
data class EvacuationCenter(
    val id: String,
    val centerName: String,
    val address: String,
    val barangay: String,
    val district: String,
    val latitude: Double,
    val longitude: Double,
    val facilities: List<String>,
    val capacity: Int,
    val currentOccupancy: Int,
    val status: String, // "Open", "Full", "Closed", "Status unknown"
    val verificationStatus: String, // "Verified", "Awaiting Verification", "Needing Review"
    val verifiedAt: String,
    val verifiedBy: String,
    val contactNumber: String,
    val distanceKm: Double = 1.2
)
