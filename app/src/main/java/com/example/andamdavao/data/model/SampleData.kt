package com.example.andamdavao.data.model

object SampleData {
    val sampleCenters = listOf(
        EvacuationCenter(
            id = "ec_001",
            centerName = "Rizal Memorial Coliseum",
            address = "Rizal Park, Poblacion District",
            barangay = "Poblacion 4-A",
            district = "Poblacion District",
            latitude = 7.0655,
            longitude = 125.6089,
            facilities = listOf("Large Hall", "Restrooms", "Water Supply", "First Aid", "Generator"),
            capacity = 1000,
            currentOccupancy = 400,
            status = "Open",
            verificationStatus = "Verified",
            verifiedAt = "Today at 08:30 AM",
            verifiedBy = "Davao City DRRMO Inspector",
            contactNumber = "(082) 221-1247",
            distanceKm = 1.2
        ),
        EvacuationCenter(
            id = "ec_002",
            centerName = "Davao City National High School Gym",
            address = "F. Torres St, Poblacion District",
            barangay = "Poblacion 8-A",
            district = "Poblacion District",
            latitude = 7.0782,
            longitude = 125.6120,
            facilities = listOf("Covered Court", "Restrooms", "Water Supply", "First Aid"),
            capacity = 800,
            currentOccupancy = 780,
            status = "Full",
            verificationStatus = "Verified",
            verifiedAt = "Today at 09:15 AM",
            verifiedBy = "Barangay Disaster Response Team",
            contactNumber = "(082) 227-3105",
            distanceKm = 2.4
        ),
        EvacuationCenter(
            id = "ec_003",
            centerName = "Calinan Gymnasium",
            address = "McArthur Highway, Calinan",
            barangay = "Calinan Poblacion",
            district = "Calinan District",
            latitude = 7.1852,
            longitude = 125.4598,
            facilities = listOf("Large Hall", "Restrooms", "Generator"),
            capacity = 600,
            currentOccupancy = 120,
            status = "Open",
            verificationStatus = "Verified",
            verifiedAt = "Yesterday at 04:00 PM",
            verifiedBy = "Calinan Admin Officer",
            contactNumber = "(082) 295-0122",
            distanceKm = 15.8
        ),
        EvacuationCenter(
            id = "ec_004",
            centerName = "Toril Cultural Center",
            address = "Agton St, Toril",
            barangay = "Toril Poblacion",
            district = "Toril District",
            latitude = 7.0189,
            longitude = 125.4981,
            facilities = listOf("Covered Court", "Restrooms", "Water Supply"),
            capacity = 500,
            currentOccupancy = 0,
            status = "Closed",
            verificationStatus = "Verified",
            verifiedAt = "Today at 07:00 AM",
            verifiedBy = "Toril DRRMO Representative",
            contactNumber = "(082) 291-0044",
            distanceKm = 12.1
        ),
        EvacuationCenter(
            id = "ec_005",
            centerName = "Bunawan Barangay Hall",
            address = "Km 23, Bunawan",
            barangay = "Bunawan",
            district = "Bunawan District",
            latitude = 7.2341,
            longitude = 125.6420,
            facilities = listOf("Multipurpose Room", "Restrooms"),
            capacity = 300,
            currentOccupancy = 0,
            status = "Status unknown",
            verificationStatus = "Awaiting Verification",
            verifiedAt = "Pending Verification",
            verifiedBy = "Unassigned",
            contactNumber = "(082) 236-0011",
            distanceKm = 22.5
        )
    )

    val sampleAnnouncements = listOf(
        PublicAnnouncement(
            id = "ann_001",
            title = "Flood Warning — Matina & Davao River Basin",
            body = "Moderate to heavy rains expected due to localized thunderstorm activity. Low-lying communities near Matina River and Davao River are advised to remain vigilant and prepare Go-Bags.",
            severity = "Urgent",
            affectedAreas = "Matina Crossing, Maa, Bankerohan, Bucana",
            issuingAgency = "Davao City DRRMO / PAGASA",
            sourceUrl = "https://www.davaocity.gov.ph",
            publishedAt = "2026-11-19 08:00 AM",
            expiresAt = "2026-11-20 08:00 AM"
        ),
        PublicAnnouncement(
            id = "ann_002",
            title = "Typhoon Advisory — Pre-Emptive Evacuation Prep",
            body = "Tropical Depression Paolo is monitoring 450km east of Mindanao. Pre-emptive evacuation centers in District 1 and District 2 have been verified open.",
            severity = "Advisory",
            affectedAreas = "Davao City Coastal & Mountainous Barangays",
            issuingAgency = "PAGASA / Office of Civil Defense Region XI",
            sourceUrl = "https://pagasa.dost.gov.ph",
            publishedAt = "2026-11-18 06:30 PM",
            expiresAt = "2026-11-21 12:00 PM"
        ),
        PublicAnnouncement(
            id = "ann_003",
            title = "Scheduled Power Maintenance Advisory",
            body = "DLPC scheduled maintenance affecting Calinan and Baguio districts from 8:00 AM to 12:00 PM for line clearing.",
            severity = "Info",
            affectedAreas = "Calinan, Baguio District",
            issuingAgency = "Davao Light & Power Co.",
            sourceUrl = "https://www.davaolight.com",
            publishedAt = "2026-11-18 02:00 PM",
            expiresAt = "2026-11-19 01:00 PM"
        ),
        PublicAnnouncement(
            id = "ann_004",
            title = "Completed City-Wide Earthquake Drill",
            body = "The Q4 Nationwide Simultaneous Earthquake Drill for Davao City has been completed successfully.",
            severity = "Info",
            affectedAreas = "All Districts, Davao City",
            issuingAgency = "Davao City DRRMO",
            sourceUrl = "https://www.davaocity.gov.ph",
            publishedAt = "2026-11-15 10:00 AM",
            expiresAt = "2026-11-16 10:00 AM",
            isHistory = true
        )
    )
}
