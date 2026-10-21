package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppPreferences
import com.example.data.CropEntity
import com.example.data.CropXDatabase
import com.example.data.CropXRepository
import com.example.data.ExpenseEntity
import com.example.data.FertilizerEntity
import com.example.data.HarvestEntity
import com.example.data.LandEntity
import com.example.data.PesticideEntity
import com.example.data.SaleEntity
import com.example.data.TransactionEntity
import com.example.model.AppLanguage
import com.example.model.CropXStrings
import com.example.model.LocalizedStrings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class NavTab {
    HOME,
    CROPS,
    LAND,
    ACCOUNTS,
    MORE
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CropXRepository
    private val preferences: AppPreferences = AppPreferences(application)

    private val _language = MutableStateFlow(preferences.language)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _isDarkMode = MutableStateFlow(preferences.isDarkMode)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _currentTab = MutableStateFlow(NavTab.HOME)
    val currentTab: StateFlow<NavTab> = _currentTab.asStateFlow()

    // Sub-screen navigation states
    private val _showingAboutDeveloper = MutableStateFlow(false)
    val showingAboutDeveloper: StateFlow<Boolean> = _showingAboutDeveloper.asStateFlow()

    private val _showingCalculator = MutableStateFlow(false)
    val showingCalculator: StateFlow<Boolean> = _showingCalculator.asStateFlow()

    init {
        val db = CropXDatabase.getDatabase(application)
        repository = CropXRepository(db)
        viewModelScope.launch {
            repository.checkAndSeedInitialCrops()
        }
    }

    val strings: StateFlow<CropXStrings> = _language.map { lang ->
        LocalizedStrings.get(lang)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        LocalizedStrings.get(_language.value)
    )

    // Crops
    val allCrops: StateFlow<List<CropEntity>> = repository.allCrops.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val activeCropCount: StateFlow<Int> = repository.activeCropCount.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )

    // Land
    val allLands: StateFlow<List<LandEntity>> = repository.allLands.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val totalLandShotok: StateFlow<Double> = allLands.map { lands ->
        lands.sumOf { it.areaInShotok() }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0.0
    )

    // Fertilizers
    val allFertilizers: StateFlow<List<FertilizerEntity>> = repository.allFertilizers.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // Pesticides
    val allPesticides: StateFlow<List<PesticideEntity>> = repository.allPesticides.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // Expenses
    val allExpenses: StateFlow<List<ExpenseEntity>> = repository.allExpenses.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val totalExpenses: StateFlow<Double> = repository.totalExpenses.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0.0
    )

    // Harvests
    val allHarvests: StateFlow<List<HarvestEntity>> = repository.allHarvests.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val totalHarvestQuantity: StateFlow<Double> = repository.totalHarvestQuantity.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0.0
    )

    // Sales
    val allSales: StateFlow<List<SaleEntity>> = repository.allSales.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val totalSales: StateFlow<Double> = repository.totalSales.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0.0
    )

    // Net Profit or Loss (Total Sales - Total Expenses, zero double-counting)
    val netProfitLoss: StateFlow<Double> = combine(totalSales, totalExpenses) { sales, expenses ->
        sales - expenses
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0.0
    )

    // Transactions (Ledger)
    val allTransactions: StateFlow<List<TransactionEntity>> = repository.allTransactions.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val recentTransactions: StateFlow<List<TransactionEntity>> = repository.recentTransactions.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // Preferences & Navigation
    fun setLanguage(newLanguage: AppLanguage) {
        _language.value = newLanguage
        preferences.language = newLanguage
    }

    fun setDarkMode(dark: Boolean) {
        _isDarkMode.value = dark
        preferences.isDarkMode = dark
    }

    fun selectTab(tab: NavTab) {
        _currentTab.value = tab
        _showingAboutDeveloper.value = false
        _showingCalculator.value = false
    }

    fun showAboutDeveloper(show: Boolean) {
        _showingAboutDeveloper.value = show
        if (show) _showingCalculator.value = false
    }

    fun showCalculator(show: Boolean) {
        _showingCalculator.value = show
        if (show) _showingAboutDeveloper.value = false
    }

    // Crop CRUD operations
    fun addCrop(
        nameBn: String,
        nameEn: String,
        variety: String,
        category: String,
        season: String,
        durationDays: Int,
        landArea: Double,
        landUnit: String,
        status: String,
        notes: String
    ) {
        viewModelScope.launch {
            val crop = CropEntity(
                nameBn = nameBn.trim(),
                nameEn = nameEn.trim(),
                variety = variety.trim(),
                category = category,
                season = season.trim(),
                durationDays = durationDays,
                landArea = landArea,
                landAreaUnit = landUnit,
                status = status,
                drawableResName = "crop_rice",
                notes = notes.trim(),
                isCustom = true
            )
            repository.insertCrop(crop)
        }
    }

    // Backward compatibility helper
    fun addCustomCrop(
        nameBn: String,
        nameEn: String,
        category: String,
        season: String,
        durationDays: Int,
        status: String,
        notes: String
    ) {
        addCrop(nameBn, nameEn, "", category, season, durationDays, 0.0, "shotok", status, notes)
    }

    fun updateCrop(crop: CropEntity) {
        viewModelScope.launch {
            repository.updateCrop(crop)
        }
    }

    fun deleteCrop(crop: CropEntity) {
        viewModelScope.launch {
            repository.deleteCrop(crop)
        }
    }

    // Land operations
    fun addLand(
        name: String,
        area: Double,
        unit: String,
        location: String,
        ownershipType: String,
        associatedCropName: String,
        soilType: String,
        notes: String
    ) {
        viewModelScope.launch {
            val land = LandEntity(
                name = name.trim(),
                area = area,
                unit = unit,
                location = location.trim(),
                ownershipType = ownershipType,
                associatedCropName = associatedCropName.trim(),
                soilType = soilType.trim(),
                notes = notes.trim()
            )
            repository.insertLand(land)
        }
    }

    fun updateLand(land: LandEntity) {
        viewModelScope.launch {
            repository.updateLand(land)
        }
    }

    fun deleteLand(land: LandEntity) {
        viewModelScope.launch {
            repository.deleteLand(land)
        }
    }

    // Fertilizer operations
    fun addFertilizer(
        name: String,
        quantity: Double,
        unit: String,
        price: Double,
        cropName: String,
        notes: String
    ) {
        viewModelScope.launch {
            val fertilizer = FertilizerEntity(
                name = name.trim(),
                quantity = quantity,
                unit = unit.trim(),
                price = price,
                cropName = cropName.trim(),
                notes = notes.trim(),
                applicationDate = System.currentTimeMillis()
            )
            repository.insertFertilizer(fertilizer)

            // If price > 0, also log as an expense in expenses & transactions
            if (price > 0) {
                val exp = ExpenseEntity(
                    category = "সার (Fertilizer)",
                    amount = price,
                    cropName = cropName.trim(),
                    description = "$name ($quantity $unit)",
                    date = System.currentTimeMillis()
                )
                repository.insertExpense(exp)
                repository.insertTransaction(
                    TransactionEntity(
                        type = "expense",
                        category = "সার (Fertilizer)",
                        amount = price,
                        relatedCropName = cropName.trim(),
                        notes = "$name ($quantity $unit)"
                    )
                )
            }
        }
    }

    fun deleteFertilizer(fertilizer: FertilizerEntity) {
        viewModelScope.launch {
            repository.deleteFertilizer(fertilizer)
        }
    }

    // Pesticide operations
    fun addPesticide(
        name: String,
        quantity: Double,
        unit: String,
        price: Double,
        cropName: String,
        notes: String
    ) {
        viewModelScope.launch {
            val pesticide = PesticideEntity(
                name = name.trim(),
                quantity = quantity,
                unit = unit.trim(),
                price = price,
                cropName = cropName.trim(),
                notes = notes.trim(),
                applicationDate = System.currentTimeMillis()
            )
            repository.insertPesticide(pesticide)

            if (price > 0) {
                val exp = ExpenseEntity(
                    category = "কীটনাশক (Pesticide)",
                    amount = price,
                    cropName = cropName.trim(),
                    description = "$name ($quantity $unit)",
                    date = System.currentTimeMillis()
                )
                repository.insertExpense(exp)
                repository.insertTransaction(
                    TransactionEntity(
                        type = "expense",
                        category = "কীটনাশক (Pesticide)",
                        amount = price,
                        relatedCropName = cropName.trim(),
                        notes = "$name ($quantity $unit)"
                    )
                )
            }
        }
    }

    fun deletePesticide(pesticide: PesticideEntity) {
        viewModelScope.launch {
            repository.deletePesticide(pesticide)
        }
    }

    // Expense operations
    fun addExpense(
        category: String,
        amount: Double,
        cropName: String,
        description: String
    ) {
        viewModelScope.launch {
            val expense = ExpenseEntity(
                category = category.trim(),
                amount = amount,
                cropName = cropName.trim(),
                description = description.trim(),
                date = System.currentTimeMillis()
            )
            repository.insertExpense(expense)

            // Sync with transaction ledger
            repository.insertTransaction(
                TransactionEntity(
                    type = "expense",
                    category = category.trim(),
                    amount = amount,
                    relatedCropName = cropName.trim(),
                    notes = description.trim(),
                    dateTimestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }

    // Harvest operations (Separate from sales records)
    fun addHarvest(
        cropName: String,
        quantity: Double,
        unit: String,
        notes: String
    ) {
        viewModelScope.launch {
            val harvest = HarvestEntity(
                cropName = cropName.trim(),
                quantity = quantity,
                unit = unit.trim(),
                notes = notes.trim(),
                date = System.currentTimeMillis()
            )
            repository.insertHarvest(harvest)
        }
    }

    fun deleteHarvest(harvest: HarvestEntity) {
        viewModelScope.launch {
            repository.deleteHarvest(harvest)
        }
    }

    // Sales operations
    fun addSale(
        cropName: String,
        quantity: Double,
        unit: String,
        sellingPrice: Double,
        buyer: String,
        notes: String
    ) {
        viewModelScope.launch {
            val totalAmount = quantity * sellingPrice
            val sale = SaleEntity(
                cropName = cropName.trim(),
                quantity = quantity,
                unit = unit.trim(),
                sellingPrice = sellingPrice,
                totalAmount = totalAmount,
                buyer = buyer.trim(),
                notes = notes.trim(),
                date = System.currentTimeMillis()
            )
            repository.insertSale(sale)

            // Sync with transaction ledger
            repository.insertTransaction(
                TransactionEntity(
                    type = "income",
                    category = "ফসল বিক্রি (Crop Sale)",
                    amount = totalAmount,
                    relatedCropName = cropName.trim(),
                    notes = "$cropName $quantity $unit @ ৳$sellingPrice (ক্রেতা: $buyer)",
                    dateTimestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteSale(sale: SaleEntity) {
        viewModelScope.launch {
            repository.deleteSale(sale)
        }
    }

    // Direct transaction support
    fun addTransaction(
        type: String,
        category: String,
        amount: Double,
        crop: String,
        land: String,
        notes: String
    ) {
        if (type == "expense") {
            addExpense(category, amount, crop, notes)
        } else {
            addSale(crop.ifBlank { "ফসল" }, 1.0, "লট", amount, land, notes)
        }
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }
}
