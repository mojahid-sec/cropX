package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.CropXStrings
import com.example.ui.theme.EarthBrown
import com.example.ui.theme.ExpenseRed
import com.example.ui.theme.ForestGreenPrimary
import com.example.ui.theme.HarvestGold
import com.example.ui.theme.ProfitGreen
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmingCalculatorScreen(
    strings: CropXStrings,
    currentLanguage: AppLanguage,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Math, 1: Land

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(strings.calculatorTitle, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("calc_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Calculate, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.calculatorStandard)
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Landscape, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.calculatorLandConverter)
                        }
                    }
                )
            }

            if (selectedTab == 0) {
                StandardMathCalculator(strings = strings, currentLanguage = currentLanguage)
            } else {
                LandAreaConverterView(strings = strings, currentLanguage = currentLanguage)
            }
        }
    }
}

@Composable
fun StandardMathCalculator(
    strings: CropXStrings,
    currentLanguage: AppLanguage
) {
    var display by remember { mutableStateOf("0") }
    var expression by remember { mutableStateOf("") }
    var operator by remember { mutableStateOf<String?>(null) }
    var firstOperand by remember { mutableStateOf<Double?>(null) }
    var isNewOperand by remember { mutableStateOf(false) }

    fun onDigit(d: String) {
        if (display == "0" || isNewOperand) {
            display = d
            isNewOperand = false
        } else {
            if (display.length < 12) {
                display += d
            }
        }
    }

    fun onDot() {
        if (isNewOperand) {
            display = "0."
            isNewOperand = false
        } else if (!display.contains(".")) {
            display += "."
        }
    }

    fun onOperator(op: String) {
        val currentVal = display.toDoubleOrNull() ?: 0.0
        firstOperand = currentVal
        operator = op
        expression = "${formatNumber(currentVal)} $op"
        isNewOperand = true
    }

    fun onEquals() {
        if (firstOperand != null && operator != null) {
            val second = display.toDoubleOrNull() ?: 0.0
            val op = operator!!
            val result = when (op) {
                "+" -> firstOperand!! + second
                "-" -> firstOperand!! - second
                "×" -> firstOperand!! * second
                "÷" -> if (second != 0.0) firstOperand!! / second else Double.NaN
                "%" -> (firstOperand!! * second) / 100.0
                else -> second
            }

            expression = "${formatNumber(firstOperand!!)} $op ${formatNumber(second)} ="
            display = if (result.isNaN() || result.isInfinite()) "Error" else formatNumber(result)
            firstOperand = null
            operator = null
            isNewOperand = true
        }
    }

    fun onClear() {
        display = "0"
        expression = ""
        firstOperand = null
        operator = null
        isNewOperand = false
    }

    fun onBackspace() {
        if (!isNewOperand && display.isNotEmpty() && display != "0" && display != "Error") {
            display = if (display.length > 1) display.dropLast(1) else "0"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Display Screen
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = expression,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    minLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = display,
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
            }
        }

        // Keypad Grid
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Row 1: C, Backspace, %, ÷
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CalcButton(text = "C", color = ExpenseRed.copy(alpha = 0.15f), textColor = ExpenseRed, modifier = Modifier.weight(1f)) { onClear() }
                CalcButton(icon = Icons.Default.Backspace, color = MaterialTheme.colorScheme.surfaceVariant, textColor = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f)) { onBackspace() }
                CalcButton(text = "%", color = MaterialTheme.colorScheme.surfaceVariant, textColor = MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f)) { onOperator("%") }
                CalcButton(text = "÷", color = ForestGreenPrimary, textColor = Color.White, modifier = Modifier.weight(1f)) { onOperator("÷") }
            }

            // Row 2: 7, 8, 9, ×
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CalcButton(text = "7", modifier = Modifier.weight(1f)) { onDigit("7") }
                CalcButton(text = "8", modifier = Modifier.weight(1f)) { onDigit("8") }
                CalcButton(text = "9", modifier = Modifier.weight(1f)) { onDigit("9") }
                CalcButton(text = "×", color = ForestGreenPrimary, textColor = Color.White, modifier = Modifier.weight(1f)) { onOperator("×") }
            }

            // Row 3: 4, 5, 6, -
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CalcButton(text = "4", modifier = Modifier.weight(1f)) { onDigit("4") }
                CalcButton(text = "5", modifier = Modifier.weight(1f)) { onDigit("5") }
                CalcButton(text = "6", modifier = Modifier.weight(1f)) { onDigit("6") }
                CalcButton(text = "-", color = ForestGreenPrimary, textColor = Color.White, modifier = Modifier.weight(1f)) { onOperator("-") }
            }

            // Row 4: 1, 2, 3, +
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CalcButton(text = "1", modifier = Modifier.weight(1f)) { onDigit("1") }
                CalcButton(text = "2", modifier = Modifier.weight(1f)) { onDigit("2") }
                CalcButton(text = "3", modifier = Modifier.weight(1f)) { onDigit("3") }
                CalcButton(text = "+", color = ForestGreenPrimary, textColor = Color.White, modifier = Modifier.weight(1f)) { onOperator("+") }
            }

            // Row 5: 00, 0, ., =
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CalcButton(text = "00", modifier = Modifier.weight(1f)) { onDigit("00") }
                CalcButton(text = "0", modifier = Modifier.weight(1f)) { onDigit("0") }
                CalcButton(text = ".", modifier = Modifier.weight(1f)) { onDot() }
                CalcButton(text = "=", color = HarvestGold, textColor = Color.Black, modifier = Modifier.weight(1f)) { onEquals() }
            }
        }
    }
}

