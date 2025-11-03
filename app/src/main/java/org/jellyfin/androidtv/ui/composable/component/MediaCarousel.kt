package org.jellyfin.androidtv.ui.composable.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import org.jellyfin.androidtv.ui.composable.theme.LocalTvPadding

/**
 * Media Carousel Component
 *
 * Horizontal scrolling carousel for displaying media items.
 * Netflix-style row with section title and horizontal scrolling content.
 *
 * @param title Title of the carousel section
 * @param items List of items to display
 * @param onSeeAllClick Optional callback when "See All" is clicked
 * @param modifier Modifier for the carousel container
 * @param itemContent Composable for rendering each item
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun <T> MediaCarousel(
	title: String,
	items: List<T>,
	onSeeAllClick: (() -> Unit)? = null,
	modifier: Modifier = Modifier,
	itemContent: @Composable (T) -> Unit
) {
	val padding = LocalTvPadding.current

	Column(
		modifier = modifier.fillMaxWidth()
	) {
		// Header Row
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = padding.screenHorizontal),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = title,
				style = MaterialTheme.typography.headlineMedium,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)

			if (onSeeAllClick != null) {
				Text(
					text = "See All ›",
					style = MaterialTheme.typography.bodyLarge,
					color = MaterialTheme.colorScheme.primary,
					modifier = Modifier.clickable(onClick = onSeeAllClick)
				)
			}
		}

		Spacer(modifier = Modifier.height(padding.medium))

		// Horizontal Scrolling List
		LazyRow(
			contentPadding = PaddingValues(horizontal = padding.screenHorizontal),
			horizontalArrangement = Arrangement.spacedBy(padding.carouselItemSpacing)
		) {
			items(items) { item ->
				itemContent(item)
			}
		}
	}
}

/**
 * Media Carousel with Index Focus
 *
 * Carousel that supports programmatic focus on specific items.
 * Useful for maintaining focus state across screen transitions.
 *
 * @param title Title of the carousel section
 * @param items List of items to display
 * @param focusedIndex Optional index to focus on initially
 * @param onSeeAllClick Optional callback when "See All" is clicked
 * @param onItemFocused Callback when an item receives focus
 * @param modifier Modifier for the carousel container
 * @param itemContent Composable for rendering each item with focus requester
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun <T> MediaCarouselWithFocus(
	title: String,
	items: List<T>,
	focusedIndex: Int? = null,
	onSeeAllClick: (() -> Unit)? = null,
	onItemFocused: ((Int) -> Unit)? = null,
	modifier: Modifier = Modifier,
	itemContent: @Composable (T, FocusRequester) -> Unit
) {
	val padding = LocalTvPadding.current

	Column(
		modifier = modifier.fillMaxWidth()
	) {
		// Header Row
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = padding.screenHorizontal),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = title,
				style = MaterialTheme.typography.headlineMedium,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)

			if (onSeeAllClick != null) {
				Text(
					text = "See All ›",
					style = MaterialTheme.typography.bodyLarge,
					color = MaterialTheme.colorScheme.primary,
					modifier = Modifier.clickable(onClick = onSeeAllClick)
				)
			}
		}

		Spacer(modifier = Modifier.height(padding.medium))

		// Horizontal Scrolling List
		LazyRow(
			contentPadding = PaddingValues(horizontal = padding.screenHorizontal),
			horizontalArrangement = Arrangement.spacedBy(padding.carouselItemSpacing)
		) {
			itemsIndexed(items) { index, item ->
				val focusRequester = FocusRequester()

				itemContent(item, focusRequester)
			}
		}
	}
}

/**
 * Continue Watching Carousel
 *
 * Specialized carousel for continue watching items.
 * Uses landscape cards with progress indicators.
 */
@Composable
fun ContinueWatchingCarousel(
	items: List<ContinueWatchingItem>,
	onItemClick: (ContinueWatchingItem) -> Unit,
	onSeeAllClick: (() -> Unit)? = null,
	modifier: Modifier = Modifier
) {
	MediaCarousel(
		title = "Continue Watching",
		items = items,
		onSeeAllClick = onSeeAllClick,
		modifier = modifier
	) { item ->
		LandscapeMediaCard(
			title = item.title,
			imageUrl = item.imageUrl,
			subtitle = item.subtitle,
			progress = item.progress,
			onClick = { onItemClick(item) }
		)
	}
}

/**
 * Latest Media Carousel
 *
 * Specialized carousel for latest/new content.
 * Uses portrait poster cards.
 */
@Composable
fun LatestMediaCarousel(
	title: String = "Latest",
	items: List<MediaItem>,
	onItemClick: (MediaItem) -> Unit,
	onSeeAllClick: (() -> Unit)? = null,
	modifier: Modifier = Modifier
) {
	MediaCarousel(
		title = title,
		items = items,
		onSeeAllClick = onSeeAllClick,
		modifier = modifier
	) { item ->
		PortraitMediaCard(
			title = item.title,
			imageUrl = item.imageUrl,
			subtitle = item.subtitle,
			isWatched = item.isWatched,
			onClick = { onItemClick(item) }
		)
	}
}

/**
 * Next Up Carousel
 *
 * Specialized carousel for next episodes/items to watch.
 * Uses landscape cards with episode information.
 */
@Composable
fun NextUpCarousel(
	items: List<NextUpItem>,
	onItemClick: (NextUpItem) -> Unit,
	onSeeAllClick: (() -> Unit)? = null,
	modifier: Modifier = Modifier
) {
	MediaCarousel(
		title = "Next Up",
		items = items,
		onSeeAllClick = onSeeAllClick,
		modifier = modifier
	) { item ->
		LandscapeMediaCard(
			title = item.title,
			imageUrl = item.imageUrl,
			subtitle = item.episodeInfo,
			onClick = { onItemClick(item) }
		)
	}
}

/**
 * Data classes for carousel items
 */

/**
 * Continue Watching Item
 *
 * Represents an item with playback progress.
 */
data class ContinueWatchingItem(
	val id: String,
	val title: String,
	val subtitle: String? = null,
	val imageUrl: String? = null,
	val progress: Float = 0f
)

/**
 * Media Item
 *
 * Generic media item for carousels.
 */
data class MediaItem(
	val id: String,
	val title: String,
	val subtitle: String? = null,
	val imageUrl: String? = null,
	val isWatched: Boolean = false
)

/**
 * Next Up Item
 *
 * Represents next episode or item to watch.
 */
data class NextUpItem(
	val id: String,
	val title: String,
	val episodeInfo: String? = null,
	val imageUrl: String? = null,
	val seriesId: String? = null
)
