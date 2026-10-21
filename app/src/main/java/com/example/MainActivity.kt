package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppLanguage
import com.example.ui.MainViewModel
import com.example.ui.NavTab
import com.example.ui.components.AppWatermarkContainer
import com.example.ui.screens.AboutDeveloperScreen
import com.example.ui.screens.AccountsScreen
import com.example.ui.screens.CropsScreen
import com.example.ui.screens.FarmingCalculatorScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LandScreen
import com.example.ui.screens.MoreScreen
import com.example.ui.theme.CropXTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = viewModel()
            val isDarkMode by mainViewModel.isDarkMode.collectAsStateWithLifecycle()
            val currentLanguage by mainViewModel.language.collectAsStateWithLifecycle()
            val currentTab by mainViewModel.currentTab.collectAsStateWithLifecycle()
            val showingAboutDeveloper by mainViewModel.showingAboutDeveloper.collectAsStateWithLifecycle()
            val showingCalculator by mainViewModel.showingCalculator.collectAsStateWithLifecycle()
            val strings by mainViewModel.strings.collectAsStateWithLifecycle()

            val crops by mainViewModel.allCrops.collectAsStateWithLifecycle()
            val activeCropCount by mainViewModel.activeCropCount.collectAsStateWithLifecycle()

            val lands by mainViewModel.allLands.collectAsStateWithLifecycle()
            val totalLandShotok by mainViewModel.totalLandShotok.collectAsStateWithLifecycle()

            val expenses by mainViewModel.allExpenses.collectAsStateWithLifecycle()
            val sales by mainViewModel.allSales.collectAsStateWithLifecycle()
            val harvests by mainViewModel.allHarvests.collectAsStateWithLifecycle()
            val fertilizers by mainViewModel.allFertilizers.collectAsStateWithLifecycle()
            val pesticides by mainViewModel.allPesticides.collectAsStateWithLifecycle()

            val totalExpenses by mainViewModel.totalExpenses.collectAsStateWithLifecycle()
            val totalSales by mainViewModel.totalSales.collectAsStateWithLifecycle()
            val totalHarvestQuantity by mainViewModel.totalHarvestQuantity.collectAsStateWithLifecycle()
            val netProfitLoss by mainViewModel.netProfitLoss.collectAsStateWithLifecycle()

            val recentTransactions by mainViewModel.recentTransactions.collectAsStateWithLifecycle()

            CropXTheme(darkTheme = isDarkMode) {
                AppWatermarkContainer {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        contentWindowInsets = WindowInsets.safeDrawing,
                        bottomBar = {
                            if (!showingAboutDeveloper && !showingCalculator) {
                                NavigationBar(
                                    modifier = Modifier.testTag("main_bottom_nav")
                                ) {
                                    NavigationBarItem(
                                        selected = currentTab == NavTab.HOME,
                                        onClick = { mainViewModel.selectTab(NavTab.HOME) },
                                        icon = { Icon(Icons.Default.Home, contentDescription = strings.navHome) },
                                        label = {
                                            Text(
                                                text = strings.navHome,
                                                fontWeight = if (currentTab == NavTab.HOME) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        modifier = Modifier.testTag("nav_tab_home")
                                    )

                                    NavigationBarItem(
                                        selected = currentTab == NavTab.CROPS,
                                        onClick = { mainViewModel.selectTab(NavTab.CROPS) },
                                        icon = { Icon(Icons.Default.Spa, contentDescription = strings.navCrops) },
                                        label = {
                                            Text(
                                                text = strings.navCrops,
                                                fontWeight = if (currentTab == NavTab.CROPS) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        modifier = Modifier.testTag("nav_tab_crops")
                                    )

                                    NavigationBarItem(
                                        selected = currentTab == NavTab.LAND,
                                        onClick = { mainViewModel.selectTab(NavTab.LAND) },
                                        icon = { Icon(Icons.Default.Landscape, contentDescription = strings.navLand) },
                                        label = {
                                            Text(
                                                text = strings.navLand,
                                                fontWeight = if (currentTab == NavTab.LAND) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        modifier = Modifier.testTag("nav_tab_land")
                                    )

                                    NavigationBarItem(
                                        selected = currentTab == NavTab.ACCOUNTS,
                                        onClick = { mainViewModel.selectTab(NavTab.ACCOUNTS) },
                                        icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = strings.navAccounts) },
                                        label = {
                                            Text(
                                                text = strings.navAccounts,
                                                fontWeight = if (currentTab == NavTab.ACCOUNTS) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        modifier = Modifier.testTag("nav_tab_accounts")
                                    )

                                    NavigationBarItem(
                                        selected = currentTab == NavTab.MORE,
                                        onClick = { mainViewModel.selectTab(NavTab.MORE) },
                                        icon = { Icon(Icons.Default.MoreHoriz, contentDescription = strings.navMore) },
                                        label = {
                                            Text(
                                                text = strings.navMore,
                                                fontWeight = if (currentTab == NavTab.MORE) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        modifier = Modifier.testTag("nav_tab_more")
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        val contentModifier = Modifier.padding(innerPadding)

                        if (showingAboutDeveloper) {
                            AboutDeveloperScreen(
                                strings = strings,
                                currentLanguage = currentLanguage,
                                onBack = { mainViewModel.showAboutDeveloper(false) },
                                modifier = contentModifier
                            )
                        } else if (showingCalculator) {
                            FarmingCalculatorScreen(
                                strings = strings,
                                currentLanguage = currentLanguage,
                                onBack = { mainViewModel.showCalculator(false) },
                                modifier = contentModifier
                            )
                        } else {
                            when (currentTab) {
                                NavTab.HOME -> {
                                    HomeScreen(
                                        strings = strings,
                                        currentLanguage = currentLanguage,
                                        totalCrops = crops.size,
                                        totalLandShotok = totalLandShotok,
                                        activeCropCount = activeCropCount,
                                        totalExpenses = totalExpenses,
                                        totalSales = totalSales,
                                        totalHarvestQuantity = totalHarvestQuantity,
                                        netProfitLoss = netProfitLoss,
                                        recentTransactions = recentTransactions,
                                        onNavigateToTab = { mainViewModel.selectTab(it) },
                                        onNavigateToAbout = { mainViewModel.showAboutDeveloper(true) },
                                        onNavigateToCalculator = { mainViewModel.showCalculator(true) },
                                        onToggleLanguage = {
                                            val next = if (currentLanguage == AppLanguage.BANGLA) AppLanguage.ENGLISH else AppLanguage.BANGLA
                                            mainViewModel.setLanguage(next)
                                        },
                                        onAddCrop = { nameBn, nameEn, variety, category, season, duration, landArea, landUnit, status, notes ->
                                            mainViewModel.addCrop(nameBn, nameEn, variety, category, season, duration, landArea, landUnit, status, notes)
                                        },
                                        onAddLand = { name, area, unit, location, ownership, crop, soil, notes ->
                                            mainViewModel.addLand(name, area, unit, location, ownership, crop, soil, notes)
                                        },
                                        onAddFertilizer = { name, quantity, unit, price, cropName, notes ->
                                            mainViewModel.addFertilizer(name, quantity, unit, price, cropName, notes)
                                        },
                                        onAddPesticide = { name, quantity, unit, price, cropName, notes ->
                                            mainViewModel.addPesticide(name, quantity, unit, price, cropName, notes)
                                        },
                                        onAddHarvest = { cropName, quantity, unit, notes ->
                                            mainViewModel.addHarvest(cropName, quantity, unit, notes)
                                        },
                                        onAddSale = { cropName, quantity, unit, sellingPrice, buyer, notes ->
                                            mainViewModel.addSale(cropName, quantity, unit, sellingPrice, buyer, notes)
                                        },
                                        onAddExpense = { category, amount, cropName, description ->
                                            mainViewModel.addExpense(category, amount, cropName, description)
                                        },
                                        modifier = contentModifier
                                    )
                                }

                                NavTab.CROPS -> {
                                    CropsScreen(
                                        strings = strings,
                                        currentLanguage = currentLanguage,
                                        crops = crops,
                                        onAddCrop = { nameBn, nameEn, variety, category, season, duration, landArea, landUnit, status, notes ->
                                            mainViewModel.addCrop(nameBn, nameEn, variety, category, season, duration, landArea, landUnit, status, notes)
                                        },
                                        onUpdateCrop = { mainViewModel.updateCrop(it) },
                                        onDeleteCrop = { mainViewModel.deleteCrop(it) },
                                        modifier = contentModifier
                                    )
                                }

                                NavTab.LAND -> {
                                    LandScreen(
                                        strings = strings,
                                        currentLanguage = currentLanguage,
                                        lands = lands,
                                        totalLandShotok = totalLandShotok,
                                        onAddLand = { name, area, unit, location, ownership, crop, soil, notes ->
                                            mainViewModel.addLand(name, area, unit, location, ownership, crop, soil, notes)
                                        },
                                        onDeleteLand = { mainViewModel.deleteLand(it) },
                                        modifier = contentModifier
                                    )
                                }

                                NavTab.ACCOUNTS -> {
                                    AccountsScreen(
                                        strings = strings,
                                        currentLanguage = currentLanguage,
                                        expenses = expenses,
                                        sales = sales,
                                        harvests = harvests,
                                        fertilizers = fertilizers,
                                        pesticides = pesticides,
                                        totalExpenses = totalExpenses,
                                        totalSales = totalSales,
                                        netProfitLoss = netProfitLoss,
                                        onAddExpense = { category, amount, cropName, description ->
                                            mainViewModel.addExpense(category, amount, cropName, description)
                                        },
                                        onDeleteExpense = { mainViewModel.deleteExpense(it) },
                                        onAddSale = { cropName, quantity, unit, sellingPrice, buyer, notes ->
                                            mainViewModel.addSale(cropName, quantity, unit, sellingPrice, buyer, notes)
                                        },
                                        onDeleteSale = { mainViewModel.deleteSale(it) },
                                        onAddHarvest = { cropName, quantity, unit, notes ->
                                            mainViewModel.addHarvest(cropName, quantity, unit, notes)
                                        },
                                        onDeleteHarvest = { mainViewModel.deleteHarvest(it) },
                                        onAddFertilizer = { name, quantity, unit, price, cropName, notes ->
                                            mainViewModel.addFertilizer(name, quantity, unit, price, cropName, notes)
                                        },
                                        onDeleteFertilizer = { mainViewModel.deleteFertilizer(it) },
                                        onAddPesticide = { name, quantity, unit, price, cropName, notes ->
                                            mainViewModel.addPesticide(name, quantity, unit, price, cropName, notes)
                                        },
                                        onDeletePesticide = { mainViewModel.deletePesticide(it) },
                                        modifier = contentModifier
                                    )
                                }

                                NavTab.MORE -> {
                                    MoreScreen(
                                        strings = strings,
                                        currentLanguage = currentLanguage,
                                        isDarkMode = isDarkMode,
                                        onLanguageChange = { mainViewModel.setLanguage(it) },
                                        onDarkModeToggle = { mainViewModel.setDarkMode(it) },
                                        onOpenAboutDeveloper = { mainViewModel.showAboutDeveloper(true) },
                                        onOpenCalculator = { mainViewModel.showCalculator(true) },
                                        modifier = contentModifier
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
