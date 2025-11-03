package org.jellyfin.androidtv.ui.composable.theme

import androidx.compose.ui.graphics.Color
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.darkColorScheme
import androidx.tv.material3.lightColorScheme

/**
 * Jellyfin TV Color Palette
 *
 * Netflix-inspired color scheme optimized for TV viewing.
 * Dark theme is primary as most TV viewing happens in dim environments.
 */

// Jellyfin Brand Colors
val JellyfinPurple = Color(0xFFAA5CC3)
val JellyfinPurpleDark = Color(0xFF9A4FB4)
val JellyfinPurpleLight = Color(0xFFBD7CD3)

// Dark Theme Colors (Primary for TV)
val DarkBackground = Color(0xFF141414) // Netflix-style near-black
val DarkSurface = Color(0xFF1F1F1F)
val DarkSurfaceVariant = Color(0xFF2B2B2B)
val DarkOnBackground = Color(0xFFE5E5E5)
val DarkOnSurface = Color(0xFFE5E5E5)

// Focus & Selection Colors
val FocusedBorder = Color(0xFFFFFFFF) // Bright white for clear focus indication
val SelectedBackground = Color(0xFF2B2B2B)
val HoverBackground = Color(0xFF323232)

// Error & Warning Colors
val ErrorRed = Color(0xFFE50914) // Netflix-style red
val WarningOrange = Color(0xFFFF9800)
val SuccessGreen = Color(0xFF4CAF50)

@OptIn(ExperimentalTvMaterial3Api::class)
val DarkTvColorScheme = darkColorScheme(
	primary = JellyfinPurple,
	onPrimary = Color.White,
	primaryContainer = JellyfinPurpleDark,
	onPrimaryContainer = Color.White,

	secondary = JellyfinPurpleLight,
	onSecondary = Color.White,
	secondaryContainer = DarkSurfaceVariant,
	onSecondaryContainer = DarkOnSurface,

	tertiary = Color(0xFFB0BEC5),
	onTertiary = Color(0xFF1F1F1F),

	error = ErrorRed,
	onError = Color.White,
	errorContainer = Color(0xFF4D0000),
	onErrorContainer = Color(0xFFFFDAD6),

	background = DarkBackground,
	onBackground = DarkOnBackground,

	surface = DarkSurface,
	onSurface = DarkOnSurface,
	surfaceVariant = DarkSurfaceVariant,
	onSurfaceVariant = Color(0xFFB0B0B0),

	scrim = Color.Black.copy(alpha = 0.75f),

	inverseSurface = Color.White,
	inverseOnSurface = DarkBackground,
	inversePrimary = JellyfinPurpleDark,
)

@OptIn(ExperimentalTvMaterial3Api::class)
val LightTvColorScheme = lightColorScheme(
	primary = JellyfinPurpleDark,
	onPrimary = Color.White,
	primaryContainer = JellyfinPurpleLight,
	onPrimaryContainer = Color(0xFF1F0033),

	secondary = Color(0xFF625B71),
	onSecondary = Color.White,
	secondaryContainer = Color(0xFFE8DEF8),
	onSecondaryContainer = Color(0xFF1D192B),

	tertiary = Color(0xFF7D5260),
	onTertiary = Color.White,

	error = ErrorRed,
	onError = Color.White,
	errorContainer = Color(0xFFFFDAD6),
	onErrorContainer = Color(0xFF410002),

	background = Color(0xFFFFFBFE),
	onBackground = Color(0xFF1C1B1F),

	surface = Color(0xFFFFFBFE),
	onSurface = Color(0xFF1C1B1F),
	surfaceVariant = Color(0xFFE7E0EC),
	onSurfaceVariant = Color(0xFF49454F),

	scrim = Color.Black,

	inverseSurface = Color(0xFF313033),
	inverseOnSurface = Color(0xFFF4EFF4),
	inversePrimary = JellyfinPurpleLight,
)

/**
 * Additional semantic colors for Jellyfin-specific use cases
 */
object JellyfinColors {
	// Media item states
	val watched = Color(0xFF4CAF50).copy(alpha = 0.7f)
	val unwatched = Color(0xFF9E9E9E).copy(alpha = 0.5f)
	val inProgress = JellyfinPurple

	// Content ratings
	val ratingG = Color(0xFF4CAF50)
	val ratingPG = Color(0xFF8BC34A)
	val ratingPG13 = Color(0xFFFF9800)
	val ratingR = Color(0xFFE50914)

	// Live TV
	val live = ErrorRed
	val recording = ErrorRed.copy(alpha = 0.8f)

	// Overlay gradients
	val heroGradientTop = Color.Transparent
	val heroGradientBottom = DarkBackground.copy(alpha = 0.95f)
	val cardGradientBottom = DarkBackground.copy(alpha = 0.7f)
}
