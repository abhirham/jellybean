package org.jellyfin.androidtv.ui.composable.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.jellyfin.androidtv.data.repository.UserViewsRepository
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.extensions.itemsApi
import org.jellyfin.sdk.api.client.extensions.tvShowsApi
import org.jellyfin.sdk.api.client.extensions.userLibraryApi
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.ItemFields
import org.jellyfin.sdk.model.api.ItemFilter
import timber.log.Timber

/**
 * ViewModel for the home screen that aggregates data from various repositories.
 * Provides data for hero banner, carousels, and navigation.
 */
class HomeViewModel(
	private val api: ApiClient,
	private val userViewsRepository: UserViewsRepository,
) : ViewModel() {

	private val _uiState = MutableStateFlow(HomeScreenUiState())
	val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

	init {
		loadHomeData()
	}

	private fun loadHomeData() {
		viewModelScope.launch {
			_uiState.value = _uiState.value.copy(isLoading = true)

			try {
				// Load all data in parallel
				launch { loadFeaturedContent() }
				launch { loadResumeItems() }
				launch { loadLatestMedia() }
				launch { loadNextUpEpisodes() }
			} catch (e: Exception) {
				Timber.e(e, "Error loading home data")
				_uiState.value = _uiState.value.copy(
					isLoading = false,
					error = e.message
				)
			}
		}
	}

	private suspend fun loadFeaturedContent() {
		try {
			val response = api.userLibraryApi.getLatestMedia(
				limit = 1,
				fields = listOf(
					ItemFields.PRIMARY_IMAGE_ASPECT_RATIO,
					ItemFields.OVERVIEW,
					ItemFields.ITEM_COUNTS,
					ItemFields.DISPLAY_PREFERENCES_ID,
					ItemFields.CHILD_COUNT
				),
				includeItemTypes = listOf(BaseItemKind.MOVIE, BaseItemKind.SERIES),
				enableImages = true,
				imageTypeLimit = 1,
			)

			val featuredItem = response.content.firstOrNull()
			_uiState.value = _uiState.value.copy(
				featuredItem = featuredItem,
				isLoading = false
			)
		} catch (e: Exception) {
			Timber.e(e, "Error loading featured content")
		}
	}

	private suspend fun loadResumeItems() {
		try {
			val response = api.itemsApi.getResumeItems(
				limit = 20,
				fields = listOf(
					ItemFields.PRIMARY_IMAGE_ASPECT_RATIO,
					ItemFields.OVERVIEW,
					ItemFields.ITEM_COUNTS,
					ItemFields.DISPLAY_PREFERENCES_ID,
					ItemFields.CHILD_COUNT
				),
				enableImages = true,
				imageTypeLimit = 1,
			)

			_uiState.value = _uiState.value.copy(
				resumeItems = response.content.items.orEmpty()
			)
		} catch (e: Exception) {
			Timber.e(e, "Error loading resume items")
		}
	}

	private suspend fun loadLatestMedia() {
		try {
			val response = api.userLibraryApi.getLatestMedia(
				limit = 20,
				fields = listOf(
					ItemFields.PRIMARY_IMAGE_ASPECT_RATIO,
					ItemFields.OVERVIEW,
					ItemFields.ITEM_COUNTS,
					ItemFields.DISPLAY_PREFERENCES_ID,
					ItemFields.CHILD_COUNT
				),
				includeItemTypes = listOf(BaseItemKind.MOVIE, BaseItemKind.SERIES),
				enableImages = true,
				imageTypeLimit = 1,
			)

			_uiState.value = _uiState.value.copy(
				latestItems = response.content
			)
		} catch (e: Exception) {
			Timber.e(e, "Error loading latest media")
		}
	}

	private suspend fun loadNextUpEpisodes() {
		try {
			val response = api.tvShowsApi.getNextUp(
				limit = 20,
				fields = listOf(
					ItemFields.PRIMARY_IMAGE_ASPECT_RATIO,
					ItemFields.OVERVIEW,
					ItemFields.ITEM_COUNTS,
					ItemFields.DISPLAY_PREFERENCES_ID,
					ItemFields.CHILD_COUNT
				),
				enableImages = true,
				imageTypeLimit = 1,
			)

			_uiState.value = _uiState.value.copy(
				nextUpItems = response.content.items.orEmpty()
			)
		} catch (e: Exception) {
			Timber.e(e, "Error loading next up episodes")
		}
	}

	fun refresh() {
		loadHomeData()
	}

	fun onItemClick(item: BaseItemDto) {
		// Navigation will be handled by the composable
		Timber.d("Item clicked: ${item.name}")
	}
}

/**
 * UI state for the home screen
 */
data class HomeScreenUiState(
	val isLoading: Boolean = false,
	val error: String? = null,
	val featuredItem: BaseItemDto? = null,
	val resumeItems: List<BaseItemDto> = emptyList(),
	val latestItems: List<BaseItemDto> = emptyList(),
	val nextUpItems: List<BaseItemDto> = emptyList(),
)
