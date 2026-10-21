package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FertilizerDao {
    @Query("SELECT * FROM fertilizers ORDER BY applicationDate DESC")
    fun getAllFertilizers(): Flow<List<FertilizerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFertilizer(fertilizer: FertilizerEntity): Long

    @Update
    suspend fun updateFertilizer(fertilizer: FertilizerEntity)

    @Delete
    suspend fun deleteFertilizer(fertilizer: FertilizerEntity)
}
