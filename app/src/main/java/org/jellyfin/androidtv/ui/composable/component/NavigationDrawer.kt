package org.jellyfin.androidtv.ui.composable.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import org.jellyfin.androidtv.ui.composable.AsyncImage
import org.jellyfin.androidtv.ui.composable.focus.tvFocusEffect
import org.jellyfin.androidtv.ui.composable.theme.LocalTvPadding

/**
 * Navigation Drawer Component
 *
 * Side navigation drawer for TV with Netflix-style menu.
 * Displays navigation items, user profile, and settings.
 *
 * @param visible Whether the drawer is visible
 * @param selectedItem Currently selected navigation item
 * @param items List of navigation items
 * @param userProfile Optional user profile information
 * @param onItemClick Callback when a navigation item is clicked
 * @param onDismiss Callback when drawer should be dismissed
 * @param modifier Modifier for the drawer container
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun NavigationDrawer(
	visible: Boolean,
	selectedItem: String?,
	items: List<NavigationItem>,
	userProfile: UserProfile? = null,
	onItemClick: (NavigationItem) -> Unit,
	onDismiss: () -> Unit,
	modifier: Modifier = Modifier
) {
	val padding = LocalTvPadding.current

	Box(modifier = modifier.fillMaxSize()) {
		// Scrim (Background overlay)
		if (visible) {
			Box(
				modifier = Modifier
					.fillMaxSize()
					.background(MaterialTheme.colorScheme.scrim)
					.clickable(onClick = onDismiss)
			)
		}

		// Drawer Content
		AnimatedVisibility(
			visible = visible,
			enter = slideInHorizontally(initialOffsetX = { -it }),
			exit = slideOutHorizontally(targetOffsetX = { -it })
		) {
			Column(
				modifier = Modifier
					.width(padding.drawerWidth)
					.fillMaxHeight()
					.background(MaterialTheme.colorScheme.surface)
					.padding(vertical = padding.screenVertical)
			) {
				// User Profile Section
				if (userProfile != null) {
					UserProfileSection(
						userProfile = userProfile,
						modifier = Modifier.padding(horizontal = padding.drawerItemPadding)
					)

					Spacer(modifier = Modifier.height(padding.large))

					// Divider
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.height(1.dp)
							.background(MaterialTheme.colorScheme.surfaceVariant)
					)

					Spacer(modifier = Modifier.height(padding.large))
				}

				// Navigation Items
				Column(
					verticalArrangement = Arrangement.spacedBy(padding.drawerItemSpacing)
				) {
					items.forEach { item ->
						NavigationDrawerItem(
							item = item,
							selected = item.id == selectedItem,
							onClick = { onItemClick(item) },
							modifier = Modifier.padding(horizontal = padding.drawerItemPadding)
						)
					}
				}
			}
		}
	}
}

/**
 * Navigation Drawer Item
 *
 * Individual navigation item with icon and label.
 *
 * @param item Navigation item data
 * @param selected Whether this item is currently selected
 * @param onClick Callback when item is clicked
 * @param modifier Modifier for the item container
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun NavigationDrawerItem(
	item: NavigationItem,
	selected: Boolean,
	onClick: () -> Unit,
	modifier: Modifier = Modifier
) {
	val backgroundColor = if (selected) {
		MaterialTheme.colorScheme.primaryContainer
	} else {
		Color.Transparent
	}

	val contentColor = if (selected) {
		MaterialTheme.colorScheme.onPrimaryContainer
	} else {
		MaterialTheme.colorScheme.onSurface
	}

	Row(
		modifier = modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(8.dp))
			.background(backgroundColor)
			.clickable(onClick = onClick)
			.tvFocusEffect(scale = 1.02f)
			.padding(vertical = 12.dp, horizontal = 16.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(16.dp)
	) {
		// Icon (using emoji for now, can be replaced with actual icons)
		Text(
			text = item.icon,
			style = MaterialTheme.typography.headlineSmall,
			color = contentColor
		)

		// Label
		Text(
			text = item.label,
			style = MaterialTheme.typography.bodyLarge,
			color = contentColor,
			fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
			maxLines = 1,
			overflow = TextOverflow.Ellipsis
		)
	}
}

/**
 * User Profile Section
 *
 * Displays user avatar and name in the drawer header.
 *
 * @param userProfile User profile data
 * @param modifier Modifier for the profile section
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun UserProfileSection(
	userProfile: UserProfile,
	modifier: Modifier = Modifier
) {
	Row(
		modifier = modifier.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(12.dp)
	) {
		// Avatar
		Box(
			modifier = Modifier
				.size(48.dp)
				.clip(CircleShape)
				.background(MaterialTheme.colorScheme.primaryContainer)
		) {
			if (userProfile.avatarUrl != null) {
				AsyncImage(
					url = userProfile.avatarUrl,
					modifier = Modifier.fillMaxSize(),
					scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
				)
			} else {
				// Fallback to initials
				Text(
					text = userProfile.name.take(1).uppercase(),
					style = MaterialTheme.typography.headlineSmall,
					color = MaterialTheme.colorScheme.onPrimaryContainer,
					modifier = Modifier.align(Alignment.Center)
				)
			}
		}

		// User Info
		Column {
			Text(
				text = userProfile.name,
				style = MaterialTheme.typography.bodyLarge,
				fontWeight = FontWeight.Bold,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)

			if (userProfile.subtitle != null) {
				Text(
					text = userProfile.subtitle,
					style = MaterialTheme.typography.bodySmall,
					color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
					maxLines = 1,
					overflow = TextOverflow.Ellipsis
				)
			}
		}
	}
}

/**
 * Compact Navigation Drawer
 *
 * Minimalist drawer showing only icons (Netflix-style sidebar).
 *
 * @param visible Whether the drawer is visible
 * @param selectedItem Currently selected navigation item
 * @param items List of navigation items
 * @param onItemClick Callback when a navigation item is clicked
 * @param modifier Modifier for the drawer container
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun CompactNavigationDrawer(
	visible: Boolean,
	selectedItem: String?,
	items: List<NavigationItem>,
	onItemClick: (NavigationItem) -> Unit,
	modifier: Modifier = Modifier
) {
	AnimatedVisibility(
		visible = visible,
		enter = slideInHorizontally(initialOffsetX = { -it }),
		exit = slideOutHorizontally(targetOffsetX = { -it })
	) {
		Column(
			modifier = modifier
				.width(80.dp)
				.fillMaxHeight()
				.background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
				.padding(vertical = 24.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {
			items.forEach { item ->
				CompactNavigationItem(
					item = item,
					selected = item.id == selectedItem,
					onClick = { onItemClick(item) }
				)
			}
		}
	}
}

/**
 * Compact Navigation Item
 *
 * Icon-only navigation item for compact drawer.
 *
 * @param item Navigation item data
 * @param selected Whether this item is currently selected
 * @param onClick Callback when item is clicked
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun CompactNavigationItem(
	item: NavigationItem,
	selected: Boolean,
	onClick: () -> Unit
) {
	val backgroundColor = if (selected) {
		MaterialTheme.colorScheme.primaryContainer
	} else {
		Color.Transparent
	}

	Box(
		modifier = Modifier
			.size(56.dp)
			.clip(RoundedCornerShape(8.dp))
			.background(backgroundColor)
			.clickable(onClick = onClick)
			.tvFocusEffect(scale = 1.05f),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = item.icon,
			style = MaterialTheme.typography.headlineMedium
		)
	}
}

/**
 * Navigation Item
 *
 * Data class representing a navigation menu item.
 */
