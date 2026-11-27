package com.example.andamdavao

import com.example.andamdavao.utils.CenterLogic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class CenterLogicTest {

    private val centers = listOf(
        testCenter("1", name = "Rizal Gym", barangay = "Poblacion", status = "Open"),
        testCenter("2", name = "Calinan Hall", barangay = "Calinan", district = "Calinan District", status = "Full"),
        testCenter("3", name = "Toril Center", barangay = "Toril", status = "Closed")
    )

    @Test
    fun emptyQuery_returnsAll() {
        assertEquals(3, CenterLogic.filter(centers, "").size)
    }

    @Test
    fun query_matchesNameBarangayAndDistrict_ignoringCase() {
        assertEquals(listOf("1"), CenterLogic.filter(centers, "rizal").map { it.id })
        assertEquals(listOf("3"), CenterLogic.filter(centers, "TORIL").map { it.id })
        assertEquals(listOf("2"), CenterLogic.filter(centers, "calinan district").map { it.id })
    }

    @Test
    fun query_isTrimmed() {
        assertEquals(listOf("1"), CenterLogic.filter(centers, "  rizal  ").map { it.id })
    }

    @Test
    fun statusFilter_works() {
        assertEquals(listOf("2"), CenterLogic.filter(centers, "", "Full").map { it.id })
        assertEquals(3, CenterLogic.filter(centers, "", "All").size)
    }

    @Test
    fun withDistances_sortsNearestFirst() {
        val near = testCenter("near", lat = 7.0730, lon = 125.6140)
        val far = testCenter("far", lat = 7.1852, lon = 125.4598)
        val sorted = CenterLogic.withDistances(listOf(far, near), 7.0722, 125.6131)
        assertEquals(listOf("near", "far"), sorted.map { it.id })
        assertNotNull(sorted[0].distanceKm)
    }

    @Test
    fun centersStartWithNoDistance() {
        assertNull(testCenter("x").distanceKm)
    }
}
