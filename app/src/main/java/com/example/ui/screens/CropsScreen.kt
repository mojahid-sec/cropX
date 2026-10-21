package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CropEntity
import com.example.model.AppLanguage
import com.example.model.CropXStrings
import com.example.ui.components.CircularCropIcon
import com.example.ui.components.CropEditDialog
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.HarvestGold
import com.example.ui.theme.ProfitGreen
import com.example.ui.theme.SproutGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CropsScreen(
    strings: CropXStrings,
    currentLanguage: AppLanguage,
    crops: List<CropEntity>,
    onAddCrop: (nameBn: String, nameEn: String, variety: String, category: String, season: String, duration: Int, landArea: Double, landUnit: String, status: String, notes: String) -> Unit,
    onUpdateCrop: (CropEntity) -> Unit,
    onDeleteCrop: (CropEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("all") }
    var showAddDialog by remember { mutableStateOf(false) }
    var cropToEdit by remember { mutableStateOf<CropEntity?>(null) }
    var selectedCropForDetails by remember { mutableStateOf<CropEntity?>(null) }
    var cropToDelete by remember { mutableStateOf<CropEntity?>(null) }

    val filteredCrops = remember(crops, searchQuery, selectedFilter) {
        crops.filter { crop ->
            val matchesSearch = searchQuery.isBlank() ||
                crop.nameBn.contains(searchQuery, ignoreCase = true) ||
                crop.nameEn.contains(searchQuery, ignoreCase = true) ||
                crop.variety.contains(searchQuery, ignoreCase = true) ||
                crop.category.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedFilter) {
                "active" -> crop.status == "active"
                "planned" -> crop.status == "planned"
                "harvested" -> crop.status == "harvested"
                else -> true
            }

            matchesSearch && matchesFilter
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = ForestGreenPrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("fab_add_crop")
            ) {
                Icon(Icons.Default.Add, contentDescription = strings.addCustomCrop)
            }
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header, Search & Filter Bar (spanning across both grid columns)
            item(span = { GridItemSpan(2) }) {
                Column {
                    Text(
                        text = strings.cropsTitle,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text(strings.searchCrops) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = null)
                                }
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().testTag("crop_search_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Filter Chips Row
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 2.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = selectedFilter == "all",
                                onClick = { selectedFilter = "all" },
                                label = { Text("${strings.filterAll} (${crops.size})") }
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedFilter == "active",
                                onClick = { selectedFilter = "active" },
                                label = { Text(strings.filterActive) }
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedFilter == "planned",
                                onClick = { selectedFilter = "planned" },
                                label = { Text(strings.filterPlanned) }
                            )
                        }
                        item {
                            FilterChip(
                                selected = selectedFilter == "harvested",
                                onClick = { selectedFilter = "harvested" },
                                label = { Text(strings.filterHarvested) }
                            )
                        }
                    }
                }
            }

            // Empty state if no crops match filter
            if (filteredCrops.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Spa,
                                contentDescription = null,
                                modifier = Modifier.size(56.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = strings.noCropsYet,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filteredCrops, key = { it.id }) { crop ->
                    CropCardItem(
                        crop = crop,
                        strings = strings,
                        onClick = { selectedCropForDetails = crop },
                        onEdit = { cropToEdit = crop }
                    )
                }
            }

            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    // Add Crop Dialog
    if (showAddDialog) {
        CropEditDialog(
            strings = strings,
            existingCrop = null,
            onDismiss = { showAddDialog = false },
            onSave = { nameBn, nameEn, variety, category, season, duration, landArea, landUnit, status, notes ->
                onAddCrop(nameBn, nameEn, variety, category, season, duration, landArea, landUnit, status, notes)
                showAddDialog = false
            }
        )
    }

    // Edit Crop Dialog
    cropToEdit?.let { crop ->
        CropEditDialog(
            strings = strings,
            existingCrop = crop,
            onDismiss = { cropToEdit = null },
            onSave = { nameBn, nameEn, variety, category, season, duration, landArea, landUnit, status, notes ->
                onUpdateCrop(
                    crop.copy(
                        nameBn = nameBn,
                        nameEn = nameEn,
                        variety = variety,
                        category = category,
                        season = season,
                        durationDays = duration,
                        landArea = landArea,
                        landAreaUnit = landUnit,
                        status = status,
                        notes = notes
                    )
                )
                cropToEdit = null
            }
        )
    }

    // Crop Details Modal
    selectedCropForDetails?.let { crop ->
        CropDetailsDialog(
            crop = crop,
            strings = strings,
            onDismiss = { selectedCropForDetails = null },
            onEdit = {
                cropToEdit = crop
                selectedCropForDetails = null
            },
            onToggleStatus = { newStatus ->
                onUpdateCrop(crop.copy(status = newStatus))
                selectedCropForDetails = null
            },
            onDelete = {
                cropToDelete = crop
                selectedCropForDetails = null
            }
        )
    }

    // Delete confirmation dialog
    cropToDelete?.let { crop ->
        AlertDialog(
            onDismissRequest = { cropToDelete = null },
            title = { Text(strings.deleteCrop, fontWeight = FontWeight.Bold) },
            text = { Text("আপনি কি নিশ্চিতভাবে \"${crop.nameBn} (${crop.nameEn})\" মুছে ফেলতে চান?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteCrop(crop)
                        cropToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(strings.deleteCrop)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { cropToDelete = null }) {
                    Text(strings.cancel)
                }
            }
        )
    }
}

