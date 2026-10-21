package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LandDao {
    @Query("SELECT * FROM lands ORDER BY id DESC")
    fun getAllLands(): Flow<List<LandEntity>>

    @Query("SELECT COUNT(*) FROM lands")
    fun getLandCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLand(land: LandEntity): Long

    @Update
    suspend fun updateLand(land: LandEntity)

    @Delete
    suspend fun deleteLand(land: LandEntity)

    @Query("SELECT * FROM lands WHERE id = :id LIMIT 1")
    suspend fun getLandById(id: Long): LandEntity?
}
