package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        CropEntity::class,
        LandEntity::class,
        TransactionEntity::class,
        FertilizerEntity::class,
        PesticideEntity::class,
        ExpenseEntity::class,
        HarvestEntity::class,
        SaleEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class CropXDatabase : RoomDatabase() {
    abstract fun cropDao(): CropDao
    abstract fun landDao(): LandDao
    abstract fun transactionDao(): TransactionDao
    abstract fun fertilizerDao(): FertilizerDao
    abstract fun pesticideDao(): PesticideDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun harvestDao(): HarvestDao
    abstract fun saleDao(): SaleDao

    companion object {
        @Volatile
        private var INSTANCE: CropXDatabase? = null

        fun getDatabase(context: Context): CropXDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CropXDatabase::class.java,
                    "cropx_agri.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
