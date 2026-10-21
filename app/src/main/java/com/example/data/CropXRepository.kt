package com.example.data

import kotlinx.coroutines.flow.Flow

class CropXRepository(private val db: CropXDatabase) {
    val allCrops: Flow<List<CropEntity>> = db.cropDao().getAllCrops()
    val activeCropCount: Flow<Int> = db.cropDao().getActiveCropCount()

    val allLands: Flow<List<LandEntity>> = db.landDao().getAllLands()
    val landCount: Flow<Int> = db.landDao().getLandCount()

    val allExpenses: Flow<List<ExpenseEntity>> = db.expenseDao().getAllExpenses()
    val totalExpenses: Flow<Double> = db.expenseDao().getTotalExpenses()

    val allSales: Flow<List<SaleEntity>> = db.saleDao().getAllSales()
    val totalSales: Flow<Double> = db.saleDao().getTotalSales()

    val allHarvests: Flow<List<HarvestEntity>> = db.harvestDao().getAllHarvests()
    val totalHarvestQuantity: Flow<Double> = db.harvestDao().getTotalHarvestQuantity()

    val allFertilizers: Flow<List<FertilizerEntity>> = db.fertilizerDao().getAllFertilizers()
    val allPesticides: Flow<List<PesticideEntity>> = db.pesticideDao().getAllPesticides()

    // Backward compatibility for existing transaction ledger
    val allTransactions: Flow<List<TransactionEntity>> = db.transactionDao().getAllTransactions()
    val recentTransactions: Flow<List<TransactionEntity>> = db.transactionDao().getRecentTransactions(5)

    suspend fun checkAndSeedInitialCrops() {
        val count = db.cropDao().getCropCount()
        if (count == 0) {
            db.cropDao().insertAll(InitialCropData.initialCrops)
        }
    }

    // Crop operations
    suspend fun insertCrop(crop: CropEntity): Long = db.cropDao().insertCrop(crop)
    suspend fun updateCrop(crop: CropEntity) = db.cropDao().updateCrop(crop)
    suspend fun deleteCrop(crop: CropEntity) = db.cropDao().deleteCrop(crop)

    // Land operations
    suspend fun insertLand(land: LandEntity): Long = db.landDao().insertLand(land)
    suspend fun updateLand(land: LandEntity) = db.landDao().updateLand(land)
    suspend fun deleteLand(land: LandEntity) = db.landDao().deleteLand(land)

    // Fertilizer operations
    suspend fun insertFertilizer(fertilizer: FertilizerEntity): Long = db.fertilizerDao().insertFertilizer(fertilizer)
    suspend fun updateFertilizer(fertilizer: FertilizerEntity) = db.fertilizerDao().updateFertilizer(fertilizer)
    suspend fun deleteFertilizer(fertilizer: FertilizerEntity) = db.fertilizerDao().deleteFertilizer(fertilizer)

    // Pesticide operations
    suspend fun insertPesticide(pesticide: PesticideEntity): Long = db.pesticideDao().insertPesticide(pesticide)
    suspend fun updatePesticide(pesticide: PesticideEntity) = db.pesticideDao().updatePesticide(pesticide)
    suspend fun deletePesticide(pesticide: PesticideEntity) = db.pesticideDao().deletePesticide(pesticide)

    // Expense operations
    suspend fun insertExpense(expense: ExpenseEntity): Long = db.expenseDao().insertExpense(expense)
    suspend fun updateExpense(expense: ExpenseEntity) = db.expenseDao().updateExpense(expense)
    suspend fun deleteExpense(expense: ExpenseEntity) = db.expenseDao().deleteExpense(expense)

    // Harvest operations
    suspend fun insertHarvest(harvest: HarvestEntity): Long = db.harvestDao().insertHarvest(harvest)
    suspend fun updateHarvest(harvest: HarvestEntity) = db.harvestDao().updateHarvest(harvest)
    suspend fun deleteHarvest(harvest: HarvestEntity) = db.harvestDao().deleteHarvest(harvest)

    // Sale operations
    suspend fun insertSale(sale: SaleEntity): Long = db.saleDao().insertSale(sale)
    suspend fun updateSale(sale: SaleEntity) = db.saleDao().updateSale(sale)
    suspend fun deleteSale(sale: SaleEntity) = db.saleDao().deleteSale(sale)

    // Transaction operations (synchronized)
    suspend fun insertTransaction(transaction: TransactionEntity): Long =
        db.transactionDao().insertTransaction(transaction)
    suspend fun deleteTransaction(transaction: TransactionEntity) =
        db.transactionDao().deleteTransaction(transaction)
}
