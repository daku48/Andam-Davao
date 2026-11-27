package com.example.andamdavao

import com.example.andamdavao.data.model.EvacuationCenter

fun testCenter(
    id: String,
    name: String = "Center $id",
    barangay: String = "Barangay $id",
    district: String = "District $id",
    status: String = "Open",
    lat: Double = 7.0722,
    lon: Double = 125.6131
) = EvacuationCenter(
    id = id,
    centerName = name,
    address = "Some street",
    barangay = barangay,
    district = district,
    latitude = lat,
    longitude = lon,
    facilities = listOf("Restrooms"),
    capacity = 100,
    currentOccupancy = 10,
    status = status,
    verificationStatus = "Verified",
    verifiedAt = "Today",
    verifiedBy = "DRRMO",
    contactNumber = "(082) 000-0000"
)