/**
 * Centered Circular Crop Card:
 * - Perfectly circular icon using 50% border radius and hidden overflow
 * - Equal width & height (88.dp x 88.dp)
 * - Object-fit: cover, object-position: center (ContentScale.Crop, Alignment.Center)
 * - Circular image centered within the card
 * - Crop names clearly visible below the circular icon
 * - Supports both light and dark modes
 */
@Composable
fun CropCardItem(
    crop: CropEntity,
    strings: CropXStrings,
    onClick: () -> Unit,
    onEdit: () -> Unit
) {
    val (statusLabel, statusColor) = when (crop.status) {
        "active" -> strings.statusActive to ProfitGreen
        "planned" -> strings.statusPlanned to HarvestGold
        else -> strings.statusHarvested to MaterialTheme.colorScheme.secondary
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top action bar inside card: Status badge & Edit button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = statusColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = statusLabel,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        maxLines = 1
                    )
                }

                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = strings.edit,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Centered Circular Crop Image:
            // - Equal width & height (84.dp)
            // - Perfectly circular using border-radius: 50% & overflow: hidden
            // - object-fit: cover & object-position: center
            CircularCropIcon(
                resName = crop.drawableResName,
                cropNameBn = crop.nameBn,
                cropNameEn = crop.nameEn,
                size = 84.dp,
                borderWidth = 2.dp,
                elevation = 2.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Crop Names clearly visible below the circular icon
            Text(
                text = crop.nameBn,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = if (crop.variety.isNotBlank()) "${crop.nameEn} • ${crop.variety}" else crop.nameEn,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Season & Land Area badges
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = crop.season,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        maxLines = 1
                    )
                }

                if (crop.landArea > 0) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                    ) {
                        Text(
                            text = "${String.format(Locale.US, "%.0f", crop.landArea)} ${crop.landAreaUnit.take(3)}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            if (crop.marketPriceRange.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = crop.marketPriceRange,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun CropDetailsDialog(
    crop: CropEntity,
    strings: CropXStrings,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onToggleStatus: (String) -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()) }
    val formattedPlantingDate = remember(crop.plantingDate) { dateFormat.format(Date(crop.plantingDate)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(crop.nameBn, fontWeight = FontWeight.Bold)
                    Text(
                        text = if (crop.variety.isNotBlank()) "${crop.nameEn} • ${crop.variety}" else crop.nameEn,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Centered High-res Circular Crop Icon in Details Modal
                CircularCropIcon(
                    resName = crop.drawableResName,
                    cropNameBn = crop.nameBn,
                    cropNameEn = crop.nameEn,
                    size = 110.dp,
                    borderWidth = 3.dp,
                    elevation = 4.dp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${strings.cropCategory}: ${crop.category}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${strings.cropDuration}: ${crop.durationDays} ${strings.days}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (crop.landArea > 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "${strings.cropLandArea}: ${String.format(Locale.US, "%.1f", crop.landArea)} ${crop.landAreaUnit}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "${strings.plantingDate}: $formattedPlantingDate",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "${strings.cropSeason}: ${crop.season}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (crop.marketPriceRange.isNotBlank()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "বাজারমূল্য: ${crop.marketPriceRange}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                if (crop.notes.isNotBlank()) {
                    Text(
                        text = crop.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text("অবস্থা পরিবর্তন করুন:", style = MaterialTheme.typography.labelMedium, modifier = Modifier.fillMaxWidth())
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = crop.status == "active",
                        onClick = { onToggleStatus("active") },
                        label = { Text(strings.statusActive) }
                    )
                    FilterChip(
                        selected = crop.status == "planned",
                        onClick = { onToggleStatus("planned") },
                        label = { Text(strings.statusPlanned) }
                    )
                    FilterChip(
                        selected = crop.status == "harvested",
                        onClick = { onToggleStatus("harvested") },
                        label = { Text(strings.statusHarvested) }
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(strings.edit)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDelete,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(strings.delete)
            }
        }
    )
}
