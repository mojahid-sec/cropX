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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import com.example.data.LandEntity
import com.example.model.AppLanguage
import com.example.model.CropXStrings
import com.example.ui.components.AddLandDialog
import com.example.ui.components.CircularCropIcon
import com.example.ui.theme.EarthBrown
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.HarvestGold
import com.example.ui.theme.ProfitGreen
import com.example.ui.theme.SproutGreen
import java.util.Locale

@Composable
fun LandScreen(
    strings: CropXStrings,
    currentLanguage: AppLanguage,
    lands: List<LandEntity>,
    totalLandShotok: Double,
    onAddLand: (name: String, area: Double, unit: String, location: String, ownership: String, crop: String, soil: String, notes: String) -> Unit,
    onDeleteLand: (LandEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var landToDelete by remember { mutableStateOf<LandEntity?>(null) }
    var showConverter by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = ForestGreenPrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("fab_add_land")
            ) {
                Icon(Icons.Default.Add, contentDescription = strings.addLandPlot)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = strings.landTitle,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Surface(
                        onClick = { showConverter = !showConverter },
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Calculate, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = strings.converterTitle,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }

            // Total Land Aggregate Banner
            item {
                val bigha = totalLandShotok / 33.0
                val acre = totalLandShotok / 100.0
                val hectare = totalLandShotok / 247.1

                ElevatedCard(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(ForestGreenPrimary.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Landscape, contentDescription = null, tint = ForestGreenPrimary)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = strings.totalLandCalculated,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = if (currentLanguage == AppLanguage.BANGLA)
                                        String.format(Locale.US, "%.1f শতক", totalLandShotok)
                                    else
                                        String.format(Locale.US, "%.1f Shotok", totalLandShotok),
                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Equivalents
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "≈ ${String.format(Locale.US, "%.2f", bigha)} ${strings.unitBigha.substringBefore(" ")}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "≈ ${String.format(Locale.US, "%.2f", acre)} ${strings.unitAcre.substringBefore(" ")}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "≈ ${String.format(Locale.US, "%.3f", hectare)} ${strings.unitHectare.substringBefore(" ")}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            // Converter Helper (Collapsible)
            if (showConverter) {
                item {
                    OutlinedCard(
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "📐 বাংলাদেশের কৃষি জমির পরিমাপ সূত্র:",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text("• ১ বিঘা = ৩৩ শতক / ডেসিমাল", style = MaterialTheme.typography.bodySmall)
                            Text("• ১ একর = ১০০ শতক = ৩.০৩ বিঘা", style = MaterialTheme.typography.bodySmall)
                            Text("• ১ হেক্টর = ২৪৭.১ শতক = ৭.৪৮ বিঘা = ২.৪৭ একর", style = MaterialTheme.typography.bodySmall)
                            Text("• ১ শতক = ৪৩৫.৬ বর্গফুট", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            // Land Plots List or Empty State
            if (lands.isEmpty()) {
                item {
                    OutlinedCard(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Terrain,
                                contentDescription = null,
                                modifier = Modifier.size(52.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = strings.noLandPlots,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = strings.noLandPlotsDesc,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = { showAddDialog = true },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(strings.addLandPlot)
                            }
                        }
                    }
                }
            } else {
                items(lands, key = { it.id }) { land ->
                    LandPlotCard(
                        land = land,
                        strings = strings,
                        onDeleteClick = { landToDelete = land }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    if (showAddDialog) {
        AddLandDialog(
            strings = strings,
            onDismiss = { showAddDialog = false },
            onConfirm = { name, area, unit, location, ownership, crop, soil, notes ->
                onAddLand(name, area, unit, location, ownership, crop, soil, notes)
                showAddDialog = false
            }
        )
    }

    landToDelete?.let { land ->
        AlertDialog(
            onDismissRequest = { landToDelete = null },
            title = { Text(strings.deleteLand, fontWeight = FontWeight.Bold) },
            text = { Text("আপনি কি নিশ্চিতভাবে \"${land.name}\" মুছে ফেলতে চান?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteLand(land)
                        landToDelete = null
                    }
                ) {
                    Text(strings.deleteLand)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { landToDelete = null }) {
                    Text(strings.cancel)
                }
            }
        )
    }
}

@Composable
fun LandPlotCard(
    land: LandEntity,
    strings: CropXStrings,
    onDeleteClick: () -> Unit
) {
    val ownershipLabel = when (land.ownershipType) {
        "own" -> strings.ownershipOwn
        "leased" -> strings.ownershipLeased
        "sharecrop" -> strings.ownershipSharecrop
        else -> strings.ownershipOwn
    }

    val unitLabel = when (land.unit.lowercase()) {
        "bigha" -> strings.unitBigha.substringBefore(" ")
        "acre" -> strings.unitAcre.substringBefore(" ")
        "hectare" -> strings.unitHectare.substringBefore(" ")
        else -> strings.unitShotok.substringBefore(" ")
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = land.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (land.location.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = land.location,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = when (land.ownershipType) {
                            "own" -> ProfitGreen.copy(alpha = 0.15f)
                            "leased" -> HarvestGold.copy(alpha = 0.15f)
                            else -> EarthBrown.copy(alpha = 0.15f)
                        }
                    ) {
                        Text(
                            text = ownershipLabel,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = when (land.ownershipType) {
                                "own" -> ProfitGreen
                                "leased" -> HarvestGold
                                else -> EarthBrown
                            },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    IconButton(onClick = onDeleteClick, modifier = Modifier.size(32.dp)) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = strings.deleteLand,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${String.format(Locale.US, "%.1f", land.area)} $unitLabel (≈ ${String.format(Locale.US, "%.1f", land.areaInShotok())} শতক)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                if (land.associatedCropName.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularCropIcon(
                                resName = "",
                                cropNameBn = land.associatedCropName,
                                cropNameEn = land.associatedCropName,
                                size = 22.dp,
                                borderWidth = 1.dp,
                                elevation = 0.dp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = land.associatedCropName,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }
                }
            }

            if (land.soilType.isNotBlank() || land.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (land.soilType.isNotBlank()) {
                        Text(
                            text = "মাটি: ${land.soilType}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (land.notes.isNotBlank()) {
                        Text(
                            text = " • ${land.notes}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
