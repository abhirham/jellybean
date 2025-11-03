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
				onItemClick = onItemClick,
				onPlayClick = { item ->
					// Handle play action
					onItemClick(item)
				},
				onInfoClick = { item ->
					// Navigate to detail screen
					onItemClick(item)
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
	onItemClick: (BaseItemDto) -> Unit,
	onPlayClick: (BaseItemDto) -> Unit,
	onInfoClick: (BaseItemDto) -> Unit,
	onRefresh: () -> Unit,
) {
	val padding = LocalTvPadding.current

	when {
		uiState.isLoading && uiState.featuredItem == null -> {
			// Initial loading state
			TvFullScreen {
				LoadingState(
					message = "Loading your content..."
				)
			}
		}

		uiState.error != null -> {
			// Error state
			TvFullScreen {
				ErrorState(
					message = uiState.error,
					onRetry = onRefresh
				)
			}
		}

		else -> {
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
					resumeItem != null && resumeItem.userData?.playedPercentage != null -> {
						HeroBannerWithProgress(
							title = resumeItem.name.orEmpty(),
							description = resumeItem.overview.orEmpty(),
							backdropUrl = resumeItem.getImageUrl(ImageType.BACKDROP),
							logoUrl = resumeItem.getImageUrl(ImageType.LOGO),
							metadata = buildMetadataString(
								resumeItem.productionYear?.toString(),
								resumeItem.runTimeTicks?.let { formatRuntime(it) },
								resumeItem.genres?.take(3)
							),
							playbackProgress = ((resumeItem.userData?.playedPercentage ?: 0.0) / 100.0).toFloat(),
							playbackText = "Continue Watching",
							onResumeClick = { onPlayClick(resumeItem) },
							onRestartClick = { onPlayClick(resumeItem) },
							onInfoClick = { onInfoClick(resumeItem) }
						)
					}

					// Show featured banner otherwise
					featuredItem != null -> {
						HeroBanner(
							title = featuredItem.name.orEmpty(),
							description = featuredItem.overview.orEmpty(),
							backdropUrl = featuredItem.getImageUrl(ImageType.BACKDROP),
							logoUrl = featuredItem.getImageUrl(ImageType.LOGO),
							metadata = buildMetadataString(
								featuredItem.productionYear?.toString(),
								featuredItem.runTimeTicks?.let { formatRuntime(it) },
								featuredItem.genres?.take(3)
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
						items = uiState.resumeItems.map { item ->
							ContinueWatchingItem(
								id = item.id.toString(),
								title = item.name.orEmpty(),
								imageUrl = item.getImageUrl(ImageType.PRIMARY),
								progress = ((item.userData?.playedPercentage ?: 0.0) / 100.0).toFloat()
							)
						},
						onItemClick = { continueWatchingItem ->
							// Find the original item and handle click
							val item = uiState.resumeItems.find { it.id.toString() == continueWatchingItem.id }
							item?.let(onItemClick)
						}
					)

					Spacer(modifier = Modifier.height(padding.medium))
				}

				// Next Up Carousel (for TV shows)
				if (uiState.nextUpItems.isNotEmpty()) {
					NextUpCarousel(
						items = uiState.nextUpItems.map { item ->
							NextUpItem(
								id = item.id.toString(),
								title = item.name.orEmpty(),
								episodeInfo = buildEpisodeInfo(item.parentIndexNumber, item.indexNumber),
								imageUrl = item.getImageUrl(ImageType.PRIMARY),
								seriesId = item.seriesId?.toString()
							)
						},
						onItemClick = { nextUpItem ->
							val item = uiState.nextUpItems.find { it.id.toString() == nextUpItem.id }
							item?.let(onItemClick)
						}
					)

					Spacer(modifier = Modifier.height(padding.medium))
				}

				// Latest Media Carousel
				if (uiState.latestItems.isNotEmpty()) {
					LatestMediaCarousel(
						title = "Latest Movies & Shows",
						items = uiState.latestItems.map { item ->
							MediaItem(
								id = item.id.toString(),
								title = item.name.orEmpty(),
								imageUrl = item.getImageUrl(ImageType.PRIMARY)
							)
						},
						onItemClick = { mediaItem ->
							val item = uiState.latestItems.find { it.id.toString() == mediaItem.id }
							item?.let(onItemClick)
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
 * Helper extension to get image URL from BaseItemDto
 */
private fun BaseItemDto.getImageUrl(imageType: ImageType): String? {
	return when (imageType) {
		ImageType.PRIMARY -> imageTags?.get(ImageType.PRIMARY)?.let { tag ->
			// Construct image URL - this is a simplified version
			// In production, you'd use the actual API client's image URL builder
			id.toString() // Placeholder
		}
		ImageType.BACKDROP -> backdropImageTags?.firstOrNull()?.let {
			id.toString() // Placeholder
		}
		ImageType.LOGO -> imageTags?.get(ImageType.LOGO)?.let {
			id.toString() // Placeholder
		}
		else -> null
	}
}

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
