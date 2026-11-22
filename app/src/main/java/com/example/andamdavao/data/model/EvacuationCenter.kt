package com.example.andamdavao.data.model

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
    val status: String,
    val verificationStatus: String,
    val verifiedAt: String,
    val verifiedBy: String,
    val contactNumber: String,
    val distanceKm: Double = 1.2
)
