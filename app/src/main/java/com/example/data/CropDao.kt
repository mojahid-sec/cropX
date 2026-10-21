package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CropDao {
    @Query("SELECT * FROM crops ORDER BY id ASC")
    fun getAllCrops(): Flow<List<CropEntity>>

    @Query("SELECT * FROM crops WHERE status = :status ORDER BY id ASC")
    fun getCropsByStatus(status: String): Flow<List<CropEntity>>

    @Query("SELECT COUNT(*) FROM crops WHERE status = 'active'")
    fun getActiveCropCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM crops")
    suspend fun getCropCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrop(crop: CropEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(crops: List<CropEntity>)

    @Update
    suspend fun updateCrop(crop: CropEntity)

    @Delete
    suspend fun deleteCrop(crop: CropEntity)

    @Query("SELECT * FROM crops WHERE id = :id LIMIT 1")
    suspend fun getCropById(id: Long): CropEntity?
}
