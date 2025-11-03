package org.jellyfin.androidtv.ui.composable.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Shapes

/**
 * Jellyfin TV Shapes
 *
 * Shape definitions for Compose for TV components.
 * TV shapes tend to be more subtle than mobile to reduce visual clutter at distance.
 */

@OptIn(ExperimentalTvMaterial3Api::class)
val TvShapes = Shapes(
	// Extra small - Used for indicators, badges
	extraSmall = RoundedCornerShape(4.dp),

	// Small - Used for buttons, chips
	small = RoundedCornerShape(8.dp),

	// Medium - Used for cards, list items (most common for media cards)
	medium = RoundedCornerShape(12.dp),

	// Large - Used for dialogs, bottom sheets
	large = RoundedCornerShape(16.dp),

	// Extra large - Used for large surfaces
	extraLarge = RoundedCornerShape(24.dp),
)
