package org.jellyfin.androidtv.ui.composable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import org.jellyfin.androidtv.ui.composable.AsyncImage
import org.jellyfin.androidtv.ui.composable.focus.tvFocusEffect
import org.jellyfin.androidtv.ui.composable.theme.JellyfinColors
import org.jellyfin.androidtv.ui.composable.theme.LocalTvPadding

/**
 * Media Card Component
 *
 * Card component for displaying media items in carousels.
 * Supports both portrait (posters) and landscape (backdrops) aspect ratios.
 *
 * @param title Title of the media item
 * @param imageUrl URL for the card image (poster or backdrop)
 * @param aspectRatio Aspect ratio of the card (e.g., 2/3 for poster, 16/9 for backdrop)
 * @param subtitle Optional subtitle (e.g., "2024 • Drama")
 * @param progress Optional playback progress (0.0 - 1.0)
 * @param isWatched Whether the item has been watched
 * @param onClick Callback when card is clicked
 * @param modifier Modifier for the card container
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MediaCard(
	title: String,
	imageUrl: String? = null,
	aspectRatio: Float = MediaCardDefaults.PosterAspectRatio,
	subtitle: String? = null,
	progress: Float? = null,
	isWatched: Boolean = false,
	onClick: () -> Unit = {},
	modifier: Modifier = Modifier
) {
	val padding = LocalTvPadding.current

	Column(
		modifier = modifier
			.width(MediaCardDefaults.PosterWidth)
			.clickable(onClick = onClick)
			.tvFocusEffect()
	) {
		// Card Image
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.aspectRatio(aspectRatio)
				.clip(RoundedCornerShape(8.dp))
				.background(MaterialTheme.colorScheme.surfaceVariant)
		) {
			if (imageUrl != null) {
				AsyncImage(
					url = imageUrl,
					modifier = Modifier.fillMaxSize(),
					scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
				)
			}

			// Progress Indicator
			if (progress != null && progress > 0) {
				Box(
					modifier = Modifier
						.align(Alignment.BottomCenter)
						.fillMaxWidth()
						.height(4.dp)
						.background(Color.White.copy(alpha = 0.3f))
				) {
					Box(
						modifier = Modifier
							.fillMaxWidth(progress.coerceIn(0f, 1f))
							.height(4.dp)
							.background(JellyfinColors.inProgress)
					)
				}
			}

			// Watched Indicator
			if (isWatched) {
				Box(
					modifier = Modifier
						.align(Alignment.TopEnd)
						.padding(8.dp)
						.size(24.dp)
						.clip(CircleShape)
						.background(JellyfinColors.watched)
				) {
					Text(
						text = "✓",
						style = MaterialTheme.typography.labelSmall,
						color = Color.White,
						modifier = Modifier.align(Alignment.Center)
					)
				}
			}
		}

		Spacer(modifier = Modifier.height(padding.small))

		// Title
		Text(
			text = title,
			style = MaterialTheme.typography.bodyMedium,
			maxLines = 1,
			overflow = TextOverflow.Ellipsis
		)

		// Subtitle
		if (subtitle != null) {
			Text(
				text = subtitle,
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)
		}
	}
}

/**
 * Landscape Media Card
 *
 * Specialized card for landscape/backdrop images (16:9 aspect ratio).
 *
 * @param title Title of the media item
 * @param imageUrl URL for the backdrop image
 * @param subtitle Optional subtitle
 * @param progress Optional playback progress
 * @param isWatched Whether the item has been watched
 * @param onClick Callback when card is clicked
 * @param modifier Modifier for the card container
 */
@Composable
fun LandscapeMediaCard(
	title: String,
	imageUrl: String? = null,
	subtitle: String? = null,
	progress: Float? = null,
	isWatched: Boolean = false,
	onClick: () -> Unit = {},
	modifier: Modifier = Modifier
) {
	MediaCard(
		title = title,
		imageUrl = imageUrl,
		aspectRatio = MediaCardDefaults.BackdropAspectRatio,
		subtitle = subtitle,
		progress = progress,
		isWatched = isWatched,
		onClick = onClick,
		modifier = modifier.width(MediaCardDefaults.BackdropWidth)
	)
}

