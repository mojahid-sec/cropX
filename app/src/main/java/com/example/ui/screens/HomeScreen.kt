package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Compost
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PestControl
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.TransactionEntity
import com.example.model.AppLanguage
import com.example.model.CropXStrings
import com.example.ui.NavTab
import com.example.ui.components.AddFertilizerDialog
import com.example.ui.components.AddHarvestDialog
import com.example.ui.components.AddLandDialog
import com.example.ui.components.AddPesticideDialog
import com.example.ui.components.AddSaleDialog
import com.example.ui.components.AddTransactionDialog
import com.example.ui.components.QuickActionButton
import com.example.ui.components.SummaryCard
import com.example.ui.components.TransactionRowItem
import com.example.ui.components.CropEditDialog
import com.example.ui.theme.EarthBrown
import com.example.ui.theme.ExpenseRed
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.HarvestGold
import com.example.ui.theme.ProfitGreen
import com.example.ui.theme.SproutGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    strings: CropXStrings,
    currentLanguage: AppLanguage,
    totalCrops: Int,
    totalLandShotok: Double,
    activeCropCount: Int,
    totalExpenses: Double,
    totalSales: Double,
    totalHarvestQuantity: Double,
    netProfitLoss: Double,
    recentTransactions: List<TransactionEntity>,
    onNavigateToTab: (NavTab) -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToCalculator: () -> Unit,
    onToggleLanguage: () -> Unit,
    onAddCrop: (nameBn: String, nameEn: String, variety: String, category: String, season: String, duration: Int, landArea: Double, landUnit: String, status: String, notes: String) -> Unit,
    onAddLand: (name: String, area: Double, unit: String, location: String, ownership: String, crop: String, soil: String, notes: String) -> Unit,
    onAddFertilizer: (name: String, quantity: Double, unit: String, price: Double, cropName: String, notes: String) -> Unit,
    onAddPesticide: (name: String, quantity: Double, unit: String, price: Double, cropName: String, notes: String) -> Unit,
    onAddHarvest: (cropName: String, quantity: Double, unit: String, notes: String) -> Unit,
    onAddSale: (cropName: String, quantity: Double, unit: String, sellingPrice: Double, buyer: String, notes: String) -> Unit,
    onAddExpense: (category: String, amount: Double, cropName: String, description: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCropDialog by remember { mutableStateOf(false) }
    var showLandDialog by remember { mutableStateOf(false) }
    var showFertilizerDialog by remember { mutableStateOf(false) }
    var showPesticideDialog by remember { mutableStateOf(false) }
    var showHarvestDialog by remember { mutableStateOf(false) }
    var showSaleDialog by remember { mutableStateOf(false) }
    var showExpenseDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top App Bar Greeting & Founder Avatar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "CropX",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = if (currentLanguage == AppLanguage.BANGLA) "কৃষি" else "Agri",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = strings.appTagline,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Calculator quick action button
                    IconButton(
                        onClick = onNavigateToCalculator,
                        modifier = Modifier.testTag("home_calc_button")
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Calculate,
                                    contentDescription = strings.calculatorTitle,
                                    tint = ForestGreenPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(2.dp))

                    // Language toggle button
                    IconButton(
                        onClick = onToggleLanguage,
                        modifier = Modifier.testTag("home_lang_toggle")
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (currentLanguage == AppLanguage.BANGLA) "বাং" else "EN",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Founder Avatar badge
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .clickable { onNavigateToAbout() }
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.developer_photo),
                            contentDescription = "MD. MOJAHIDUR RAHMAN SAJID",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

        // Hero Agricultural Banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.farm_hero_banner),
                        contentDescription = "Farm Panorama",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.75f)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = if (currentLanguage == AppLanguage.BANGLA) "আজকের কৃষি ড্যাশবোর্ড" else "Today's Farm Overview",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = if (currentLanguage == AppLanguage.BANGLA)
                                "সুশৃঙ্খল হিসাব, কর্তন ও সার ব্যবস্থাপনায় আধুনিক খামার পরিচালনা"
                            else
                                "Maximize farm productivity with real-time operations & financial tracking",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        )
                    }
                }
            }
        }

        // Real Summary Cards Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Row 1: Total Land & Total Crops
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SummaryCard(
                        title = strings.totalLand,
                        value = if (totalLandShotok > 0) {
                            val bigha = totalLandShotok / 33.0
                            if (currentLanguage == AppLanguage.BANGLA) {
                                String.format(Locale.US, "%.1f শতক (%.2f বিঘা)", totalLandShotok, bigha)
                            } else {
                                String.format(Locale.US, "%.1f Shotok (%.2f Bigha)", totalLandShotok, bigha)
                            }
                        } else {
                            if (currentLanguage == AppLanguage.BANGLA) "০ শতক" else "0 Shotok"
                        },
                        icon = Icons.Default.Landscape,
                        iconColor = ForestGreenPrimary,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.weight(1f).clickable { onNavigateToTab(NavTab.LAND) },
                        testTag = "summary_total_land"
                    )

                    SummaryCard(
                        title = strings.totalCrops,
                        value = "$totalCrops (চলতি: $activeCropCount)",
                        icon = Icons.Default.Spa,
                        iconColor = SproutGreen,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.weight(1f).clickable { onNavigateToTab(NavTab.CROPS) },
                        testTag = "summary_total_crops"
                    )
                }

                // Row 2: Total Expenses & Total Sales
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SummaryCard(
                        title = strings.totalExpenses,
                        value = String.format(Locale.US, "${strings.currencySymbol}%.0f", totalExpenses),
                        icon = Icons.Default.ArrowDownward,
                        iconColor = ExpenseRed,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.weight(1f).clickable { onNavigateToTab(NavTab.ACCOUNTS) },
                        testTag = "summary_total_expenses"
                    )

                    SummaryCard(
                        title = strings.totalSales,
                        value = String.format(Locale.US, "${strings.currencySymbol}%.0f", totalSales),
                        icon = Icons.Default.ArrowUpward,
                        iconColor = ProfitGreen,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.weight(1f).clickable { onNavigateToTab(NavTab.ACCOUNTS) },
                        testTag = "summary_total_sales"
                    )
                }

                // Row 3: Total Harvest Quantity
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SummaryCard(
                        title = strings.totalHarvest,
                        value = if (totalHarvestQuantity > 0) {
                            String.format(Locale.US, "%.1f একক", totalHarvestQuantity)
                        } else {
                            "০ কর্তন"
                        },
                        icon = Icons.Default.Warehouse,
                        iconColor = EarthBrown,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth().clickable { onNavigateToTab(NavTab.ACCOUNTS) },
                        testTag = "summary_total_harvest"
                    )
                }
            }
        }

        // Net Profit / Loss Banner Card
        item {
            ElevatedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = if (netProfitLoss >= 0) {
                        ProfitGreen.copy(alpha = 0.1f)
                    } else {
                        ExpenseRed.copy(alpha = 0.1f)
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToTab(NavTab.ACCOUNTS) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(
                                    if (netProfitLoss >= 0) ProfitGreen.copy(alpha = 0.2f)
                                    else ExpenseRed.copy(alpha = 0.2f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (netProfitLoss >= 0) Icons.Default.MonetizationOn else Icons.Default.Payments,
                                contentDescription = null,
                                tint = if (netProfitLoss >= 0) ProfitGreen else ExpenseRed
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (netProfitLoss >= 0) strings.netProfit else strings.netLoss,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (netProfitLoss >= 0) ProfitGreen else ExpenseRed
                            )
                            Text(
                                text = if (totalExpenses == 0.0 && totalSales == 0.0)
                                    strings.noTransactionsYet
                                else
                                    strings.profitAndLoss,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Text(
                        text = String.format(Locale.US, "${strings.currencySymbol}%.0f", kotlin.math.abs(netProfitLoss)),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = if (netProfitLoss >= 0) ProfitGreen else ExpenseRed
                        )
                    )
                }
            }
        }

        // Quick Actions Grid (All 8 Farm Operations)
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = strings.quickActions,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                // Row 1: Add Crop & Add Land
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionButton(
                        title = strings.addCropAction,
                        icon = Icons.Default.Spa,
                        containerColor = SproutGreen,
                        contentColor = Color.White,
                        modifier = Modifier.weight(1f).testTag("quick_action_add_crop"),
                        onClick = { showCropDialog = true }
                    )
                    QuickActionButton(
                        title = strings.addLandAction,
                        icon = Icons.Default.Landscape,
                        containerColor = ForestGreenPrimary,
                        contentColor = Color.White,
                        modifier = Modifier.weight(1f).testTag("quick_action_add_land"),
                        onClick = { showLandDialog = true }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Row 2: Add Expense & Add Sale
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionButton(
                        title = strings.addExpenseAction,
                        icon = Icons.Default.ArrowDownward,
                        containerColor = ExpenseRed.copy(alpha = 0.9f),
                        contentColor = Color.White,
                        modifier = Modifier.weight(1f).testTag("quick_action_add_expense"),
                        onClick = { showExpenseDialog = true }
                    )
                    QuickActionButton(
                        title = strings.addSaleAction,
                        icon = Icons.Default.ArrowUpward,
                        containerColor = ProfitGreen,
                        contentColor = Color.White,
                        modifier = Modifier.weight(1f).testTag("quick_action_add_sale"),
                        onClick = { showSaleDialog = true }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Row 3: Fertilizer & Pesticide
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionButton(
                        title = strings.addFertilizerAction,
                        icon = Icons.Default.Compost,
                        containerColor = HarvestGold,
                        contentColor = Color.Black,
                        modifier = Modifier.weight(1f).testTag("quick_action_add_fertilizer"),
                        onClick = { showFertilizerDialog = true }
                    )
                    QuickActionButton(
                        title = strings.addPesticideAction,
                        icon = Icons.Default.PestControl,
                        containerColor = EarthBrown,
                        contentColor = Color.White,
                        modifier = Modifier.weight(1f).testTag("quick_action_add_pesticide"),
                        onClick = { showPesticideDialog = true }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Row 4: Harvest & Agri Calculator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionButton(
                        title = strings.addHarvestAction,
                        icon = Icons.Default.Warehouse,
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White,
                        modifier = Modifier.weight(1f).testTag("quick_action_add_harvest"),
                        onClick = { showHarvestDialog = true }
                    )
                    QuickActionButton(
                        title = strings.openCalculatorAction,
                        icon = Icons.Default.Calculate,
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.weight(1f).testTag("quick_action_open_calc"),
                        onClick = onNavigateToCalculator
                    )
                }
            }
        }

        // Recent Transactions Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strings.recentTransactions,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (recentTransactions.isNotEmpty()) {
                    Text(
                        text = strings.viewAll,
                        style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .clickable { onNavigateToTab(NavTab.ACCOUNTS) }
                            .padding(4.dp)
                    )
                }
            }
        }

        // Recent Transactions List or Empty State
        if (recentTransactions.isEmpty()) {
            item {
                OutlinedCard(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = strings.noTransactionsYet,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = strings.noTransactionsDesc,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { showExpenseDialog = true },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.addExpenseAction)
                        }
                    }
                }
            }
        } else {
            items(recentTransactions) { item ->
                TransactionRowItem(item = item, strings = strings)
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }

    // Dialogs
    if (showCropDialog) {
        CropEditDialog(
            strings = strings,
            existingCrop = null,
            onDismiss = { showCropDialog = false },
            onSave = { nameBn, nameEn, variety, category, season, duration, landArea, landUnit, status, notes ->
                onAddCrop(nameBn, nameEn, variety, category, season, duration, landArea, landUnit, status, notes)
                showCropDialog = false
            }
        )
    }

    if (showLandDialog) {
        AddLandDialog(
            strings = strings,
            onDismiss = { showLandDialog = false },
            onConfirm = { name, area, unit, location, ownership, crop, soil, notes ->
                onAddLand(name, area, unit, location, ownership, crop, soil, notes)
                showLandDialog = false
            }
        )
    }

    if (showExpenseDialog) {
        AddTransactionDialog(
            strings = strings,
            initialType = "expense",
            onDismiss = { showExpenseDialog = false },
            onConfirm = { _, category, amount, crop, _, notes ->
                onAddExpense(category, amount, crop, notes)
                showExpenseDialog = false
            }
        )
    }

    if (showSaleDialog) {
        AddSaleDialog(
            strings = strings,
            onDismiss = { showSaleDialog = false },
            onConfirm = { cropName, quantity, unit, sellingPrice, buyer, notes ->
                onAddSale(cropName, quantity, unit, sellingPrice, buyer, notes)
                showSaleDialog = false
            }
        )
    }

    if (showFertilizerDialog) {
        AddFertilizerDialog(
            strings = strings,
            onDismiss = { showFertilizerDialog = false },
            onConfirm = { name, quantity, unit, price, cropName, notes ->
                onAddFertilizer(name, quantity, unit, price, cropName, notes)
                showFertilizerDialog = false
            }
        )
    }

    if (showPesticideDialog) {
        AddPesticideDialog(
            strings = strings,
            onDismiss = { showPesticideDialog = false },
            onConfirm = { name, quantity, unit, price, cropName, notes ->
                onAddPesticide(name, quantity, unit, price, cropName, notes)
                showPesticideDialog = false
            }
        )
    }

    if (showHarvestDialog) {
        AddHarvestDialog(
            strings = strings,
            onDismiss = { showHarvestDialog = false },
            onConfirm = { cropName, quantity, unit, notes ->
                onAddHarvest(cropName, quantity, unit, notes)
                showHarvestDialog = false
            }
        )
    }
}
