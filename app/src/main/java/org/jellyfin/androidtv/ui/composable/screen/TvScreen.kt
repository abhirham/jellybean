package org.jellyfin.androidtv.ui.composable.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jellyfin.androidtv.ui.composable.theme.LocalTvPadding

/**
 * Base TV Screen Wrapper
 *
 * Provides consistent screen structure for all TV screens.
 * Handles safe area padding and common screen patterns.
 *
 * @param modifier Modifier for the screen container
 * @param applySafeArea Whether to apply TV safe area padding
 * @param content Screen content
 */
@Composable
fun TvScreen(
	modifier: Modifier = Modifier,
	applySafeArea: Boolean = true,
	content: @Composable () -> Unit
) {
	val padding = LocalTvPadding.current

	Box(
		modifier = modifier
			.fillMaxSize()
			.then(
				if (applySafeArea) {
					Modifier.padding(
						horizontal = padding.screenHorizontal,
						vertical = padding.screenVertical
					)
				} else {
					Modifier
				}
			)
	) {
		content()
	}
}

/**
 * Full-screen TV Screen (no safe area padding)
 *
 * For screens that need edge-to-edge content like video playback or hero banners.
 *
 * @param modifier Modifier for the screen container
 * @param content Screen content
 */
@Composable
fun TvFullScreen(
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit
) {
	TvScreen(
		modifier = modifier,
		applySafeArea = false,
		content = content
	)
}
