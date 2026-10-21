package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.CropEntity
import com.example.model.CropXStrings
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropEditDialog(
    strings: CropXStrings,
    existingCrop: CropEntity? = null,
    onDismiss: () -> Unit,
    onSave: (nameBn: String, nameEn: String, variety: String, category: String, season: String, duration: Int, landArea: Double, landUnit: String, status: String, notes: String) -> Unit
) {
    var nameBn by remember { mutableStateOf(existingCrop?.nameBn ?: "") }
    var nameEn by remember { mutableStateOf(existingCrop?.nameEn ?: "") }
    var variety by remember { mutableStateOf(existingCrop?.variety ?: "") }
    var category by remember { mutableStateOf(existingCrop?.category ?: "সবজি / Vegetable") }
    var season by remember { mutableStateOf(existingCrop?.season ?: "রবি / Rabi") }
    var durationText by remember { mutableStateOf(existingCrop?.durationDays?.toString() ?: "90") }
    var landAreaText by remember { mutableStateOf(existingCrop?.landArea?.takeIf { it > 0 }?.toString() ?: "33") }
    var landUnit by remember { mutableStateOf(existingCrop?.landAreaUnit ?: "shotok") }
    var status by remember { mutableStateOf(existingCrop?.status ?: "active") }
    var notes by remember { mutableStateOf(existingCrop?.notes ?: "") }

    var nameError by remember { mutableStateOf(false) }
    var durationError by remember { mutableStateOf(false) }
    var landAreaError by remember { mutableStateOf(false) }

    val categories = listOf("সবজি / Vegetable", "দানাশস্য / Grain", "অর্থকরী / Cash Crop", "মসলা / Spices", "ফল / Fruit")
    var categoryExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (existingCrop == null) strings.addCustomCrop else strings.editCrop,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Centered live Circular Crop Icon preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularCropIcon(
                        resName = existingCrop?.drawableResName ?: "",
                        cropNameBn = nameBn,
                        cropNameEn = nameEn,
                        size = 80.dp,
                        borderWidth = 2.dp,
                        elevation = 3.dp
                    )
                }

                OutlinedTextField(
                    value = nameBn,
                    onValueChange = {
                        nameBn = it
                        if (it.isNotBlank()) nameError = false
                    },
                    label = { Text(strings.cropNameBn) },
                    placeholder = { Text("যেমন: বেগুন / ধান / আলু") },
                    isError = nameError,
                    supportingText = if (nameError) { { Text(strings.validationNameRequired, color = MaterialTheme.colorScheme.error) } } else null,
                    modifier = Modifier.fillMaxWidth().testTag("crop_name_bn_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = nameEn,
                    onValueChange = { nameEn = it },
                    label = { Text(strings.cropNameEn) },
                    placeholder = { Text("e.g. Eggplant / Rice / Potato") },
                    modifier = Modifier.fillMaxWidth().testTag("crop_name_en_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = variety,
                    onValueChange = { variety = it },
                    label = { Text(strings.cropVariety) },
                    placeholder = { Text("যেমন: ব্রি ধান-২৮, ইসলামপুরী, ডায়মন্ড") },
                    modifier = Modifier.fillMaxWidth().testTag("crop_variety_input"),
                    singleLine = true
                )

                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(strings.cropCategory) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = landAreaText,
                        onValueChange = {
                            landAreaText = it
                            landAreaError = false
                        },
                        label = { Text(strings.cropLandArea) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = landAreaError,
                        modifier = Modifier.weight(1.2f).testTag("crop_land_area_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = durationText,
                        onValueChange = {
                            durationText = it
                            durationError = false
                        },
                        label = { Text("${strings.cropDuration} (${strings.days})") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = durationError,
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = landUnit == "shotok",
                        onClick = { landUnit = "shotok" },
                        label = { Text(strings.unitShotok.substringBefore(" ")) }
                    )
                    FilterChip(
                        selected = landUnit == "bigha",
                        onClick = { landUnit = "bigha" },
                        label = { Text(strings.unitBigha.substringBefore(" ")) }
                    )
                    FilterChip(
                        selected = landUnit == "acre",
                        onClick = { landUnit = "acre" },
                        label = { Text(strings.unitAcre.substringBefore(" ")) }
                    )
                }

                OutlinedTextField(
                    value = season,
                    onValueChange = { season = it },
                    label = { Text(strings.cropSeason) },
                    placeholder = { Text("রবি / খরিফ / বারোমাসি") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text(strings.cropStatus, style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = status == "active",
                        onClick = { status = "active" },
                        label = { Text(strings.statusActive) }
                    )
                    FilterChip(
                        selected = status == "planned",
                        onClick = { status = "planned" },
                        label = { Text(strings.statusPlanned) }
                    )
                    FilterChip(
                        selected = status == "harvested",
                        onClick = { status = "harvested" },
                        label = { Text(strings.statusHarvested) }
                    )
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(strings.notes) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nameBn.isBlank() && nameEn.isBlank()) {
                        nameError = true
                        return@Button
                    }
                    val finalBn = if (nameBn.isNotBlank()) nameBn.trim() else nameEn.trim()
                    val finalEn = if (nameEn.isNotBlank()) nameEn.trim() else nameBn.trim()
                    val dur = durationText.toIntOrNull()
                    if (dur == null || dur <= 0) {
                        durationError = true
                        return@Button
                    }
                    val area = landAreaText.toDoubleOrNull() ?: 0.0

                    onSave(finalBn, finalEn, variety.trim(), category, season.trim(), dur, area, landUnit, status, notes.trim())
                },
                modifier = Modifier.testTag("submit_crop_button")
            ) {
                Text(strings.save)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(strings.cancel)
            }
        }
    )
}