/**
 * Portrait Media Card
 *
 * Specialized card for portrait/poster images (2:3 aspect ratio).
 *
 * @param title Title of the media item
 * @param imageUrl URL for the poster image
 * @param subtitle Optional subtitle
 * @param progress Optional playback progress
 * @param isWatched Whether the item has been watched
 * @param onClick Callback when card is clicked
 * @param modifier Modifier for the card container
 */
@Composable
fun PortraitMediaCard(
	title: String,
	imageUrl: String? = null,
	subtitle: String? = null,
	progress: Float? = null,
	isWatched: Boolean = false,
	onClick: () -> Unit = {},
	modifier: Modifier = Modifier
) {
	MediaCard(
		title = title,
		imageUrl = imageUrl,
		aspectRatio = MediaCardDefaults.PosterAspectRatio,
		subtitle = subtitle,
		progress = progress,
		isWatched = isWatched,
		onClick = onClick,
		modifier = modifier.width(MediaCardDefaults.PosterWidth)
	)
}

/**
 * Square Media Card
 *
 * Specialized card for square images (1:1 aspect ratio).
 * Useful for albums, artists, or profile pictures.
 *
 * @param title Title of the media item
 * @param imageUrl URL for the square image
 * @param subtitle Optional subtitle
 * @param onClick Callback when card is clicked
 * @param modifier Modifier for the card container
 */
@Composable
fun SquareMediaCard(
	title: String,
	imageUrl: String? = null,
	subtitle: String? = null,
	onClick: () -> Unit = {},
	modifier: Modifier = Modifier
) {
	MediaCard(
		title = title,
		imageUrl = imageUrl,
		aspectRatio = 1f,
		subtitle = subtitle,
		onClick = onClick,
		modifier = modifier.width(MediaCardDefaults.SquareWidth)
	)
}

/**
 * Media Card with Overlay
 *
 * Card with text overlay on the image (Netflix-style).
 * Title and metadata appear over a gradient overlay on the image.
 *
 * @param title Title of the media item
 * @param imageUrl URL for the card image
 * @param aspectRatio Aspect ratio of the card
 * @param metadata Optional metadata text (e.g., "2024 • 1h 45m")
 * @param onClick Callback when card is clicked
 * @param modifier Modifier for the card container
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MediaCardWithOverlay(
	title: String,
	imageUrl: String? = null,
	aspectRatio: Float = MediaCardDefaults.BackdropAspectRatio,
	metadata: String? = null,
	onClick: () -> Unit = {},
	modifier: Modifier = Modifier
) {
	Box(
		modifier = modifier
			.width(MediaCardDefaults.BackdropWidth)
			.aspectRatio(aspectRatio)
			.clip(RoundedCornerShape(8.dp))
			.background(MaterialTheme.colorScheme.surfaceVariant)
			.clickable(onClick = onClick)
			.tvFocusEffect()
	) {
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
				.background(
					Brush.verticalGradient(
						colors = listOf(
							Color.Transparent,
							JellyfinColors.cardGradientBottom
						),
						startY = 0f,
						endY = 600f
					)
				)
		)

		// Text Content
		Column(
			modifier = Modifier
				.align(Alignment.BottomStart)
				.padding(12.dp)
		) {
			Text(
				text = title,
				style = MaterialTheme.typography.bodyLarge,
				maxLines = 2,
				overflow = TextOverflow.Ellipsis
			)

			if (metadata != null) {
				Text(
					text = metadata,
					style = MaterialTheme.typography.bodySmall,
					color = Color.White.copy(alpha = 0.8f),
					maxLines = 1,
					overflow = TextOverflow.Ellipsis
				)
			}
		}
	}
}

/**
 * Default values for Media Cards
 */
object MediaCardDefaults {
	// Aspect Ratios
	const val PosterAspectRatio = 2f / 3f // Portrait
	const val BackdropAspectRatio = 16f / 9f // Landscape
	const val SquareAspectRatio = 1f // Square

	// Widths (TV-optimized)
	val PosterWidth: Dp = 200.dp
	val BackdropWidth: Dp = 360.dp
	val SquareWidth: Dp = 180.dp

	// Heights (calculated from aspect ratios)
	val PosterHeight: Dp = (PosterWidth.value / PosterAspectRatio).dp
	val BackdropHeight: Dp = (BackdropWidth.value / BackdropAspectRatio).dp
	val SquareHeight: Dp = SquareWidth
}
