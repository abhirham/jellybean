package org.jellyfin.androidtv.ui.composable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Text
import org.jellyfin.androidtv.ui.composable.AsyncImage
import org.jellyfin.androidtv.ui.composable.theme.JellyfinColors
import org.jellyfin.androidtv.ui.composable.theme.LocalTvPadding

/**
 * Hero Banner Component
 *
 * Netflix-style large featured content banner for the home screen.
 * Displays backdrop image with metadata and action buttons.
 *
 * @param title Primary title of the featured content
 * @param description Content description/overview
 * @param backdropUrl URL for the backdrop image
 * @param logoUrl Optional logo image URL (replaces title text if provided)
 * @param metadata Additional metadata (e.g., "2024 • 2h 15m • Drama")
 * @param onPlayClick Callback when Play button is clicked
 * @param onInfoClick Callback when More Info button is clicked
 * @param modifier Modifier for the banner container
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HeroBanner(
	title: String,
	description: String,
	backdropUrl: String? = null,
	logoUrl: String? = null,
	metadata: String? = null,
	onPlayClick: () -> Unit = {},
	onInfoClick: () -> Unit = {},
	modifier: Modifier = Modifier
) {
	val padding = LocalTvPadding.current

	Box(
		modifier = modifier
			.fillMaxWidth()
			.height(720.dp)
	) {
		// Backdrop Image
		if (backdropUrl != null) {
			AsyncImage(
				url = backdropUrl,
				modifier = Modifier.fillMaxSize(),
				scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
			)
		}

		// Gradient Overlay (top to bottom)
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(
					Brush.verticalGradient(
						colors = listOf(
							JellyfinColors.heroGradientTop,
							JellyfinColors.heroGradientBottom
						),
						startY = 0f,
						endY = 1200f
					)
				)
		)

		// Content
		Column(
			modifier = Modifier
				.align(Alignment.BottomStart)
				.padding(
					start = padding.screenHorizontal,
					end = padding.screenHorizontal,
					bottom = padding.heroBottomPadding
				)
				.fillMaxWidth(0.5f),
			verticalArrangement = Arrangement.spacedBy(padding.medium)
		) {
			// Logo or Title
			if (logoUrl != null) {
				AsyncImage(
					url = logoUrl,
					modifier = Modifier
						.height(120.dp)
						.width(300.dp),
					scaleType = android.widget.ImageView.ScaleType.FIT_CENTER
				)
			} else {
				Text(
					text = title,
					style = MaterialTheme.typography.displayLarge,
					maxLines = 2,
					overflow = TextOverflow.Ellipsis
				)
			}

			// Metadata
			if (metadata != null) {
				Text(
					text = metadata,
					style = MaterialTheme.typography.bodyLarge,
					color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
				)
			}

			// Description
			Text(
				text = description,
				style = MaterialTheme.typography.bodyLarge,
				maxLines = 3,
				overflow = TextOverflow.Ellipsis,
				color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
			)

			Spacer(modifier = Modifier.height(padding.medium))

			// Action Buttons
			Row(
				horizontalArrangement = Arrangement.spacedBy(padding.medium)
			) {
				Button(
					onClick = onPlayClick
				) {
					Text(text = "▶ Play")
				}

				OutlinedButton(
					onClick = onInfoClick
				) {
					Text(text = "ⓘ More Info")
				}
			}
		}
	}
}

/**
 * Hero Banner with Continue Watching
 *
 * Variant for items with playback progress.
 *
 * @param title Primary title of the featured content
 * @param description Content description/overview
 * @param backdropUrl URL for the backdrop image
 * @param logoUrl Optional logo image URL
 * @param metadata Additional metadata
 * @param playbackProgress Progress percentage (0.0 - 1.0)
 * @param playbackText Text to display for progress (e.g., "Continue S1:E5")
 * @param onResumeClick Callback when Resume/Continue button is clicked
 * @param onRestartClick Callback when Restart button is clicked
 * @param onInfoClick Callback when More Info button is clicked
 * @param modifier Modifier for the banner container
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HeroBannerWithProgress(
	title: String,
	description: String,
	backdropUrl: String? = null,
	logoUrl: String? = null,
	metadata: String? = null,
	playbackProgress: Float = 0f,
	playbackText: String = "Continue",
	onResumeClick: () -> Unit = {},
	onRestartClick: () -> Unit = {},
	onInfoClick: () -> Unit = {},
	modifier: Modifier = Modifier
) {
	val padding = LocalTvPadding.current

	Box(
		modifier = modifier
			.fillMaxWidth()
			.height(720.dp)
	) {
		// Backdrop Image
		if (backdropUrl != null) {
			AsyncImage(
				url = backdropUrl,
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
							JellyfinColors.heroGradientTop,
							JellyfinColors.heroGradientBottom
						),
						startY = 0f,
						endY = 1200f
					)
				)
		)

		// Content
		Column(
			modifier = Modifier
				.align(Alignment.BottomStart)
				.padding(
					start = padding.screenHorizontal,
					end = padding.screenHorizontal,
					bottom = padding.heroBottomPadding
				)
				.fillMaxWidth(0.5f),
			verticalArrangement = Arrangement.spacedBy(padding.medium)
		) {
			// Logo or Title
			if (logoUrl != null) {
				AsyncImage(
					url = logoUrl,
					modifier = Modifier
						.height(120.dp)
						.width(300.dp),
					scaleType = android.widget.ImageView.ScaleType.FIT_CENTER
				)
			} else {
				Text(
					text = title,
					style = MaterialTheme.typography.displayLarge,
					maxLines = 2,
					overflow = TextOverflow.Ellipsis
				)
			}

			// Metadata
			if (metadata != null) {
				Text(
					text = metadata,
					style = MaterialTheme.typography.bodyLarge,
					color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
				)
			}

			// Description
			Text(
				text = description,
				style = MaterialTheme.typography.bodyLarge,
				maxLines = 3,
				overflow = TextOverflow.Ellipsis,
				color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
			)

			// Progress Bar
			if (playbackProgress > 0) {
				ProgressBar(
					progress = playbackProgress,
					modifier = Modifier
						.fillMaxWidth()
						.height(4.dp)
				)
			}

			Spacer(modifier = Modifier.height(padding.medium))

			// Action Buttons
			Row(
				horizontalArrangement = Arrangement.spacedBy(padding.medium)
			) {
				Button(
					onClick = onResumeClick
				) {
					Text(text = "▶ $playbackText")
				}

				OutlinedButton(
					onClick = onRestartClick
				) {
					Text(text = "↻ Restart")
				}

				OutlinedButton(
					onClick = onInfoClick
				) {
					Text(text = "ⓘ More Info")
				}
			}
		}
	}
}

/**
 * Simple Progress Bar
 *
 * Horizontal progress indicator.
 *
 * @param progress Progress value (0.0 - 1.0)
 * @param modifier Modifier for the progress bar
 * @param color Color of the progress bar
 * @param backgroundColor Background color of the progress bar
 */
@Composable
private fun ProgressBar(
	progress: Float,
	modifier: Modifier = Modifier,
	color: Color = JellyfinColors.inProgress,
	backgroundColor: Color = Color.White.copy(alpha = 0.3f)
) {
	Box(
		modifier = modifier.background(backgroundColor)
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth(progress.coerceIn(0f, 1f))
				.height(4.dp)
				.background(color)
		)
	}
}