@Composable
fun CalcButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    color: Color = MaterialTheme.colorScheme.surface,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .height(58.dp)
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (icon != null) {
                Icon(icon, contentDescription = null, tint = textColor)
            } else if (text != null) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = textColor
                )
            }
        }
    }
}

private fun formatNumber(value: Double): String {
    return if (value % 1.0 == 0.0) {
        String.format(Locale.US, "%.0f", value)
    } else {
        String.format(Locale.US, "%.4f", value).trimEnd('0').trimEnd('.')
    }
}

@Composable
fun LandAreaConverterView(
    strings: CropXStrings,
    currentLanguage: AppLanguage
) {
    var inputValue by remember { mutableStateOf("1") }
    var selectedUnit by remember { mutableStateOf("bigha") } // "shotok", "bigha", "acre", "hectare", "katha", "sqft"

    val inputDouble = inputValue.toDoubleOrNull() ?: 0.0

    // Standard baseline in Shotok (1 Bigha = 33 Shotok in Bangladesh, 1 Acre = 100 Shotok, 1 Katha = 1.65 Shotok)
    val baselineShotok = remember(inputDouble, selectedUnit) {
        when (selectedUnit) {
            "shotok" -> inputDouble
            "bigha" -> inputDouble * 33.0
            "acre" -> inputDouble * 100.0
            "hectare" -> inputDouble * 247.1
            "katha" -> inputDouble * 1.65
            "sqft" -> inputDouble / 435.6
            else -> inputDouble
        }
    }

    val bigha = baselineShotok / 33.0
    val acre = baselineShotok / 100.0
    val hectare = baselineShotok / 247.1
    val katha = baselineShotok / 1.65
    val sqft = baselineShotok * 435.6
    val sqMetre = sqft * 0.092903

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = strings.convertFrom,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { inputValue = it },
                        label = { Text(strings.landArea) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth().testTag("calc_land_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = selectedUnit == "bigha",
                            onClick = { selectedUnit = "bigha" },
                            label = { Text("বিঘা (Bigha)") }
                        )
                        FilterChip(
                            selected = selectedUnit == "shotok",
                            onClick = { selectedUnit = "shotok" },
                            label = { Text("শতক (Shotok)") }
                        )
                        FilterChip(
                            selected = selectedUnit == "acre",
                            onClick = { selectedUnit = "acre" },
                            label = { Text("একর (Acre)") }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = selectedUnit == "hectare",
                            onClick = { selectedUnit = "hectare" },
                            label = { Text("হেক্টর (Hectare)") }
                        )
                        FilterChip(
                            selected = selectedUnit == "katha",
                            onClick = { selectedUnit = "katha" },
                            label = { Text("কাঠা (Katha)") }
                        )
                        FilterChip(
                            selected = selectedUnit == "sqft",
                            onClick = { selectedUnit = "sqft" },
                            label = { Text("বর্গফুট (Sq Ft)") }
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = strings.convertTo,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ConversionResultRow("শতক / ডেসিমাল (Shotok / Decimal)", String.format(Locale.US, "%.3f", baselineShotok), "শতক")
                ConversionResultRow("বিঘা (Bigha = ৩৩ শতক)", String.format(Locale.US, "%.3f", bigha), "বিঘা")
                ConversionResultRow("কাঠা (Katha = ১.৬৫ শতক)", String.format(Locale.US, "%.2f", katha), "কাঠা")
                ConversionResultRow("একর (Acre = ১০০ শতক)", String.format(Locale.US, "%.4f", acre), "একর")
                ConversionResultRow("হেক্টর (Hectare = ২৪৭.১ শতক)", String.format(Locale.US, "%.4f", hectare), "হেক্টর")
                ConversionResultRow("বর্গফুট (Square Feet)", String.format(Locale.US, "%,.1f", sqft), "বর্গফুট")
                ConversionResultRow("বর্গমিটার (Square Metre)", String.format(Locale.US, "%,.2f", sqMetre), "বর্গমিটার")
            }
        }

        item {
            OutlinedCard(
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "📌 প্রচলিত ভূমি রূপান্তর সূত্রাবলি:",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text("• ১ বিঘা = ৩৩ শতক = ২০ কাঠা = ১৪,৩৭৬ বর্গফুট", style = MaterialTheme.typography.bodySmall)
                    Text("• ১ একর = ১০০ শতক = ৩.০৩ বিঘা = ৪৩,৫৬০ বর্গফুট", style = MaterialTheme.typography.bodySmall)
                    Text("• ১ হেক্টর = ২.৪৭ একর = ৭.৪৮ বিঘা = ২৪৭.১০ শতক", style = MaterialTheme.typography.bodySmall)
                    Text("• ১ কাঠা = ১.৬৫ শতক = ৭২০ বর্গফুট", style = MaterialTheme.typography.bodySmall)
                    Text("• ১ শতক = ৪৩৫.৬ বর্গফুট = ৪০.৪৭ বর্গমিটার", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun ConversionResultRow(
    label: String,
    value: String,
    unit: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "$value $unit",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}
