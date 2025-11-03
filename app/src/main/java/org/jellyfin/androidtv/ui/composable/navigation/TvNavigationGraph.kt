package org.jellyfin.androidtv.ui.composable.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text

/**
 * Main navigation graph for Jellyfin TV Compose UI
 *
 * Defines the navigation structure for all screens in the app.
 * Uses Compose Navigation with type-safe destinations.
 */
@Composable
fun TvNavigationGraph(
	navController: NavHostController,
	modifier: Modifier = Modifier,
	startDestination: String = NavigationDestination.Home.route,
) {
	NavHost(
		navController = navController,
		startDestination = startDestination,
		modifier = modifier,
	) {
		// Home screen
		composable(NavigationDestination.Home.route) {
			// TODO: HomeScreen composable will be implemented in Phase 3
			PlaceholderScreen(title = "Home Screen")
		}

		// Library screens
		composable(NavigationDestination.MyLibrary.route) {
			PlaceholderScreen(title = "My Library")
		}

		composable(NavigationDestination.Movies.route) {
			PlaceholderScreen(title = "Movies")
		}

		composable(NavigationDestination.TvShows.route) {
			PlaceholderScreen(title = "TV Shows")
		}

		composable(NavigationDestination.LiveTv.route) {
			PlaceholderScreen(title = "Live TV")
		}

		// Detail screens
		composable(
			route = NavigationDestination.ItemDetail.ROUTE,
			arguments = listOf(
				navArgument("itemId") { type = NavType.StringType }
			)
		) { backStackEntry ->
			val itemId = backStackEntry.arguments?.getString("itemId") ?: return@composable
			PlaceholderScreen(title = "Item Detail: $itemId")
		}

		composable(
			route = NavigationDestination.SeasonDetail.ROUTE,
			arguments = listOf(
				navArgument("seriesId") { type = NavType.StringType },
				navArgument("seasonId") { type = NavType.StringType }
			)
		) { backStackEntry ->
			val seriesId = backStackEntry.arguments?.getString("seriesId") ?: return@composable
			val seasonId = backStackEntry.arguments?.getString("seasonId") ?: return@composable
			PlaceholderScreen(title = "Season: $seriesId/$seasonId")
		}

		// Browse by category
		composable(
			route = NavigationDestination.Browse.ROUTE,
			arguments = listOf(
				navArgument("categoryId") { type = NavType.StringType }
			)
		) { backStackEntry ->
			val categoryId = backStackEntry.arguments?.getString("categoryId") ?: return@composable
			PlaceholderScreen(title = "Browse: $categoryId")
		}

		// Search
		composable(NavigationDestination.Search.route) {
			PlaceholderScreen(title = "Search")
		}

		// Settings
		composable(NavigationDestination.Settings.route) {
			PlaceholderScreen(title = "Settings")
		}

		composable(NavigationDestination.UserProfile.route) {
			PlaceholderScreen(title = "User Profile")
		}

		// Playback
		composable(
			route = NavigationDestination.Playback.ROUTE,
			arguments = listOf(
				navArgument("itemId") { type = NavType.StringType }
			)
		) { backStackEntry ->
			val itemId = backStackEntry.arguments?.getString("itemId") ?: return@composable
			PlaceholderScreen(title = "Playback: $itemId")
		}
	}
}

/**
 * Temporary placeholder screen for screens not yet implemented
 * TODO: Remove this when all screens are implemented
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun PlaceholderScreen(title: String) {
	// Placeholder - will be replaced with actual screen implementations
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = "TODO: $title",
			style = MaterialTheme.typography.headlineMedium
		)
	}
}
