package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "harvests")
data class HarvestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cropName: String,
    val quantity: Double,
    val unit: String = "কেজি / kg",
    val date: Long = System.currentTimeMillis(),
    val notes: String = ""
)
