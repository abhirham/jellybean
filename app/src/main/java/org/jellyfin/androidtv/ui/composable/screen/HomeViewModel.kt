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
import org.jellyfin.androidtv.util.ImageHelper
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.extensions.itemsApi
import org.jellyfin.sdk.api.client.extensions.tvShowsApi
import org.jellyfin.sdk.api.client.extensions.userLibraryApi
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.ItemFields
import org.jellyfin.sdk.model.api.ItemFilter
import org.jellyfin.sdk.model.api.ImageType
import timber.log.Timber

/**
 * ViewModel for the home screen that aggregates data from various repositories.
 * Provides data for hero banner, carousels, and navigation.
 */
class HomeViewModel(
	private val api: ApiClient,
	private val userViewsRepository: UserViewsRepository,
	private val imageHelper: ImageHelper,
) : ViewModel() {

	private val _uiState = MutableStateFlow(HomeScreenUiState())
	val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

	init {
		loadHomeData()
	}

	private fun loadHomeData() {
		viewModelScope.launch {
			_uiState.value = _uiState.value.copy(isLoading = true, error = null)

			try {
				// Load all data in parallel
				val featuredJob = launch { loadFeaturedContent() }
				val resumeJob = launch { loadResumeItems() }
				val latestJob = launch { loadLatestMedia() }
				val nextUpJob = launch { loadNextUpEpisodes() }

				// Wait for all to complete
				featuredJob.join()
				resumeJob.join()
				latestJob.join()
				nextUpJob.join()

				// All data loaded successfully
				_uiState.value = _uiState.value.copy(isLoading = false)
			} catch (e: Exception) {
				Timber.e(e, "Error loading home data")
				_uiState.value = _uiState.value.copy(
					isLoading = false,
					error = e.message ?: "Failed to load content"
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

			val featuredItem = response.content.firstOrNull()?.toHomeItem()
			_uiState.value = _uiState.value.copy(
				featuredItem = featuredItem
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
				resumeItems = response.content.items.orEmpty().map { it.toHomeItem() }
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
				latestItems = response.content.map { it.toHomeItem() }
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
				nextUpItems = response.content.items.orEmpty().map { it.toHomeItem() }
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

	/**
	 * Convert BaseItemDto to HomeItemWithImages with pre-generated image URLs
	 */
	private fun BaseItemDto.toHomeItem(): HomeItemWithImages {
		return HomeItemWithImages(
			item = this,
			primaryImageUrl = imageHelper.getPrimaryImageUrl(this, preferParentThumb = true),
			backdropImageUrl = imageHelper.getPrimaryImageUrl(this)?.let {
				// For backdrop, we prefer backdrop images
				imageHelper.getBannerImageUrl(this, fillWidth = 1920, fillHeight = 1080)
			},
			logoImageUrl = imageHelper.getLogoImageUrl(this)
		)
	}
}

/**
 * UI state for the home screen
 */
data class HomeScreenUiState(
	val isLoading: Boolean = false,
	val error: String? = null,
	val featuredItem: HomeItemWithImages? = null,
	val resumeItems: List<HomeItemWithImages> = emptyList(),
	val latestItems: List<HomeItemWithImages> = emptyList(),
	val nextUpItems: List<HomeItemWithImages> = emptyList(),
)

/**
 * Wrapper for BaseItemDto with pre-generated image URLs
 */
data class HomeItemWithImages(
	val item: BaseItemDto,
	val primaryImageUrl: String?,
	val backdropImageUrl: String?,
	val logoImageUrl: String?,
)
