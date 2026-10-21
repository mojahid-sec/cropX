package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // "expense" or "income"
    val category: String,
    val amount: Double,
    val dateTimestamp: Long = System.currentTimeMillis(),
    val relatedCropName: String = "",
    val relatedLandName: String = "",
    val notes: String = ""
)
