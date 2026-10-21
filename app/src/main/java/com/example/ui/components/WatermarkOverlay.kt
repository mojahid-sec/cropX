package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R

/**
 * Renders the developer's authentic uploaded photograph as a subtle watermark
 * throughout the app with 6% opacity (strictly within the 4-8% range requested).
 * ContentScale.Fit preserves original facial geometry, aspect ratio, and identity
 * without stretching, cropping, or distortion.
 */
@Composable
fun AppWatermarkContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Subtle background watermark at 6% opacity
        Image(
            painter = painterResource(id = R.drawable.developer_photo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(0.85f)
                .align(Alignment.Center)
                .alpha(0.06f), // 6% opacity
            contentScale = ContentScale.Fit
        )

        // Main interactive content
        content()
    }
}
