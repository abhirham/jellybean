package org.jellyfin.androidtv.ui.composable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import org.jellyfin.androidtv.ui.composable.AsyncImage
import org.jellyfin.androidtv.ui.composable.theme.JellyfinColors

/**
 * Backdrop Component
 *
 * Full-screen or partial backdrop image with gradient overlay.
 * Used for hero banners, detail screens, and background visuals.
 *
 * @param imageUrl URL of the backdrop image
 * @param contentDescription Accessibility description
 * @param gradientType Type of gradient overlay to apply
 * @param modifier Modifier for the backdrop container
 */
@Composable
fun Backdrop(
	imageUrl: String?,
	contentDescription: String? = null,
	gradientType: BackdropGradient = BackdropGradient.BottomToTop,
	modifier: Modifier = Modifier
) {
	Box(modifier = modifier) {
		// Background Image
		if (imageUrl != null) {
			AsyncImage(
				url = imageUrl,
				modifier = Modifier.fillMaxSize(),
				scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
			)
		}

		// Gradient Overlay
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(gradientType.brush)
		)
	}
}

/**
 * Backdrop Gradient Types
 *
 * Predefined gradient overlays for backdrops.
 */
sealed class BackdropGradient(val brush: Brush) {
	/**
	 * Gradient from top (transparent) to bottom (dark)
	 * Use for hero banners with content at bottom
	 */
	object BottomToTop : BackdropGradient(
		Brush.verticalGradient(
			colors = listOf(
				JellyfinColors.heroGradientTop,
				JellyfinColors.heroGradientBottom
			),
			startY = 0f,
			endY = 1200f
		)
	)

	/**
	 * Gradient from left (transparent) to right (dark)
	 * Use for detail screens with content on right
	 */
	object RightToLeft : BackdropGradient(
		Brush.horizontalGradient(
			colors = listOf(
				Color.Transparent,
				JellyfinColors.heroGradientBottom
			),
			startX = 0f,
			endX = 1200f
		)
	)

	/**
	 * Radial gradient from center (transparent) to edges (dark)
	 * Use for centered content with backdrop
	 */
	object CenterOut : BackdropGradient(
		Brush.radialGradient(
			colors = listOf(
				Color.Transparent,
				JellyfinColors.heroGradientBottom
			),
			radius = 800f
		)
	)

	/**
	 * Heavy gradient for better text readability
	 * Use when content needs high contrast
	 */
	object Heavy : BackdropGradient(
		Brush.verticalGradient(
			colors = listOf(
				Color.Black.copy(alpha = 0.3f),
				Color.Black.copy(alpha = 0.9f)
			),
			startY = 0f,
			endY = 1000f
		)
	)

	/**
	 * Light gradient for subtle effect
	 * Use when backdrop should be more visible
	 */
	object Light : BackdropGradient(
		Brush.verticalGradient(
			colors = listOf(
				Color.Transparent,
				Color.Black.copy(alpha = 0.5f)
			),
			startY = 0f,
			endY = 1200f
		)
	)

	/**
	 * No gradient overlay
	 * Use when image should be fully visible
	 */
	object None : BackdropGradient(
		Brush.verticalGradient(
			colors = listOf(Color.Transparent, Color.Transparent)
		)
	)

	/**
	 * Custom gradient
	 *
	 * @param colors List of colors for the gradient
	 * @param startY Start Y position (vertical gradient)
	 * @param endY End Y position (vertical gradient)
	 */
	class Custom(
		colors: List<Color>,
		startY: Float = 0f,
		endY: Float = 1200f
	) : BackdropGradient(
		Brush.verticalGradient(
			colors = colors,
			startY = startY,
			endY = endY
		)
	)
}

/**
 * Scrim Overlay
 *
 * Semi-transparent overlay for dimming content.
 * Used in modals, dialogs, and drawers.
 *
 * @param alpha Opacity of the scrim (0.0 - 1.0)
 * @param color Color of the scrim
 * @param modifier Modifier for the scrim container
 */
@Composable
fun Scrim(
	alpha: Float = 0.75f,
	color: Color = Color.Black,
	modifier: Modifier = Modifier
) {
	Box(
		modifier = modifier
			.fillMaxSize()
			.background(color.copy(alpha = alpha.coerceIn(0f, 1f)))
	)
}

/**
 * Blurred Backdrop
 *
 * Backdrop with blur effect (requires RenderScript or custom blur implementation).
 * Note: This is a placeholder - actual blur implementation requires additional setup.
 *
 * @param imageUrl URL of the backdrop image
 * @param contentDescription Accessibility description
 * @param blurRadius Blur radius in pixels
 * @param modifier Modifier for the backdrop container
 */
@Composable
fun BlurredBackdrop(
	imageUrl: String?,
	contentDescription: String? = null,
	blurRadius: Float = 25f,
	modifier: Modifier = Modifier
) {
	// TODO: Implement actual blur effect
	// For now, just render the backdrop with a scrim
	Box(modifier = modifier) {
		Backdrop(
			imageUrl = imageUrl,
			contentDescription = contentDescription,
			gradientType = BackdropGradient.None
		)
		Scrim(alpha = 0.5f)
	}
}
