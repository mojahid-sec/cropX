package com.example.ui.screens

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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Compost
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PestControl
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ExpenseEntity
import com.example.data.FertilizerEntity
import com.example.data.HarvestEntity
import com.example.data.PesticideEntity
import com.example.data.SaleEntity
import com.example.model.AppLanguage
import com.example.model.CropXStrings
import com.example.ui.components.AddFertilizerDialog
import com.example.ui.components.CircularCropIcon
import com.example.ui.components.SummaryCard
import com.example.ui.components.AddHarvestDialog
import com.example.ui.components.AddPesticideDialog
import com.example.ui.components.AddSaleDialog
import com.example.ui.components.AddTransactionDialog
import com.example.ui.theme.EarthBrown
import com.example.ui.theme.ExpenseRed
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.HarvestGold
import com.example.ui.theme.ProfitGreen
import com.example.ui.theme.SproutGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AccountsScreen(
    strings: CropXStrings,
    currentLanguage: AppLanguage,
    expenses: List<ExpenseEntity>,
    sales: List<SaleEntity>,
    harvests: List<HarvestEntity>,
    fertilizers: List<FertilizerEntity>,
    pesticides: List<PesticideEntity>,
    totalExpenses: Double,
    totalSales: Double,
    netProfitLoss: Double,
    onAddExpense: (category: String, amount: Double, cropName: String, description: String) -> Unit,
    onDeleteExpense: (ExpenseEntity) -> Unit,
    onAddSale: (cropName: String, quantity: Double, unit: String, sellingPrice: Double, buyer: String, notes: String) -> Unit,
    onDeleteSale: (SaleEntity) -> Unit,
    onAddHarvest: (cropName: String, quantity: Double, unit: String, notes: String) -> Unit,
    onDeleteHarvest: (HarvestEntity) -> Unit,
    onAddFertilizer: (name: String, quantity: Double, unit: String, price: Double, cropName: String, notes: String) -> Unit,
    onDeleteFertilizer: (FertilizerEntity) -> Unit,
    onAddPesticide: (name: String, quantity: Double, unit: String, price: Double, cropName: String, notes: String) -> Unit,
    onDeletePesticide: (PesticideEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Expenses, 1: Sales, 2: Harvest, 3: Fertilizer, 4: Pesticide, 5: P&L
    var showExpenseDialog by remember { mutableStateOf(false) }
    var showSaleDialog by remember { mutableStateOf(false) }
    var showHarvestDialog by remember { mutableStateOf(false) }
    var showFertilizerDialog by remember { mutableStateOf(false) }
    var showPesticideDialog by remember { mutableStateOf(false) }

    val tabTitles = listOf(
        strings.tabExpenses,
        strings.tabSales,
        strings.tabHarvest,
        strings.tabFertilizer,
        strings.tabPesticide,
        strings.tabSummary
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            if (selectedTab < 5) {
                FloatingActionButton(
                    onClick = {
                        when (selectedTab) {
                            0 -> showExpenseDialog = true
                            1 -> showSaleDialog = true
                            2 -> showHarvestDialog = true
                            3 -> showFertilizerDialog = true
                            4 -> showPesticideDialog = true
                        }
                    },
                    containerColor = ForestGreenPrimary,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.testTag("accounts_fab_add")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Entry")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Screen Header & P&L Quick Ticker
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = strings.accountsTitle,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (netProfitLoss >= 0) ProfitGreen.copy(alpha = 0.15f) else ExpenseRed.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "${if (netProfitLoss >= 0) "+" else "-"}${strings.currencySymbol}${String.format(Locale.US, "%.0f", kotlin.math.abs(netProfitLoss))}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = if (netProfitLoss >= 0) ProfitGreen else ExpenseRed
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Scrollable Operational Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // Tab Content
            when (selectedTab) {
                0 -> ExpensesTabContent(
                    strings = strings,
                    expenses = expenses,
                    totalExpenses = totalExpenses,
                    onDelete = onDeleteExpense,
                    onOpenAdd = { showExpenseDialog = true }
                )
                1 -> SalesTabContent(
                    strings = strings,
                    sales = sales,
                    totalSales = totalSales,
                    onDelete = onDeleteSale,
                    onOpenAdd = { showSaleDialog = true }
                )
                2 -> HarvestTabContent(
                    strings = strings,
                    harvests = harvests,
                    onDelete = onDeleteHarvest,
                    onOpenAdd = { showHarvestDialog = true }
                )
                3 -> FertilizerTabContent(
                    strings = strings,
                    fertilizers = fertilizers,
                    onDelete = onDeleteFertilizer,
                    onOpenAdd = { showFertilizerDialog = true }
                )
                4 -> PesticideTabContent(
                    strings = strings,
                    pesticides = pesticides,
                    onDelete = onDeletePesticide,
                    onOpenAdd = { showPesticideDialog = true }
                )
                5 -> ProfitAndLossTabContent(
                    strings = strings,
                    totalSales = totalSales,
                    totalExpenses = totalExpenses,
                    netProfitLoss = netProfitLoss,
                    salesCount = sales.size,
                    expensesCount = expenses.size
                )
            }
        }
    }

    // Interactive Dialogs
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
}

// 1. Expenses Tab Content
@Composable
fun ExpensesTabContent(
    strings: CropXStrings,
    expenses: List<ExpenseEntity>,
    totalExpenses: Double,
    onDelete: (ExpenseEntity) -> Unit,
    onOpenAdd: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SummaryCard(
                title = strings.totalExpenses,
                value = "${strings.currencySymbol}${String.format(Locale.US, "%,.0f", totalExpenses)}",
                icon = Icons.Default.ArrowDownward,
                iconColor = ExpenseRed,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (expenses.isEmpty()) {
            item {
                EmptyStateCard(
                    title = "কোনো খরচের রেকর্ড নেই",
                    desc = "সার, বীজ, কীটনাশক বা মজুরির খরচ যুক্ত করুন।",
                    buttonLabel = strings.addExpense,
                    onAction = onOpenAdd
                )
            }
        } else {
            items(expenses, key = { it.id }) { item ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.category,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = dateFormat.format(Date(item.date)),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (item.cropName.isNotBlank()) {
                                Text(
                                    text = "ফসল: ${item.cropName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            if (item.description.isNotBlank()) {
                                Text(
                                    text = item.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "-${strings.currencySymbol}${String.format(Locale.US, "%.0f", item.amount)}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ExpenseRed
                                )
                            )
                            IconButton(onClick = { onDelete(item) }) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(70.dp)) }
    }
}

// 2. Sales Tab Content
@Composable
fun SalesTabContent(
    strings: CropXStrings,
    sales: List<SaleEntity>,
    totalSales: Double,
    onDelete: (SaleEntity) -> Unit,
    onOpenAdd: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SummaryCard(
                title = strings.totalSales,
                value = "${strings.currencySymbol}${String.format(Locale.US, "%,.0f", totalSales)}",
                icon = Icons.Default.ArrowUpward,
                iconColor = ProfitGreen,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (sales.isEmpty()) {
            item {
                EmptyStateCard(
                    title = strings.noSalesYet,
                    desc = "উৎপাদিত ফসল বিক্রয়ের হিসাব এখানে যুক্ত করুন।",
                    buttonLabel = strings.addSale,
                    onAction = onOpenAdd
                )
            }
        } else {
            items(sales, key = { it.id }) { item ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CircularCropIcon(
                            resName = "",
                            cropNameBn = item.cropName,
                            cropNameEn = item.cropName,
                            size = 46.dp,
                            borderWidth = 1.5.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.cropName,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${item.quantity} ${item.unit} @ ${strings.currencySymbol}${item.sellingPrice} | ${dateFormat.format(Date(item.date))}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (item.buyer.isNotBlank()) {
                                Text(
                                    text = "ক্রেতা: ${item.buyer}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            if (item.notes.isNotBlank()) {
                                Text(
                                    text = item.notes,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "+${strings.currencySymbol}${String.format(Locale.US, "%.0f", item.totalAmount)}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ProfitGreen
                                )
                            )
                            IconButton(onClick = { onDelete(item) }) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(70.dp)) }
    }
}

// 3. Harvest Tab Content
@Composable
fun HarvestTabContent(
    strings: CropXStrings,
    harvests: List<HarvestEntity>,
    onDelete: (HarvestEntity) -> Unit,
    onOpenAdd: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()) }
    val totalQty = harvests.sumOf { it.quantity }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SummaryCard(
                title = strings.totalHarvest,
                value = if (totalQty > 0) String.format(Locale.US, "%,.1f মোট উত্তোলিত", totalQty) else "০ কর্তন",
                icon = Icons.Default.Warehouse,
                iconColor = EarthBrown,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (harvests.isEmpty()) {
            item {
                EmptyStateCard(
                    title = strings.noHarvestsYet,
                    desc = "মাঠ থেকে ফসল উত্তোলনের সময় এখানে রেকর্ড যুক্ত করুন।",
                    buttonLabel = strings.addHarvest,
                    onAction = onOpenAdd
                )
            }
        } else {
            items(harvests, key = { it.id }) { item ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CircularCropIcon(
                            resName = "",
                            cropNameBn = item.cropName,
                            cropNameEn = item.cropName,
                            size = 46.dp,
                            borderWidth = 1.5.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.cropName,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${strings.harvestDate}: ${dateFormat.format(Date(item.date))}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (item.notes.isNotBlank()) {
                                Text(
                                    text = item.notes,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = "${item.quantity} ${item.unit}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }

                            IconButton(onClick = { onDelete(item) }) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(70.dp)) }
    }
}

// 4. Fertilizer Tab Content
@Composable
fun FertilizerTabContent(
    strings: CropXStrings,
    fertilizers: List<FertilizerEntity>,
    onDelete: (FertilizerEntity) -> Unit,
    onOpenAdd: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()) }
    val totalCost = fertilizers.sumOf { it.price }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SummaryCard(
                title = "মোট সার প্রয়োগ খরচ",
                value = "${strings.currencySymbol}${String.format(Locale.US, "%,.0f", totalCost)}",
                icon = Icons.Default.Compost,
                iconColor = HarvestGold,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (fertilizers.isEmpty()) {
            item {
                EmptyStateCard(
                    title = strings.noFertilizersYet,
                    desc = "ইউরিয়া, টিএসপি, ডিএপি বা জৈব সার প্রয়োগের তথ্য লিখুন।",
                    buttonLabel = strings.addFertilizer,
                    onAction = onOpenAdd
                )
            }
        } else {
            items(fertilizers, key = { it.id }) { item ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "পরিমাণ: ${item.quantity} ${item.unit} | তারিখ: ${dateFormat.format(Date(item.applicationDate))}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (item.cropName.isNotBlank()) {
                                Text(
                                    text = "ফসল: ${item.cropName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            if (item.notes.isNotBlank()) {
                                Text(
                                    text = item.notes,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (item.price > 0) {
                                Text(
                                    text = "${strings.currencySymbol}${String.format(Locale.US, "%.0f", item.price)}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(onClick = { onDelete(item) }) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(70.dp)) }
    }
}

// 5. Pesticide Tab Content
@Composable
fun PesticideTabContent(
    strings: CropXStrings,
    pesticides: List<PesticideEntity>,
    onDelete: (PesticideEntity) -> Unit,
    onOpenAdd: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()) }
    val totalCost = pesticides.sumOf { it.price }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SummaryCard(
                title = "মোট বালাইনাশক খরচ",
                value = "${strings.currencySymbol}${String.format(Locale.US, "%,.0f", totalCost)}",
                icon = Icons.Default.PestControl,
                iconColor = EarthBrown,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (pesticides.isEmpty()) {
            item {
                EmptyStateCard(
                    title = strings.noPesticidesYet,
                    desc = "কীটনাশক, ছত্রাকনাশক বা বালাইনাশক ব্যবহারের তথ্য সংরক্ষণ করুন।",
                    buttonLabel = strings.addPesticide,
                    onAction = onOpenAdd
                )
            }
        } else {
            items(pesticides, key = { it.id }) { item ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "পরিমাণ: ${item.quantity} ${item.unit} | তারিখ: ${dateFormat.format(Date(item.applicationDate))}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (item.cropName.isNotBlank()) {
                                Text(
                                    text = "ফসল: ${item.cropName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            if (item.notes.isNotBlank()) {
                                Text(
                                    text = item.notes,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (item.price > 0) {
                                Text(
                                    text = "${strings.currencySymbol}${String.format(Locale.US, "%.0f", item.price)}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(onClick = { onDelete(item) }) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
        item { Spacer(modifier = Modifier.height(70.dp)) }
    }
}

// 6. Profit and Loss Tab Content (No Duplicate Expense Counting, Harvest separate from Sales)
@Composable
fun ProfitAndLossTabContent(
    strings: CropXStrings,
    totalSales: Double,
    totalExpenses: Double,
    netProfitLoss: Double,
    salesCount: Int,
    expensesCount: Int
) {
    val isProfit = netProfitLoss >= 0
    val margin = if (totalSales > 0) (netProfitLoss / totalSales) * 100.0 else 0.0

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = if (isProfit) ProfitGreen.copy(alpha = 0.12f) else ExpenseRed.copy(alpha = 0.12f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = strings.profitAndLoss,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${if (isProfit) "+" else "-"}${strings.currencySymbol}${String.format(Locale.US, "%,.2f", kotlin.math.abs(netProfitLoss))}",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isProfit) ProfitGreen else ExpenseRed
                        )
                    )
                    Text(
                        text = if (isProfit) "লাভের মার্জিন: ${String.format(Locale.US, "%.1f", margin)}%" else "সামগ্রিক খামার ঘাটতি",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = if (isProfit) ProfitGreen else ExpenseRed
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "আর্থিক সারাংশ বিবরণী",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("মোট বিক্রয় আয় ($salesCount টি বিক্রয়)", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "+${strings.currencySymbol}${String.format(Locale.US, "%,.2f", totalSales)}",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = ProfitGreen)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("মোট কৃষি খরচ ($expensesCount টি রেকর্ড)", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "-${strings.currencySymbol}${String.format(Locale.US, "%,.2f", totalExpenses)}",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = ExpenseRed)
                        )
                    }

                    androidx.compose.material3.HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (isProfit) "চূড়ান্ত নিট লাভ" else "চূড়ান্ত নিট ক্ষতি",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${if (isProfit) "+" else "-"}${strings.currencySymbol}${String.format(Locale.US, "%,.2f", kotlin.math.abs(netProfitLoss))}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isProfit) ProfitGreen else ExpenseRed
                            )
                        )
                    }
                }
            }
        }

        item {
            OutlinedCard(
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "🛡️ নির্ভুল আর্থিক হিসাবের নিশ্চয়তা:",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text("• বিক্রয় আয়ের সাথে কর্তন (Harvest) রেকর্ড সম্পূর্ণরূপে পৃথক রাখা হয়।", style = MaterialTheme.typography.bodySmall)
                    Text("• সার ও কীটনাশক ব্যবহারের খরচ স্বয়ংক্রিয়ভাবে একীভূত থাকে এবং দ্বৈত গণনা রোধ করা হয়।", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        item { Spacer(modifier = Modifier.height(70.dp)) }
    }
}

@Composable
fun EmptyStateCard(
    title: String,
    desc: String,
    buttonLabel: String,
    onAction: () -> Unit
) {
    OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ReceiptLong,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = onAction,
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(buttonLabel)
            }
        }
    }
}
