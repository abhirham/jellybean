package org.jellyfin.androidtv.ui.composable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import org.jellyfin.androidtv.ui.composable.theme.JellyfinColors

/**
 * Metadata Display Components
 *
 * Reusable components for displaying media metadata (ratings, year, runtime, etc.)
 */

/**
 * Metadata Row
 *
 * Displays metadata items separated by dots.
 * Example: "2024 · 2h 15m · Drama · PG-13"
 *
 * @param items List of metadata items to display
 * @param modifier Modifier for the row container
 * @param separator Separator character between items
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MetadataRow(
	items: List<String>,
	modifier: Modifier = Modifier,
	separator: String = "·"
) {
	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		items.forEachIndexed { index, item ->
			Text(
				text = item,
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
			)

			if (index < items.size - 1) {
				Text(
					text = separator,
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
				)
			}
		}
	}
}

/**
 * Content Rating Badge
 *
 * Displays content rating (G, PG, PG-13, R, etc.) in a colored badge.
 *
 * @param rating Content rating string
 * @param modifier Modifier for the badge container
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ContentRatingBadge(
	rating: String,
	modifier: Modifier = Modifier
) {
	val backgroundColor = when (rating.uppercase()) {
		"G", "TV-G" -> JellyfinColors.ratingG
		"PG", "TV-PG" -> JellyfinColors.ratingPG
		"PG-13", "TV-14" -> JellyfinColors.ratingPG13
		"R", "TV-MA" -> JellyfinColors.ratingR
		else -> Color.Gray
	}

	Box(
		modifier = modifier
			.background(
				color = backgroundColor,
				shape = RoundedCornerShape(4.dp)
			)
			.padding(horizontal = 8.dp, vertical = 4.dp)
	) {
		Text(
			text = rating,
			style = MaterialTheme.typography.labelMedium,
			fontWeight = FontWeight.Bold,
			color = Color.White
		)
	}
}

/**
 * Star Rating
 *
 * Displays star rating with text (e.g., "★ 8.5").
 *
 * @param rating Rating value (0.0 - 10.0)
 * @param maxRating Maximum rating value (default 10.0)
 * @param modifier Modifier for the rating container
 * @param showOutOf Whether to show "out of" text (e.g., "8.5/10")
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun StarRating(
	rating: Float,
	maxRating: Float = 10f,
	modifier: Modifier = Modifier,
	showOutOf: Boolean = false
) {
	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.spacedBy(4.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = "★",
			style = MaterialTheme.typography.bodyMedium,
			color = JellyfinColors.ratingPG13
		)

		val ratingText = if (showOutOf) {
			String.format("%.1f/%.0f", rating, maxRating)
		} else {
			String.format("%.1f", rating)
		}

		Text(
			text = ratingText,
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurface
		)
	}
}

/**
 * Runtime Display
 *
 * Displays runtime in human-readable format.
 *
 * @param runtimeMinutes Runtime in minutes
 * @param modifier Modifier for the runtime display
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun RuntimeDisplay(
	runtimeMinutes: Int,
	modifier: Modifier = Modifier
) {
	val hours = runtimeMinutes / 60
	val minutes = runtimeMinutes % 60

	val runtimeText = when {
		hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
		hours > 0 -> "${hours}h"
		else -> "${minutes}m"
	}

	Text(
		text = runtimeText,
		style = MaterialTheme.typography.bodyMedium,
		color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
		modifier = modifier
	)
}

/**
 * Episode Info
 *
 * Displays episode information (e.g., "S1:E5 - Episode Title").
 *
 * @param seasonNumber Season number
 * @param episodeNumber Episode number
 * @param episodeTitle Optional episode title
 * @param modifier Modifier for the info display
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun EpisodeInfo(
	seasonNumber: Int,
	episodeNumber: Int,
	episodeTitle: String? = null,
	modifier: Modifier = Modifier
) {
	val episodeText = buildString {
		append("S${seasonNumber}:E${episodeNumber}")
		if (episodeTitle != null) {
			append(" - $episodeTitle")
		}
	}

	Text(
		text = episodeText,
		style = MaterialTheme.typography.bodyMedium,
		color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
		modifier = modifier
	)
}

/**
 * Genre Tags
 *
 * Displays genre tags as pills/chips.
 *
 * @param genres List of genre names
 * @param modifier Modifier for the tags container
 * @param maxVisible Maximum number of genres to show
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun GenreTags(
	genres: List<String>,
	modifier: Modifier = Modifier,
	maxVisible: Int = 3
) {
	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.spacedBy(8.dp)
	) {
		genres.take(maxVisible).forEach { genre ->
			Box(
				modifier = Modifier
					.background(
						color = MaterialTheme.colorScheme.surfaceVariant,
						shape = RoundedCornerShape(16.dp)
					)
					.padding(horizontal = 12.dp, vertical = 6.dp)
			) {
				Text(
					text = genre,
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		}

		if (genres.size > maxVisible) {
			Box(
				modifier = Modifier
					.background(
						color = MaterialTheme.colorScheme.surfaceVariant,
						shape = RoundedCornerShape(16.dp)
					)
					.padding(horizontal = 12.dp, vertical = 6.dp)
			) {
				Text(
					text = "+${genres.size - maxVisible}",
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		}
	}
}

/**
 * Live Indicator
 *
 * Displays "LIVE" badge for live TV content.
 *
 * @param modifier Modifier for the badge container
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun LiveIndicator(
	modifier: Modifier = Modifier
) {
	Box(
		modifier = modifier
			.background(
				color = JellyfinColors.live,
				shape = RoundedCornerShape(4.dp)
			)
			.padding(horizontal = 8.dp, vertical = 4.dp)
	) {
		Text(
			text = "● LIVE",
			style = MaterialTheme.typography.labelMedium,
			fontWeight = FontWeight.Bold,
			color = Color.White
		)
	}
}

/**
 * Recording Indicator
 *
 * Displays "REC" badge for recording content.
 *
 * @param modifier Modifier for the badge container
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun RecordingIndicator(
	modifier: Modifier = Modifier
) {
	Box(
		modifier = modifier
			.background(
				color = JellyfinColors.recording,
				shape = RoundedCornerShape(4.dp)
			)
			.padding(horizontal = 8.dp, vertical = 4.dp)
	) {
		Text(
			text = "● REC",
			style = MaterialTheme.typography.labelMedium,
			fontWeight = FontWeight.Bold,
			color = Color.White
		)
	}
}

/**
 * Year Badge
 *
 * Displays release year in a subtle badge.
 *
 * @param year Release year
 * @param modifier Modifier for the badge container
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun YearBadge(
	year: Int,
	modifier: Modifier = Modifier
) {
	Text(
		text = year.toString(),
		style = MaterialTheme.typography.bodyMedium,
		color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
		modifier = modifier
	)
}

/**
 * Helper function to format metadata items
 */
object MetadataFormatter {
	/**
	 * Format runtime from minutes to "Xh Ym" string
	 */
	fun formatRuntime(minutes: Int): String {
		val hours = minutes / 60
		val mins = minutes % 60

		return when {
			hours > 0 && mins > 0 -> "${hours}h ${mins}m"
			hours > 0 -> "${hours}h"
			else -> "${mins}m"
		}
	}

	/**
	 * Format episode designation "SX:EY"
	 */
	fun formatEpisode(season: Int, episode: Int): String {
		return "S${season}:E${episode}"
	}

	/**
	 * Create metadata items list from common properties
	 */
	fun createMetadataList(
		year: Int? = null,
		runtime: Int? = null,
		genres: List<String>? = null,
		rating: String? = null
	): List<String> {
		return buildList {
			year?.let { add(it.toString()) }
			runtime?.let { add(formatRuntime(it)) }
			genres?.firstOrNull()?.let { add(it) }
			rating?.let { add(it) }
		}
	}
}
