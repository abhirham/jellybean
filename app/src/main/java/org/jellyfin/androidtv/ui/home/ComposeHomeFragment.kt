package org.jellyfin.androidtv.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jellyfin.androidtv.auth.repository.ServerRepository
import org.jellyfin.androidtv.auth.repository.SessionRepository
import org.jellyfin.androidtv.data.repository.NotificationsRepository
import org.jellyfin.androidtv.ui.composable.screen.HomeScreen
import org.jellyfin.androidtv.ui.composable.theme.JellyfinTvTheme
import org.jellyfin.androidtv.ui.composable.screen.HomeViewModel
import org.jellyfin.androidtv.ui.navigation.Destinations
import org.jellyfin.androidtv.ui.navigation.NavigationRepository
import org.jellyfin.sdk.model.api.BaseItemDto
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * Compose-based home fragment using the new Netflix-style HomeScreen.
 * This is the modern replacement for the Leanback-based HomeFragment.
 */
class ComposeHomeFragment : Fragment() {
	private val sessionRepository by inject<SessionRepository>()
	private val serverRepository by inject<ServerRepository>()
	private val notificationRepository by inject<NotificationsRepository>()
	private val navigationRepository by inject<NavigationRepository>()
	private val viewModel by viewModel<HomeViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		return ComposeView(requireContext()).apply {
			setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
			setContent {
				JellyfinTvTheme {
					HomeScreen(
						onNavigate = { destination ->
							handleNavigation(destination)
						},
						onItemClick = { item ->
							handleItemClick(item)
						}
					)
				}
			}
		}
	}

	override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// Update server notifications (same as original HomeFragment)
		sessionRepository.currentSession
			.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
			.onEach { session ->
				if (session == null) {
					notificationRepository.updateServerNotifications(null)
				} else {
					val server = serverRepository.getServer(session.serverId)
					notificationRepository.updateServerNotifications(server)
				}
			}
			.launchIn(viewLifecycleOwner.lifecycleScope)
	}

	/**
	 * Handle navigation from the home screen
	 */
	private fun handleNavigation(destination: String) {
		Timber.d("Navigation to: $destination")

		when (destination) {
			"search" -> {
				// Navigate to search screen
				navigationRepository.navigate(Destinations.search())
			}
			else -> {
				// Try to find the library folder by ID
				val libraryFolder = viewModel.uiState.value.libraryFolders[destination]

				if (libraryFolder != null) {
					Timber.d("Navigating to library: ${libraryFolder.name}")
					// Navigate to library browser with the folder item
					navigationRepository.navigate(Destinations.libraryBrowser(libraryFolder))
				} else {
					Timber.w("Library folder not found for ID: $destination")
				}
			}
		}
	}

	/**
	 * Handle item click from carousels or hero banner
	 */
	private fun handleItemClick(item: BaseItemDto) {
		Timber.d("Item clicked: ${item.name} (${item.id})")

		// Navigate to item details screen
		// The detail screen will handle whether to show details or start playback
		navigationRepository.navigate(Destinations.itemDetails(item.id))
	}
}
