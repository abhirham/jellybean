package org.jellyfin.androidtv.ui.composable.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme as TvMaterialTheme

/**
 * Jellyfin TV Theme
 *
 * Netflix-style theme for Android TV using Jetpack Compose for TV.
 * This theme provides TV-optimized colors, typography, and shapes designed for
 * 10-foot viewing experiences.
 *
 * IMPORTANT: This uses androidx.tv.material3 (TV Material), NOT mobile Material.
 * Never mix TV and mobile Material libraries as they have incompatible theme systems.
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun JellyfinTvTheme(
	useDarkTheme: Boolean = true,
	content: @Composable () -> Unit
) {
	val colorScheme = if (useDarkTheme) {
		DarkTvColorScheme
	} else {
		LightTvColorScheme
	}

	TvMaterialTheme(
		colorScheme = colorScheme,
		typography = TvTypography,
		shapes = TvShapes,
	) {
		CompositionLocalProvider(
			LocalTvPadding provides TvPadding,
			content = content
		)
	}
}
