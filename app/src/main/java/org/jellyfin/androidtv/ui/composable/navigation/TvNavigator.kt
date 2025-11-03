package org.jellyfin.androidtv.ui.composable.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

/**
 * Type-safe navigator for Jellyfin TV Compose UI
 *
 * Provides convenient navigation methods with compile-time safety.
 * Wraps NavController to provide domain-specific navigation logic.
 */
class TvNavigator(private val navController: NavController) {

	// Home navigation
	fun navigateToHome(clearBackStack: Boolean = false) {
		val navOptions = if (clearBackStack) {
			NavOptions.Builder()
				.setPopUpTo(NavigationDestination.Home.route, inclusive = false)
				.build()
		} else null

		navController.navigate(NavigationDestination.Home.route, navOptions)
	}

	// Library navigation
	fun navigateToMyLibrary() {
		navController.navigate(NavigationDestination.MyLibrary.route)
	}

	fun navigateToMovies() {
		navController.navigate(NavigationDestination.Movies.route)
	}

	fun navigateToTvShows() {
		navController.navigate(NavigationDestination.TvShows.route)
	}

	fun navigateToLiveTv() {
		navController.navigate(NavigationDestination.LiveTv.route)
	}

	// Detail navigation
	fun navigateToItemDetail(itemId: String) {
		navController.navigate(NavigationDestination.ItemDetail.createRoute(itemId))
	}

	fun navigateToSeasonDetail(seriesId: String, seasonId: String) {
		navController.navigate(NavigationDestination.SeasonDetail.createRoute(seriesId, seasonId))
	}

	// Browse navigation
	fun navigateToBrowse(categoryId: String) {
		navController.navigate(NavigationDestination.Browse.createRoute(categoryId))
	}

	// Search navigation
	fun navigateToSearch() {
		navController.navigate(NavigationDestination.Search.route)
	}

	// Settings navigation
	fun navigateToSettings() {
		navController.navigate(NavigationDestination.Settings.route)
	}

	fun navigateToUserProfile() {
		navController.navigate(NavigationDestination.UserProfile.route)
	}

	// Playback navigation
	fun navigateToPlayback(itemId: String) {
		navController.navigate(NavigationDestination.Playback.createRoute(itemId))
	}

	// Back navigation
	fun navigateBack(): Boolean {
		return navController.popBackStack()
	}

	// Navigate to a category from the drawer
	fun navigateToCategory(category: NavigationCategory) {
		when (category) {
			NavigationCategory.HOME -> navigateToHome()
			NavigationCategory.MY_LIBRARY -> navigateToMyLibrary()
			NavigationCategory.MOVIES -> navigateToMovies()
			NavigationCategory.TV_SHOWS -> navigateToTvShows()
			NavigationCategory.LIVE_TV -> navigateToLiveTv()
			NavigationCategory.SEARCH -> navigateToSearch()
			NavigationCategory.SETTINGS -> navigateToSettings()
		}
	}
}
