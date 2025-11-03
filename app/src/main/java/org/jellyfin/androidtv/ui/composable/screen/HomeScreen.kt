package org.jellyfin.androidtv.ui.composable.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jellyfin.androidtv.ui.composable.component.ContinueWatchingCarousel
import org.jellyfin.androidtv.ui.composable.component.ContinueWatchingItem
import org.jellyfin.androidtv.ui.composable.component.HeroBanner
import org.jellyfin.androidtv.ui.composable.component.HeroBannerWithProgress
import org.jellyfin.androidtv.ui.composable.component.LatestMediaCarousel
import org.jellyfin.androidtv.ui.composable.component.MediaItem
import org.jellyfin.androidtv.ui.composable.component.NavigationDrawer
import org.jellyfin.androidtv.ui.composable.component.NavigationItem
import org.jellyfin.androidtv.ui.composable.component.NextUpCarousel
import org.jellyfin.androidtv.ui.composable.component.NextUpItem
import org.jellyfin.androidtv.ui.composable.component.UserProfile
import org.jellyfin.androidtv.ui.composable.theme.JellyfinTvTheme
import org.jellyfin.androidtv.ui.composable.theme.LocalTvPadding
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.ImageType
import org.koin.androidx.compose.koinViewModel

/**
 * Netflix-style home screen for Jellyfin Android TV.
 * Features hero banner, navigation drawer, and multiple content carousels.
 */
@Composable
fun HomeScreen(
	viewModel: HomeViewModel = koinViewModel(),
	onNavigate: (String) -> Unit = {},
	onItemClick: (BaseItemDto) -> Unit = {},
) {
	val uiState by viewModel.uiState.collectAsState()
	var drawerOpen by remember { mutableStateOf(false) }

	JellyfinTvTheme {
		Box(modifier = Modifier.fillMaxSize()) {
			// Main content
			HomeScreenContent(
				uiState = uiState,
				onItemClick = { homeItem ->
					// Pass the underlying BaseItemDto
					onItemClick(homeItem.item)
				},
				onPlayClick = { homeItem ->
					// Handle play action
					onItemClick(homeItem.item)
				},
				onInfoClick = { homeItem ->
					// Navigate to detail screen
					onItemClick(homeItem.item)
				},
				onRefresh = viewModel::refresh,
			)

			// Navigation drawer overlay
			NavigationDrawer(
				visible = drawerOpen,
				selectedItem = "home",
				items = getDefaultNavigationItems(),
				userProfile = UserProfile(
					id = "user-1", // TODO: Get from user repository
					name = "User",
					avatarUrl = null
				),
				onItemClick = { item ->
					drawerOpen = false
					onNavigate(item.id)
				},
				onDismiss = { drawerOpen = false }
			)
		}
	}
}

