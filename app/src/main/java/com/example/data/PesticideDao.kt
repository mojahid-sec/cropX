package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PesticideDao {
    @Query("SELECT * FROM pesticides ORDER BY applicationDate DESC")
    fun getAllPesticides(): Flow<List<PesticideEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPesticide(pesticide: PesticideEntity): Long

    @Update
    suspend fun updatePesticide(pesticide: PesticideEntity)

    @Delete
    suspend fun deletePesticide(pesticide: PesticideEntity)
}
