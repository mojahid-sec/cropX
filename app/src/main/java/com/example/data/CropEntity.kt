package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crops")
data class CropEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nameBn: String,
    val nameEn: String,
    val variety: String = "",
    val category: String,
    val season: String,
    val durationDays: Int,
    val status: String, // "active", "planned", "harvested"
    val drawableResName: String,
    val marketPriceRange: String = "",
    val plantingDate: Long = System.currentTimeMillis(),
    val landArea: Double = 0.0,
    val landAreaUnit: String = "shotok",
    val notes: String = "",
    val isCustom: Boolean = false
)
