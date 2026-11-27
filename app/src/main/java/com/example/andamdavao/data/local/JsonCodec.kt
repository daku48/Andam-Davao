package com.example.andamdavao.data.local

import com.example.andamdavao.data.model.EvacuationCenter
import com.example.andamdavao.data.model.PublicAnnouncement
import com.example.andamdavao.data.model.WeatherData
import org.json.JSONArray
import org.json.JSONObject

/** Hand-written JSON so the offline cache needs no extra libraries. */
object JsonCodec {

    fun centersToJson(centers: List<EvacuationCenter>): String {
        val array = JSONArray()
        centers.forEach { c ->
            array.put(
                JSONObject()
                    .put("id", c.id)
                    .put("centerName", c.centerName)
                    .put("address", c.address)
                    .put("barangay", c.barangay)
                    .put("district", c.district)
                    .put("latitude", c.latitude)
                    .put("longitude", c.longitude)
                    .put("facilities", JSONArray(c.facilities))
                    .put("capacity", c.capacity)
                    .put("currentOccupancy", c.currentOccupancy)
                    .put("status", c.status)
                    .put("verificationStatus", c.verificationStatus)
                    .put("verifiedAt", c.verifiedAt)
                    .put("verifiedBy", c.verifiedBy)
                    .put("contactNumber", c.contactNumber)
            )
        }
        return array.toString()
    }

    fun centersFromJson(json: String): List<EvacuationCenter> {
        val array = JSONArray(json)
        return (0 until array.length()).map { i ->
            val o = array.getJSONObject(i)
            val facilities = o.optJSONArray("facilities")
            EvacuationCenter(
                id = o.getString("id"),
                centerName = o.optString("centerName"),
                address = o.optString("address"),
                barangay = o.optString("barangay"),
                district = o.optString("district"),
                latitude = o.getDouble("latitude"),
                longitude = o.getDouble("longitude"),
                facilities = if (facilities == null) emptyList()
                else (0 until facilities.length()).map { facilities.getString(it) },
                capacity = o.optInt("capacity"),
                currentOccupancy = o.optInt("currentOccupancy"),
                status = o.optString("status", "Status unknown"),
                verificationStatus = o.optString("verificationStatus"),
                verifiedAt = o.optString("verifiedAt"),
                verifiedBy = o.optString("verifiedBy"),
                contactNumber = o.optString("contactNumber")
            )
        }
    }

    fun announcementsToJson(items: List<PublicAnnouncement>): String {
        val array = JSONArray()
        items.forEach { a ->
            array.put(
                JSONObject()
                    .put("id", a.id)
                    .put("title", a.title)
                    .put("body", a.body)
                    .put("severity", a.severity)
                    .put("affectedAreas", a.affectedAreas)
                    .put("issuingAgency", a.issuingAgency)
                    .put("sourceUrl", a.sourceUrl)
                    .put("publishedAt", a.publishedAt)
                    .put("expiresAt", a.expiresAt)
                    .put("isHistory", a.isHistory)
            )
        }
        return array.toString()
    }

    fun announcementsFromJson(json: String): List<PublicAnnouncement> {
        val array = JSONArray(json)
        return (0 until array.length()).map { i ->
            val o = array.getJSONObject(i)
            PublicAnnouncement(
                id = o.getString("id"),
                title = o.optString("title"),
                body = o.optString("body"),
                severity = o.optString("severity", "Advisory"),
                affectedAreas = o.optString("affectedAreas"),
                issuingAgency = o.optString("issuingAgency"),
                sourceUrl = o.optString("sourceUrl"),
                publishedAt = o.optString("publishedAt"),
                expiresAt = o.optString("expiresAt"),
                isHistory = o.optBoolean("isHistory", false)
            )
        }
    }

    fun weatherToJson(w: WeatherData): String {
        val o = JSONObject()
            .put("temperature", w.temperature)
            .put("condition", w.condition)
            .put("windSpeed", w.windSpeed)
            .put("fetchedAtMillis", w.fetchedAtMillis)
            .put("isAlertLevel", w.isAlertLevel)
        w.humidity?.let { o.put("humidity", it) }
        w.rainProbability?.let { o.put("rainProbability", it) }
        return o.toString()
    }

    fun weatherFromJson(json: String): WeatherData {
        val o = JSONObject(json)
        return WeatherData(
            temperature = o.getDouble("temperature"),
            condition = o.getString("condition"),
            humidity = if (o.has("humidity")) o.getInt("humidity") else null,
            windSpeed = o.getDouble("windSpeed"),
            rainProbability = if (o.has("rainProbability")) o.getInt("rainProbability") else null,
            fetchedAtMillis = o.getLong("fetchedAtMillis"),
            isAlertLevel = o.optBoolean("isAlertLevel", false)
        )
    }
}
