package org.jellyfin.androidtv.ui.composable.navigation

/**
 * Navigation destinations for Jellyfin TV Compose UI
 *
 * Defines all available screens/destinations in the app's navigation graph.
 * Netflix-style navigation with home, browse, detail, and playback screens.
 */
sealed class NavigationDestination(val route: String) {
	// Home screen with hero banner and carousels
	data object Home : NavigationDestination("home")

	// Library and browsing screens
	data object MyLibrary : NavigationDestination("my_library")
	data object Movies : NavigationDestination("movies")
	data object TvShows : NavigationDestination("tv_shows")
	data object LiveTv : NavigationDestination("live_tv")

	// Content detail screens
	data class ItemDetail(val itemId: String) : NavigationDestination("item_detail/{itemId}") {
		companion object {
			const val ROUTE = "item_detail/{itemId}"
			fun createRoute(itemId: String) = "item_detail/$itemId"
		}
	}

	data class SeasonDetail(val seriesId: String, val seasonId: String) :
		NavigationDestination("season_detail/{seriesId}/{seasonId}") {
		companion object {
			const val ROUTE = "season_detail/{seriesId}/{seasonId}"
			fun createRoute(seriesId: String, seasonId: String) = "season_detail/$seriesId/$seasonId"
		}
	}

	// Search
	data object Search : NavigationDestination("search")

	// Settings
	data object Settings : NavigationDestination("settings")
	data object UserProfile : NavigationDestination("user_profile")

	// Playback
	data class Playback(val itemId: String) : NavigationDestination("playback/{itemId}") {
		companion object {
			const val ROUTE = "playback/{itemId}"
			fun createRoute(itemId: String) = "playback/$itemId"
		}
	}

	// Browse by category/genre
	data class Browse(val categoryId: String) : NavigationDestination("browse/{categoryId}") {
		companion object {
			const val ROUTE = "browse/{categoryId}"
			fun createRoute(categoryId: String) = "browse/$categoryId"
		}
	}
}

/**
 * Navigation categories for the left drawer
 */
enum class NavigationCategory(
	val displayName: String,
	val destination: NavigationDestination
) {
	HOME("Home", NavigationDestination.Home),
	MY_LIBRARY("My Library", NavigationDestination.MyLibrary),
	MOVIES("Movies", NavigationDestination.Movies),
	TV_SHOWS("TV Shows", NavigationDestination.TvShows),
	LIVE_TV("Live TV", NavigationDestination.LiveTv),
	SEARCH("Search", NavigationDestination.Search),
	SETTINGS("Settings", NavigationDestination.Settings);

	companion object {
		val mainCategories = listOf(HOME, MY_LIBRARY, MOVIES, TV_SHOWS, LIVE_TV)
		val secondaryCategories = listOf(SEARCH, SETTINGS)
	}
}
