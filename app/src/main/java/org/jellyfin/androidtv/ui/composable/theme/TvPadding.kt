package org.jellyfin.androidtv.ui.composable.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Jellyfin TV Padding System
 *
 * Consistent padding values for TV layouts following Netflix-style spacing.
 * TV requires larger padding than mobile due to viewing distance and screen size.
 */

@Immutable
data class TvPaddingValues(
	// Screen edge padding (safe area from screen edges)
	val screenHorizontal: Dp = 48.dp,
	val screenVertical: Dp = 48.dp,

	// Component padding
	val extraSmall: Dp = 4.dp,
	val small: Dp = 8.dp,
	val medium: Dp = 16.dp,
	val large: Dp = 24.dp,
	val extraLarge: Dp = 32.dp,

	// Carousel spacing
	val carouselItemSpacing: Dp = 12.dp,
	val carouselVerticalSpacing: Dp = 24.dp,

	// Card padding
	val cardPadding: Dp = 12.dp,
	val cardContentPadding: Dp = 16.dp,

	// Hero banner
	val heroContentPadding: Dp = 48.dp,
	val heroBottomPadding: Dp = 32.dp,

	// Navigation drawer
	val drawerWidth: Dp = 240.dp,
	val drawerItemPadding: Dp = 16.dp,
	val drawerItemSpacing: Dp = 8.dp,
)

val TvPadding = TvPaddingValues()

val LocalTvPadding = staticCompositionLocalOf { TvPadding }