data class NavigationItem(
	val id: String,
	val label: String,
	val icon: String, // Emoji or icon identifier
	val route: String? = null
)

/**
 * User Profile
 *
 * Data class for user profile information in drawer.
 */
data class UserProfile(
	val id: String,
	val name: String,
	val avatarUrl: String? = null,
	val subtitle: String? = null // e.g., "Admin" or email
)

/**
 * Common navigation items for Jellyfin
 */
object JellyfinNavigationItems {
	val Home = NavigationItem(
		id = "home",
		label = "Home",
		icon = "🏠",
		route = "home"
	)

	val Movies = NavigationItem(
		id = "movies",
		label = "Movies",
		icon = "🎬",
		route = "movies"
	)

	val Shows = NavigationItem(
		id = "shows",
		label = "TV Shows",
		icon = "📺",
		route = "shows"
	)

	val Music = NavigationItem(
		id = "music",
		label = "Music",
		icon = "🎵",
		route = "music"
	)

	val LiveTV = NavigationItem(
		id = "livetv",
		label = "Live TV",
		icon = "📡",
		route = "livetv"
	)

	val Favorites = NavigationItem(
		id = "favorites",
		label = "Favorites",
		icon = "⭐",
		route = "favorites"
	)

	val Search = NavigationItem(
		id = "search",
		label = "Search",
		icon = "🔍",
		route = "search"
	)

	val Settings = NavigationItem(
		id = "settings",
		label = "Settings",
		icon = "⚙️",
		route = "settings"
	)

	fun getDefaultItems(): List<NavigationItem> = listOf(
		Home,
		Movies,
		Shows,
		Music,
		LiveTV,
		Favorites,
		Search,
		Settings
	)
}
