package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pesticides")
data class PesticideEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val quantity: Double,
    val unit: String = "মিলি / ml",
    val price: Double = 0.0,
    val applicationDate: Long = System.currentTimeMillis(),
    val cropName: String = "",
    val notes: String = ""
)