// Backward compatibility alias
@Composable
fun AddCropDialog(
    strings: CropXStrings,
    onDismiss: () -> Unit,
    onConfirm: (nameBn: String, nameEn: String, category: String, season: String, duration: Int, status: String, notes: String) -> Unit
) {
    CropEditDialog(
        strings = strings,
        existingCrop = null,
        onDismiss = onDismiss,
        onSave = { nameBn, nameEn, _, category, season, duration, _, _, status, notes ->
            onConfirm(nameBn, nameEn, category, season, duration, status, notes)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFertilizerDialog(
    strings: CropXStrings,
    onDismiss: () -> Unit,
    onConfirm: (name: String, quantity: Double, unit: String, price: Double, cropName: String, notes: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var quantityText by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf(strings.unitKg) }
    var priceText by remember { mutableStateOf("") }
    var cropName by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }

    val commonFertilizers = listOf("ইউরিয়া (Urea)", "টিএসপি (TSP)", "ডিএপি (DAP)", "এমওপি / পটাশ (MoP)", "জিপসাম (Gypsum)", "জৈব সার (Compost)")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(strings.addFertilizer, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = false
                    },
                    label = { Text(strings.fertilizerName) },
                    placeholder = { Text("ইউরিয়া / টিএসপি / ড্যাপ") },
                    isError = nameError,
                    supportingText = if (nameError) { { Text(strings.validationNameRequired, color = MaterialTheme.colorScheme.error) } } else null,
                    modifier = Modifier.fillMaxWidth().testTag("fertilizer_name_input"),
                    singleLine = true
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = {
                            quantityText = it
                            quantityError = false
                        },
                        label = { Text(strings.fertilizerQuantity) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = quantityError,
                        modifier = Modifier.weight(1.2f).testTag("fertilizer_quantity_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text("একক") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("${strings.fertilizerPrice} (${strings.currencySymbol})") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth().testTag("fertilizer_price_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = cropName,
                    onValueChange = { cropName = it },
                    label = { Text(strings.relatedCrop) },
                    placeholder = { Text("ধান / আলু / ভুট্টা") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(strings.notes) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        nameError = true
                        return@Button
                    }
                    val qty = quantityText.toDoubleOrNull()
                    if (qty == null || qty <= 0.0) {
                        quantityError = true
                        return@Button
                    }
                    val prc = priceText.toDoubleOrNull() ?: 0.0
                    onConfirm(name.trim(), qty, unit.trim(), prc, cropName.trim(), notes.trim())
                },
                modifier = Modifier.testTag("save_fertilizer_button")
            ) {
                Text(strings.save)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text(strings.cancel) }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPesticideDialog(
    strings: CropXStrings,
    onDismiss: () -> Unit,
    onConfirm: (name: String, quantity: Double, unit: String, price: Double, cropName: String, notes: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var quantityText by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf(strings.unitMl) }
    var priceText by remember { mutableStateOf("") }
    var cropName by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(strings.addPesticide, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = false
                    },
                    label = { Text(strings.pesticideName) },
                    placeholder = { Text("কীটনাশকের নাম বা ব্র্যান্ড") },
                    isError = nameError,
                    supportingText = if (nameError) { { Text(strings.validationNameRequired, color = MaterialTheme.colorScheme.error) } } else null,
                    modifier = Modifier.fillMaxWidth().testTag("pesticide_name_input"),
                    singleLine = true
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = {
                            quantityText = it
                            quantityError = false
                        },
                        label = { Text(strings.pesticideQuantity) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = quantityError,
                        modifier = Modifier.weight(1.2f).testTag("pesticide_quantity_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text("একক") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("${strings.pesticidePrice} (${strings.currencySymbol})") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth().testTag("pesticide_price_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = cropName,
                    onValueChange = { cropName = it },
                    label = { Text(strings.relatedCrop) },
                    placeholder = { Text("বেগুন / আলু / ধান") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(strings.notes) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        nameError = true
                        return@Button
                    }
                    val qty = quantityText.toDoubleOrNull()
                    if (qty == null || qty <= 0.0) {
                        quantityError = true
                        return@Button
                    }
                    val prc = priceText.toDoubleOrNull() ?: 0.0
                    onConfirm(name.trim(), qty, unit.trim(), prc, cropName.trim(), notes.trim())
                },
                modifier = Modifier.testTag("save_pesticide_button")
            ) {
                Text(strings.save)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text(strings.cancel) }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHarvestDialog(
    strings: CropXStrings,
    onDismiss: () -> Unit,
    onConfirm: (cropName: String, quantity: Double, unit: String, notes: String) -> Unit
) {
    var cropName by remember { mutableStateOf("") }
    var quantityText by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf(strings.unitMaund) }
    var notes by remember { mutableStateOf("") }

    var cropNameError by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(strings.addHarvest, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = cropName,
                    onValueChange = {
                        cropName = it
                        cropNameError = false
                    },
                    label = { Text(strings.relatedCrop) },
                    placeholder = { Text("যেমন: ধান / আলু / বেগুন") },
                    isError = cropNameError,
                    supportingText = if (cropNameError) { { Text(strings.validationNameRequired, color = MaterialTheme.colorScheme.error) } } else null,
                    modifier = Modifier.fillMaxWidth().testTag("harvest_crop_input"),
                    singleLine = true
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = {
                            quantityText = it
                            quantityError = false
                        },
                        label = { Text(strings.harvestedQuantity) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = quantityError,
                        modifier = Modifier.weight(1.2f).testTag("harvest_quantity_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text(strings.harvestUnit) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = unit == strings.unitMaund,
                        onClick = { unit = strings.unitMaund },
                        label = { Text(strings.unitMaund.substringBefore(" ")) }
                    )
                    FilterChip(
                        selected = unit == strings.unitKg,
                        onClick = { unit = strings.unitKg },
                        label = { Text(strings.unitKg) }
                    )
                    FilterChip(
                        selected = unit == strings.unitPiece,
                        onClick = { unit = strings.unitPiece },
                        label = { Text(strings.unitPiece) }
                    )
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(strings.notes) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (cropName.isBlank()) {
                        cropNameError = true
                        return@Button
                    }
                    val qty = quantityText.toDoubleOrNull()
                    if (qty == null || qty <= 0.0) {
                        quantityError = true
                        return@Button
                    }
                    onConfirm(cropName.trim(), qty, unit.trim(), notes.trim())
                },
                modifier = Modifier.testTag("save_harvest_button")
            ) {
                Text(strings.save)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text(strings.cancel) }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSaleDialog(
    strings: CropXStrings,
    onDismiss: () -> Unit,
    onConfirm: (cropName: String, quantity: Double, unit: String, sellingPrice: Double, buyer: String, notes: String) -> Unit
) {
    var cropName by remember { mutableStateOf("") }
    var quantityText by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf(strings.unitMaund) }
    var unitPriceText by remember { mutableStateOf("") }
    var buyer by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    var cropError by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }

    val quantityDouble = quantityText.toDoubleOrNull() ?: 0.0
    val priceDouble = unitPriceText.toDoubleOrNull() ?: 0.0
    val totalSale = quantityDouble * priceDouble

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(strings.addSale, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = cropName,
                    onValueChange = {
                        cropName = it
                        cropError = false
                    },
                    label = { Text(strings.relatedCrop) },
                    placeholder = { Text("ধান / আলু / টমেটো / বেগুন") },
                    isError = cropError,
                    supportingText = if (cropError) { { Text(strings.validationNameRequired, color = MaterialTheme.colorScheme.error) } } else null,
                    modifier = Modifier.fillMaxWidth().testTag("sale_crop_input"),
                    singleLine = true
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = quantityText,
                        onValueChange = {
                            quantityText = it
                            quantityError = false
                        },
                        label = { Text(strings.soldQuantity) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = quantityError,
                        modifier = Modifier.weight(1.2f).testTag("sale_quantity_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text("একক") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = unitPriceText,
                    onValueChange = {
                        unitPriceText = it
                        priceError = false
                    },
                    label = { Text("${strings.unitPrice} (${strings.currencySymbol})") },
                    placeholder = { Text("প্রতি এককের দাম") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = priceError,
                    modifier = Modifier.fillMaxWidth().testTag("sale_unit_price_input"),
                    singleLine = true
                )

                // Calculated total sales indicator
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = strings.totalSaleCalculated,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "${strings.currencySymbol}${String.format(Locale.US, "%,.2f", totalSale)}",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                OutlinedTextField(
                    value = buyer,
                    onValueChange = { buyer = it },
                    label = { Text(strings.buyerName) },
                    placeholder = { Text("ক্রেতা / আড়তদারের নাম") },
                    modifier = Modifier.fillMaxWidth().testTag("sale_buyer_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(strings.notes) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (cropName.isBlank()) {
                        cropError = true
                        return@Button
                    }
                    if (quantityDouble <= 0.0) {
                        quantityError = true
                        return@Button
                    }
                    if (priceDouble <= 0.0) {
                        priceError = true
                        return@Button
                    }
                    onConfirm(cropName.trim(), quantityDouble, unit.trim(), priceDouble, buyer.trim(), notes.trim())
                },
                modifier = Modifier.testTag("save_sale_button")
            ) {
                Text(strings.save)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text(strings.cancel) }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLandDialog(
    strings: CropXStrings,
    onDismiss: () -> Unit,
    onConfirm: (name: String, area: Double, unit: String, location: String, ownership: String, crop: String, soil: String, notes: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var areaText by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf("shotok") }
    var location by remember { mutableStateOf("") }
    var ownership by remember { mutableStateOf("own") }
    var associatedCrop by remember { mutableStateOf("") }
    var soilType by remember { mutableStateOf("দোআঁশ / Loamy") }
    var notes by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf(false) }
    var areaError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(strings.addLandPlot, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = false
                    },
                    label = { Text(strings.plotName) },
                    placeholder = { Text("যেমন: উত্তর মাঠ ১ / Field 1") },
                    isError = nameError,
                    supportingText = if (nameError) { { Text(strings.validationNameRequired, color = MaterialTheme.colorScheme.error) } } else null,
                    modifier = Modifier.fillMaxWidth().testTag("land_name_input"),
                    singleLine = true
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = areaText,
                        onValueChange = {
                            areaText = it
                            areaError = false
                        },
                        label = { Text(strings.landArea) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = areaError,
                        supportingText = if (areaError) { { Text(strings.validationQuantityInvalid, color = MaterialTheme.colorScheme.error) } } else null,
                        modifier = Modifier.fillMaxWidth().testTag("land_area_input"),
                        singleLine = true
                    )
                }

                Text(strings.landUnit, style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilterChip(
                        selected = selectedUnit == "shotok",
                        onClick = { selectedUnit = "shotok" },
                        label = { Text(strings.unitShotok) }
                    )
                    FilterChip(
                        selected = selectedUnit == "bigha",
                        onClick = { selectedUnit = "bigha" },
                        label = { Text(strings.unitBigha) }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilterChip(
                        selected = selectedUnit == "acre",
                        onClick = { selectedUnit = "acre" },
                        label = { Text(strings.unitAcre) }
                    )
                    FilterChip(
                        selected = selectedUnit == "hectare",
                        onClick = { selectedUnit = "hectare" },
                        label = { Text(strings.unitHectare) }
                    )
                }

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text(strings.location) },
                    placeholder = { Text("গ্রাম / মৌজা / উপজেলা") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text(strings.ownershipType, style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = ownership == "own",
                        onClick = { ownership = "own" },
                        label = { Text(strings.ownershipOwn) }
                    )
                    FilterChip(
                        selected = ownership == "leased",
                        onClick = { ownership = "leased" },
                        label = { Text(strings.ownershipLeased) }
                    )
                    FilterChip(
                        selected = ownership == "sharecrop",
                        onClick = { ownership = "sharecrop" },
                        label = { Text(strings.ownershipSharecrop) }
                    )
                }

                OutlinedTextField(
                    value = associatedCrop,
                    onValueChange = { associatedCrop = it },
                    label = { Text(strings.associatedCrop) },
                    placeholder = { Text("ধান / আলু / মরিচ") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = soilType,
                    onValueChange = { soilType = it },
                    label = { Text(strings.soilType) },
                    placeholder = { Text("দোআঁশ / বেলে-দোআঁশ / এঁটেল") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(strings.notes) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        nameError = true
                        return@Button
                    }
                    val areaVal = areaText.toDoubleOrNull()
                    if (areaVal == null || areaVal <= 0.0) {
                        areaError = true
                        return@Button
                    }
                    onConfirm(name.trim(), areaVal, selectedUnit, location.trim(), ownership, associatedCrop.trim(), soilType.trim(), notes.trim())
                },
                modifier = Modifier.testTag("save_land_button")
            ) {
                Text(strings.saveLand)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(strings.cancel)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    strings: CropXStrings,
    initialType: String = "expense",
    onDismiss: () -> Unit,
    onConfirm: (type: String, category: String, amount: Double, crop: String, land: String, notes: String) -> Unit
) {
    var type by remember { mutableStateOf(initialType) }
    var amountText by remember { mutableStateOf("") }
    var selectedCategory by remember {
        mutableStateOf(if (initialType == "expense") strings.catFertilizer else strings.catSale)
    }
    var relatedCrop by remember { mutableStateOf("") }
    var relatedLand by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var amountError by remember { mutableStateOf(false) }

    val expenseCategories = listOf(
        strings.catSeeds,
        strings.catFertilizer,
        strings.catIrrigation,
        strings.catLabor,
        strings.catPesticide,
        strings.catMachinery,
        strings.catTransport,
        strings.catOther
    )

    val incomeCategories = listOf(
        strings.catSale,
        strings.catSubsidy,
        strings.catOther
    )

    var categoryExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (type == "expense") strings.addExpense else strings.addTransaction, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = type == "expense",
                        onClick = {
                            type = "expense"
                            selectedCategory = strings.catFertilizer
                        },
                        label = { Text(strings.typeExpense) },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = type == "income",
                        onClick = {
                            type = "income"
                            selectedCategory = strings.catSale
                        },
                        label = { Text(strings.typeIncome) },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = amountText,
                    onValueChange = {
                        amountText = it
                        amountError = false
                    },
                    label = { Text("${strings.amount} (${strings.currencySymbol})") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = amountError,
                    supportingText = if (amountError) { { Text(strings.validationPriceInvalid, color = MaterialTheme.colorScheme.error) } } else null,
                    modifier = Modifier.fillMaxWidth().testTag("transaction_amount_input"),
                    singleLine = true
                )

                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(strings.category) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        val activeCategories = if (type == "expense") expenseCategories else incomeCategories
                        activeCategories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    selectedCategory = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = relatedCrop,
                    onValueChange = { relatedCrop = it },
                    label = { Text(strings.relatedCrop) },
                    placeholder = { Text("ধান / আলু / টমেটো") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = relatedLand,
                    onValueChange = { relatedLand = it },
                    label = { Text(strings.relatedLand) },
                    placeholder = { Text("উত্তর মাঠ ১") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(strings.notes) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amt = amountText.toDoubleOrNull()
                    if (amt == null || amt <= 0.0) {
                        amountError = true
                        return@Button
                    }
                    onConfirm(type, selectedCategory, amt, relatedCrop.trim(), relatedLand.trim(), notes.trim())
                },
                modifier = Modifier.testTag("save_transaction_button")
            ) {
                Text(strings.saveTransaction)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(strings.cancel)
            }
        }
    )
}
