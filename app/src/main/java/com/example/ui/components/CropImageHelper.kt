package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R

object CropImageHelper {

    private val directMap = mapOf(
        "crop_eggplant" to R.drawable.crop_eggplant,
        "crop_rice" to R.drawable.crop_rice,
        "crop_potato" to R.drawable.crop_potato,
        "crop_jute" to R.drawable.crop_jute,
        "crop_tomato" to R.drawable.crop_tomato,
        "crop_cucumber" to R.drawable.crop_cucumber,
        "crop_chili" to R.drawable.crop_chili,
        "crop_onion" to R.drawable.crop_onion,
        "crop_garlic" to R.drawable.crop_garlic,
        "crop_cabbage" to R.drawable.crop_cabbage,
        "crop_cauliflower" to R.drawable.crop_cauliflower,
        "crop_maize" to R.drawable.crop_maize,
        "crop_bottle_gourd" to R.drawable.crop_bottle_gourd,
        "crop_pumpkin" to R.drawable.crop_pumpkin,
        "crop_wheat" to R.drawable.crop_wheat,
        "crop_mustard" to R.drawable.crop_mustard,
        "crop_lentil" to R.drawable.crop_lentil,
        "crop_sweet_potato" to R.drawable.crop_sweet_potato
    )

    /**
     * Maps crop drawable resource name or crop name (Bangla or English)
     * to the locally bundled high-quality photograph drawable ID.
     */
    fun getDrawableForCrop(resName: String, cropNameBn: String = "", cropNameEn: String = ""): Int? {
        val cleanRes = resName.trim().lowercase()
        directMap[cleanRes]?.let { return it }

        // Normalize unicode and strings for matching
        val combined = "$cleanRes ${cropNameBn.trim()} ${cropNameEn.trim()}".lowercase()
            .replace('\u09DC', '\u09A1') // normalize ড় to ড
            .replace('\u09DD', '\u09A2') // normalize ঢ় to ঢ
            .replace('\u09DF', '\u09AF') // normalize য় to য

        return when {
            // Sweet Potato (check before potato)
            combined.contains("sweet_potato") || combined.contains("sweet potato") ||
                combined.contains("মিষ্টি আলু") || combined.contains("মিষ্টিআলু") ||
                combined.contains("রাঙা আলু") || combined.contains("রাঙ্গা আলু") -> R.drawable.crop_sweet_potato

            // Potato
            combined.contains("potato") || combined.contains("আলু") ||
                combined.contains("গোল আলু") || combined.contains("ডায়মন্ড") ||
                combined.contains("কার্ডিনাল") -> R.drawable.crop_potato

            // Eggplant / Brinjal
            combined.contains("eggplant") || combined.contains("brinjal") ||
                combined.contains("aubergine") || combined.contains("বেগুন") ||
                combined.contains("বেগুনী") || combined.contains("ইসলামপুরী") -> R.drawable.crop_eggplant

            // Rice / Paddy
            combined.contains("rice") || combined.contains("paddy") ||
                combined.contains("ধান") || combined.contains("চাল") ||
                combined.contains("বোরো") || combined.contains("আমন") ||
                combined.contains("আউশ") -> R.drawable.crop_rice

            // Jute
            combined.contains("jute") || combined.contains("পাট") ||
                combined.contains("তোষা") || combined.contains("মেস্তা") -> R.drawable.crop_jute

            // Tomato
            combined.contains("tomato") || combined.contains("টমেটো") ||
                combined.contains("টমেটোস") -> R.drawable.crop_tomato

            // Cucumber
            combined.contains("cucumber") || combined.contains("শসা") ||
                combined.contains("শশা") || combined.contains("ক্ষীরা") -> R.drawable.crop_cucumber

            // Chili
            combined.contains("chili") || combined.contains("chilli") ||
                combined.contains("pepper") || combined.contains("মরিচ") ||
                combined.contains("কাঁচামরিচ") || combined.contains("ঝাল") -> R.drawable.crop_chili

            // Onion
            combined.contains("onion") || combined.contains("পেঁয়াজ") ||
                combined.contains("পেঁয়াজ") || combined.contains("পিঁয়াজ") ||
                combined.contains("পিয়াজ") || combined.contains("পিয়াজ") -> R.drawable.crop_onion

            // Garlic
            combined.contains("garlic") || combined.contains("রসুন") ||
                combined.contains("রসুনী") -> R.drawable.crop_garlic

            // Cabbage
            combined.contains("cabbage") || combined.contains("বাঁধাকপি") ||
                combined.contains("বাধাকপি") || combined.contains("পাতাকপি") -> R.drawable.crop_cabbage

            // Cauliflower
            combined.contains("cauliflower") || combined.contains("ফুলকপি") ||
                combined.contains("ফুল কপি") -> R.drawable.crop_cauliflower

            // Maize / Corn
            combined.contains("maize") || combined.contains("corn") ||
                combined.contains("ভুট্টা") -> R.drawable.crop_maize

            // Bottle Gourd
            combined.contains("bottle_gourd") || combined.contains("bottle gourd") ||
                combined.contains("calabash") || combined.contains("লাউ") ||
                combined.contains("কদু") -> R.drawable.crop_bottle_gourd

            // Pumpkin
            combined.contains("pumpkin") || combined.contains("squash") ||
                combined.contains("মিষ্টি কুম") || combined.contains("কুম") -> R.drawable.crop_pumpkin

            // Wheat
            combined.contains("wheat") || combined.contains("গম") ||
                combined.contains("আটা") -> R.drawable.crop_wheat

            // Mustard
            combined.contains("mustard") || combined.contains("সরিষা") ||
                combined.contains("সর্ষে") || combined.contains("রাই") -> R.drawable.crop_mustard

            // Lentil / Dal
            combined.contains("lentil") || combined.contains("মসুর") ||
                combined.contains("মশুর") || combined.contains("ডাল") -> R.drawable.crop_lentil

            else -> null
        }
    }
}

/**
 * Renders a perfectly circular crop image adhering strictly to:
 * - Equal width & height (size x size)
 * - Perfect 50% circle clipping (`border-radius: 50%`, `overflow: hidden`)
 * - 100% width and height filling the container (`fillMaxSize`)
 * - Object-fit: cover & object-position: center (`ContentScale.Crop`, `Alignment.Center`)
 * - Clean, theme-aware placeholder fallback if image is missing
 * - Subtle border accent and elevation for both Light and Dark modes
 */
@Composable
fun CircularCropIcon(
    resName: String,
    cropNameBn: String = "",
    cropNameEn: String = "",
    size: Dp = 84.dp,
    borderWidth: Dp = 2.dp,
    borderColor: Color? = null,
    elevation: Dp = 2.dp,
    modifier: Modifier = Modifier
) {
    val drawableId = remember(resName, cropNameBn, cropNameEn) {
        CropImageHelper.getDrawableForCrop(resName, cropNameBn, cropNameEn)
    }

    val finalBorderColor = borderColor ?: MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)

    Surface(
        shape = CircleShape,
        border = BorderStroke(borderWidth, finalBorderColor),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = elevation,
        modifier = modifier.size(size)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (drawableId != null) {
                Image(
                    painter = painterResource(id = drawableId),
                    contentDescription = if (cropNameBn.isNotBlank()) "$cropNameBn ($cropNameEn)" else cropNameEn,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Clean theme-adaptive placeholder when image file is missing
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = cropNameBn.ifBlank { cropNameEn }.ifBlank { "Crop" },
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(size * 0.45f)
                    )
                }
            }
        }
    }
}
