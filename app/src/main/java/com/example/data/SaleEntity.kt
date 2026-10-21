package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sales")
data class SaleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cropName: String,
    val quantity: Double,
    val unit: String = "কেজি / kg",
    val sellingPrice: Double, // unit price
    val totalAmount: Double, // quantity * sellingPrice
    val buyer: String = "",
    val date: Long = System.currentTimeMillis(),
    val notes: String = ""
)
