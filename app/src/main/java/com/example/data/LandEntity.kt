package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lands")
data class LandEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val area: Double,
    val unit: String, // "shotok", "bigha", "acre", "hectare"
    val location: String,
    val ownershipType: String, // "own", "leased", "sharecrop"
    val associatedCropId: Long? = null,
    val associatedCropName: String = "",
    val soilType: String = "",
    val notes: String = ""
) {
    /**
     * Converts any unit area to standard Shotok (Decimal).
     * 1 Bigha = 33 Shotok
     * 1 Acre = 100 Shotok
     * 1 Hectare = 247.1 Shotok
     */
    fun areaInShotok(): Double {
        return when (unit.lowercase()) {
            "bigha" -> area * 33.0
            "acre" -> area * 100.0
            "hectare" -> area * 247.1
            else -> area
        }
    }
}