@Composable
private fun HomeScreenContent(
	uiState: HomeScreenUiState,
	onItemClick: (HomeItemWithImages) -> Unit,
	onPlayClick: (HomeItemWithImages) -> Unit,
	onInfoClick: (HomeItemWithImages) -> Unit,
	onRefresh: () -> Unit,
) {
	val padding = LocalTvPadding.current

	when {
		uiState.isLoading && uiState.featuredItem == null -> {
			// Initial loading state
			LoadingState(
				message = "Loading your content..."
			)
		}

		uiState.error != null -> {
			// Error state
			ErrorState(
				message = uiState.error,
				onRetry = onRefresh
			)
		}

		else -> {
			// Check if we have any content to show
			val hasContent = uiState.featuredItem != null ||
				uiState.resumeItems.isNotEmpty() ||
				uiState.latestItems.isNotEmpty() ||
				uiState.nextUpItems.isNotEmpty()

			if (!hasContent) {
				// Show empty state
				EmptyState(
					title = "No Content Available",
					message = "Connect to a Jellyfin server with media to get started"
				)
			} else {
				// Content loaded - Use regular Column with scroll for now
				Column(
					modifier = Modifier
						.fillMaxSize()
						.verticalScroll(rememberScrollState())
				) {
					// Hero Banner Section
					val featuredItem = uiState.featuredItem
					val resumeItem = uiState.resumeItems.firstOrNull()

				when {
					// Show resume banner if user has something in progress
					resumeItem != null && resumeItem.item.userData?.playedPercentage != null -> {
						HeroBannerWithProgress(
							title = resumeItem.item.name.orEmpty(),
							description = resumeItem.item.overview.orEmpty(),
							backdropUrl = resumeItem.backdropImageUrl,
							logoUrl = resumeItem.logoImageUrl,
							metadata = buildMetadataString(
								resumeItem.item.productionYear?.toString(),
								resumeItem.item.runTimeTicks?.let { formatRuntime(it) },
								resumeItem.item.genres?.take(3)
							),
							playbackProgress = ((resumeItem.item.userData?.playedPercentage ?: 0.0) / 100.0).toFloat(),
							playbackText = "Continue Watching",
							onResumeClick = { onPlayClick(resumeItem) },
							onRestartClick = { onPlayClick(resumeItem) },
							onInfoClick = { onInfoClick(resumeItem) }
						)
					}

					// Show featured banner otherwise
					featuredItem != null -> {
						HeroBanner(
							title = featuredItem.item.name.orEmpty(),
							description = featuredItem.item.overview.orEmpty(),
							backdropUrl = featuredItem.backdropImageUrl,
							logoUrl = featuredItem.logoImageUrl,
							metadata = buildMetadataString(
								featuredItem.item.productionYear?.toString(),
								featuredItem.item.runTimeTicks?.let { formatRuntime(it) },
								featuredItem.item.genres?.take(3)
							),
							onPlayClick = { onPlayClick(featuredItem) },
							onInfoClick = { onInfoClick(featuredItem) }
						)
					}
				}

				// Spacing after hero banner
				Spacer(modifier = Modifier.height(padding.large))

				// Continue Watching Carousel
				if (uiState.resumeItems.isNotEmpty()) {
					ContinueWatchingCarousel(
						items = uiState.resumeItems.map { homeItem ->
							ContinueWatchingItem(
								id = homeItem.item.id.toString(),
								title = homeItem.item.name.orEmpty(),
								imageUrl = homeItem.primaryImageUrl,
								progress = ((homeItem.item.userData?.playedPercentage ?: 0.0) / 100.0).toFloat()
							)
						},
						onItemClick = { continueWatchingItem ->
							// Find the original item and handle click
							val homeItem = uiState.resumeItems.find { it.item.id.toString() == continueWatchingItem.id }
							homeItem?.let(onItemClick)
						}
					)

					Spacer(modifier = Modifier.height(padding.medium))
				}

				// Next Up Carousel (for TV shows)
				if (uiState.nextUpItems.isNotEmpty()) {
					NextUpCarousel(
						items = uiState.nextUpItems.map { homeItem ->
							NextUpItem(
								id = homeItem.item.id.toString(),
								title = homeItem.item.name.orEmpty(),
								episodeInfo = buildEpisodeInfo(homeItem.item.parentIndexNumber, homeItem.item.indexNumber),
								imageUrl = homeItem.primaryImageUrl,
								seriesId = homeItem.item.seriesId?.toString()
							)
						},
						onItemClick = { nextUpItem ->
							val homeItem = uiState.nextUpItems.find { it.item.id.toString() == nextUpItem.id }
							homeItem?.let(onItemClick)
						}
					)

					Spacer(modifier = Modifier.height(padding.medium))
				}

				// Latest Media Carousel
				if (uiState.latestItems.isNotEmpty()) {
					LatestMediaCarousel(
						title = "Latest Movies & Shows",
						items = uiState.latestItems.map { homeItem ->
							MediaItem(
								id = homeItem.item.id.toString(),
								title = homeItem.item.name.orEmpty(),
								imageUrl = homeItem.primaryImageUrl
							)
						},
						onItemClick = { mediaItem ->
							val homeItem = uiState.latestItems.find { it.item.id.toString() == mediaItem.id }
							homeItem?.let(onItemClick)
						},
						onSeeAllClick = {
							// Navigate to full latest media library
						}
					)

					Spacer(modifier = Modifier.height(padding.medium))
				}

					// Bottom padding for safe area
					Spacer(modifier = Modifier.height(padding.large))
				}
			}
		}
	}
}

/**
 * Default navigation items for the drawer
 */
private fun getDefaultNavigationItems(): List<NavigationItem> = listOf(
	NavigationItem(id = "home", label = "Home", icon = "🏠"),
	NavigationItem(id = "movies", label = "Movies", icon = "🎬"),
	NavigationItem(id = "shows", label = "TV Shows", icon = "📺"),
	NavigationItem(id = "music", label = "Music", icon = "🎵"),
	NavigationItem(id = "livetv", label = "Live TV", icon = "📡"),
	NavigationItem(id = "favorites", label = "Favorites", icon = "⭐"),
	NavigationItem(id = "search", label = "Search", icon = "🔍"),
	NavigationItem(id = "settings", label = "Settings", icon = "⚙️"),
)

/**
 * Build metadata string from components (e.g., "2024 · 2h 15m · Drama, Thriller")
 */
private fun buildMetadataString(year: String?, runtime: String?, genres: List<String>?): String? {
	val parts = listOfNotNull(
		year,
		runtime,
		genres?.joinToString(", ")
	)
	return if (parts.isNotEmpty()) parts.joinToString(" · ") else null
}

/**
 * Build episode info string (e.g., "S1:E5")
 */
private fun buildEpisodeInfo(seasonNumber: Int?, episodeNumber: Int?): String? {
	return if (seasonNumber != null && episodeNumber != null) {
		"S$seasonNumber:E$episodeNumber"
	} else null
}

/**
 * Format runtime ticks to human-readable string (e.g., "2h 15m")
 */
private fun formatRuntime(ticks: Long): String {
	val totalMinutes = (ticks / 10_000_000 / 60).toInt()
	val hours = totalMinutes / 60
	val minutes = totalMinutes % 60

	return when {
		hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
		hours > 0 -> "${hours}h"
		else -> "${minutes}m"
	}
}
