package com.example.andamdavao.utils

import com.example.andamdavao.data.model.EvacuationCenter

object CenterLogic {

    fun filter(
        centers: List<EvacuationCenter>,
        query: String,
        statusFilter: String = "All"
    ): List<EvacuationCenter> {
        val q = query.trim()
        return centers.filter { center ->
            val matchesSearch = q.isEmpty() ||
                    center.centerName.contains(q, ignoreCase = true) ||
                    center.barangay.contains(q, ignoreCase = true) ||
                    center.district.contains(q, ignoreCase = true)
            val matchesStatus = statusFilter.equals("All", ignoreCase = true) ||
                    center.status.equals(statusFilter, ignoreCase = true)
            matchesSearch && matchesStatus
        }
    }

    /** Fills in distanceKm from the given position and sorts nearest first. */
    fun withDistances(
        centers: List<EvacuationCenter>,
        latitude: Double,
        longitude: Double
    ): List<EvacuationCenter> {
        return centers
            .map {
                it.copy(
                    distanceKm = LocationUtils.calculateDistanceKm(
                        latitude, longitude, it.latitude, it.longitude
                    )
                )
            }
            .sortedBy { it.distanceKm }
    }
}
