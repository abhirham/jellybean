package org.jellyfin.androidtv.ui.composable.focus

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jellyfin.androidtv.ui.composable.theme.FocusedBorder

/**
 * TV Focus Indicators
 *
 * Provides visual feedback for focused elements on TV.
 * Netflix-style focus effects with scaling and borders.
 */

/**
 * Focus scale effect (Netflix-style)
 *
 * Scales up element when focused for prominence.
 *
 * @param focusedScale Scale factor when focused (default 1.05 = 5% larger)
 * @param unfocusedScale Scale factor when not focused (default 1.0)
 */
fun Modifier.focusScale(
	focusedScale: Float = 1.05f,
	unfocusedScale: Float = 1.0f
): Modifier = composed {
	var isFocused by remember { mutableStateOf(false) }
	val scale by animateFloatAsState(
		targetValue = if (isFocused) focusedScale else unfocusedScale,
		label = "focusScale"
	)

	this
		.onFocusChanged { isFocused = it.isFocused }
		.scale(scale)
}

/**
 * Focus border indicator
 *
 * Shows a white border when element is focused.
 *
 * @param focusedBorderWidth Border width when focused
 * @param unfocusedBorderWidth Border width when not focused (default 0)
 * @param borderColor Color of the focus border
 * @param cornerRadius Corner radius for the border
 */
fun Modifier.focusBorder(
	focusedBorderWidth: Dp = 3.dp,
	unfocusedBorderWidth: Dp = 0.dp,
	borderColor: Color = FocusedBorder,
	cornerRadius: Dp = 8.dp
): Modifier = composed {
	var isFocused by remember { mutableStateOf(false) }
	val borderWidth by animateDpAsState(
		targetValue = if (isFocused) focusedBorderWidth else unfocusedBorderWidth,
		label = "focusBorder"
	)

	this
		.onFocusChanged { isFocused = it.isFocused }
		.border(
			border = BorderStroke(borderWidth, borderColor),
			shape = androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius)
		)
}

/**
 * Focus shadow effect
 *
 * Adds elevation shadow when focused.
 *
 * @param focusedElevation Shadow elevation when focused
 * @param unfocusedElevation Shadow elevation when not focused
 */
fun Modifier.focusShadow(
	focusedElevation: Dp = 8.dp,
	unfocusedElevation: Dp = 0.dp
): Modifier = composed {
	var isFocused by remember { mutableStateOf(false) }
	val elevation by animateDpAsState(
		targetValue = if (isFocused) focusedElevation else unfocusedElevation,
		label = "focusShadow"
	)

	this
		.onFocusChanged { isFocused = it.isFocused }
		.shadow(elevation)
}

/**
 * Combined TV focus effect (Netflix-style)
 *
 * Combines scale, border, and shadow for complete focus feedback.
 *
 * @param scale Scale factor when focused
 * @param borderWidth Border width when focused
 * @param elevation Shadow elevation when focused
 */
fun Modifier.tvFocusEffect(
	scale: Float = 1.05f,
	borderWidth: Dp = 3.dp,
	elevation: Dp = 8.dp
): Modifier = this
	.focusScale(focusedScale = scale)
	.focusBorder(focusedBorderWidth = borderWidth)
	.focusShadow(focusedElevation = elevation)

/**
 * Focus padding to prevent clipping when scaled
 *
 * Adds padding to prevent content from being clipped when focus scale is applied.
 *
 * @param scale The scale factor used in focusScale
 */
fun Modifier.focusPadding(scale: Float = 1.05f): Modifier {
	// Calculate padding needed to prevent clipping
	// For 5% scale (1.05), we need ~2.5% padding on each side
	val paddingPercent = (scale - 1.0f) / 2.0f
	val padding = (8 * paddingPercent).dp

	return this.padding(padding)
}
