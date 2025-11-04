package org.jellyfin.androidtv.ui.composable.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import org.jellyfin.androidtv.ui.composable.AsyncImage
import org.jellyfin.androidtv.ui.composable.focus.tvFocusEffect
import org.jellyfin.androidtv.ui.composable.theme.LocalTvPadding
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap

/**
 * TV Navigation Drawer with Expand/Collapse
 *
 * Netflix-style navigation drawer that can expand to show labels or collapse to show only icons.
 * Automatically expands when focused and collapses when focus is lost.
 *
 * @param expanded Whether the drawer is expanded (showing labels) or collapsed (icons only)
 * @param selectedItem Currently selected navigation item ID
 * @param items List of navigation items
 * @param userProfile User profile information
 * @param onItemClick Callback when a navigation item is clicked
 * @param onFocusChanged Callback when drawer focus state changes (true = focused, false = not focused)
 * @param modifier Modifier for the drawer container
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvNavigationDrawer(
	expanded: Boolean,
	selectedItem: String?,
	items: List<NavigationItem>,
	userProfile: UserProfile? = null,
	onItemClick: (NavigationItem) -> Unit,
	onFocusChanged: (Boolean) -> Unit,
	modifier: Modifier = Modifier
) {
	val padding = LocalTvPadding.current
	val drawerWidth by animateDpAsState(
		targetValue = if (expanded) 240.dp else 64.dp,
		label = "drawer_width"
	)

	Column(
		modifier = modifier
			.width(drawerWidth)
			.fillMaxHeight()
			.background(MaterialTheme.colorScheme.surface)
			.padding(vertical = padding.screenVertical)
			.onFocusChanged { focusState ->
				onFocusChanged(focusState.hasFocus)
			}
	) {
		// User Profile Section (only when expanded)
		if (expanded && userProfile != null) {
			TvUserProfileSection(
				userProfile = userProfile,
				modifier = Modifier.padding(horizontal = 16.dp)
			)

			Spacer(modifier = Modifier.height(padding.large))

			// Divider
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(1.dp)
					.background(MaterialTheme.colorScheme.surfaceVariant)
			)

			Spacer(modifier = Modifier.height(padding.medium))
		} else if (!expanded) {
			// Hamburger menu icon when collapsed
			val menuIconColor = MaterialTheme.colorScheme.onSurface

			Box(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 8.dp)
					.padding(vertical = 12.dp),
				contentAlignment = Alignment.Center
			) {
				// Hamburger menu icon
				Canvas(modifier = Modifier.size(24.dp)) {
					val strokeWidth = 2.dp.toPx()
					val spacing = size.height / 4

					// Three horizontal lines
					for (i in 0..2) {
						val y = spacing + i * spacing
						drawLine(
							color = menuIconColor,
							start = Offset(size.width * 0.2f, y),
							end = Offset(size.width * 0.8f, y),
							strokeWidth = strokeWidth,
							cap = StrokeCap.Round
						)
					}
				}
			}

			Spacer(modifier = Modifier.height(padding.medium))
		}

		// Navigation Items
		Column(
			verticalArrangement = Arrangement.spacedBy(4.dp)
		) {
			items.forEach { item ->
				TvNavigationDrawerItem(
					item = item,
					selected = item.id == selectedItem,
					expanded = expanded,
					onClick = { onItemClick(item) }
				)
			}
		}
	}
}

/**
 * User Profile Section for TV Drawer
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun TvUserProfileSection(
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
				.size(40.dp)
				.clip(CircleShape)
				.background(MaterialTheme.colorScheme.primaryContainer)
		) {
			if (userProfile.avatarUrl != null) {
				AsyncImage(
					url = userProfile.avatarUrl,
					modifier = Modifier.fillMaxWidth(),
					scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
				)
			} else {
				// Fallback to initials
				Text(
					text = userProfile.name.take(1).uppercase(),
					style = MaterialTheme.typography.titleMedium,
					color = MaterialTheme.colorScheme.onPrimaryContainer,
					modifier = Modifier.align(Alignment.Center)
				)
			}
		}

		// User name
		Text(
			text = userProfile.name,
			style = MaterialTheme.typography.bodyLarge,
			fontWeight = FontWeight.Medium,
			maxLines = 1,
			overflow = TextOverflow.Ellipsis
		)
	}
}

/**
 * TV Navigation Drawer Item
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun TvNavigationDrawerItem(
	item: NavigationItem,
	selected: Boolean,
	expanded: Boolean,
	onClick: () -> Unit
) {
	val backgroundColor = if (selected) {
		MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
	} else {
		Color.Transparent
	}

	val contentColor = if (selected) {
		MaterialTheme.colorScheme.primary
	} else {
		MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
	}

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(4.dp))
			.background(backgroundColor)
			.clickable(onClick = onClick)
			.tvFocusEffect(scale = 1.02f)
			.padding(
				horizontal = if (expanded) 16.dp else 8.dp,
				vertical = 12.dp
			),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(if (expanded) 16.dp else 0.dp)
	) {
		// Icon
		Box(
			modifier = Modifier.size(24.dp),
			contentAlignment = Alignment.Center
		) {
			DrawNavigationIcon(
				iconType = item.icon,
				color = contentColor
			)
		}

		// Label (only when expanded)
		if (expanded) {
			Text(
				text = item.label,
				style = MaterialTheme.typography.bodyMedium,
				color = contentColor,
				fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)
		}
	}
}

/**
 * Draw custom navigation icons
 */
@Composable
private fun DrawNavigationIcon(
	iconType: String,
	color: Color
) {
	Canvas(modifier = Modifier.size(24.dp)) {
		val strokeWidth = 2.dp.toPx()

		when (iconType) {
			"search" -> {
				// Search icon: magnifying glass
				val radius = size.width * 0.3f
				val centerX = size.width * 0.35f
				val centerY = size.height * 0.35f

				// Circle
				drawCircle(
					color = color,
					radius = radius,
					center = Offset(centerX, centerY),
					style = Stroke(width = strokeWidth)
				)

				// Handle
				drawLine(
					color = color,
					start = Offset(centerX + radius * 0.7f, centerY + radius * 0.7f),
					end = Offset(size.width * 0.85f, size.height * 0.85f),
					strokeWidth = strokeWidth,
					cap = StrokeCap.Round
				)
			}

			"movies" -> {
				// Movie clapperboard icon
				val padding = size.width * 0.15f
				val width = size.width - padding * 2
				val height = size.height - padding * 2

				// Main rectangle
				drawRect(
					color = color,
					topLeft = Offset(padding, padding + height * 0.25f),
					size = androidx.compose.ui.geometry.Size(width, height * 0.75f),
					style = Stroke(width = strokeWidth)
				)

				// Top bar (clapperboard top)
				val path = Path().apply {
					moveTo(padding, padding + height * 0.25f)
					lineTo(padding + width * 0.2f, padding)
					lineTo(padding + width * 0.8f, padding)
					lineTo(padding + width, padding + height * 0.25f)
					close()
				}
				drawPath(
					path = path,
					color = color,
					style = Stroke(width = strokeWidth)
				)

				// Vertical stripes on top bar
				for (i in 1..2) {
					val x = padding + width * (i * 0.33f)
					drawLine(
						color = color,
						start = Offset(x, padding),
						end = Offset(x - width * 0.05f, padding + height * 0.25f),
						strokeWidth = strokeWidth
					)
				}
			}

			"tvshows" -> {
				// TV icon
				val padding = size.width * 0.15f
				val width = size.width - padding * 2
				val height = size.height - padding * 2

				// Screen
				drawRect(
					color = color,
					topLeft = Offset(padding, padding),
					size = androidx.compose.ui.geometry.Size(width, height * 0.75f),
					style = Stroke(width = strokeWidth)
				)

				// Stand
				drawLine(
					color = color,
					start = Offset(size.width * 0.5f, padding + height * 0.75f),
					end = Offset(size.width * 0.5f, padding + height),
					strokeWidth = strokeWidth
				)

				// Base
				drawLine(
					color = color,
					start = Offset(padding + width * 0.2f, padding + height),
					end = Offset(padding + width * 0.8f, padding + height),
					strokeWidth = strokeWidth * 1.5f,
					cap = StrokeCap.Round
				)
			}

			"music" -> {
				// Music note icon
				val padding = size.width * 0.2f

				// Note stem
				drawLine(
					color = color,
					start = Offset(size.width * 0.65f, padding),
					end = Offset(size.width * 0.65f, size.height * 0.65f),
					strokeWidth = strokeWidth
				)

				// Note head (oval)
				drawOval(
					color = color,
					topLeft = Offset(size.width * 0.45f, size.height * 0.6f),
					size = androidx.compose.ui.geometry.Size(size.width * 0.25f, size.height * 0.25f),
					style = Stroke(width = strokeWidth)
				)

				// Flag
				val flagPath = Path().apply {
					moveTo(size.width * 0.65f, padding)
					cubicTo(
						size.width * 0.8f, padding,
						size.width * 0.8f, padding + size.height * 0.2f,
						size.width * 0.65f, padding + size.height * 0.25f
					)
				}
				drawPath(
					path = flagPath,
					color = color,
					style = Stroke(width = strokeWidth)
				)
			}

			"folder" -> {
				// Folder icon
				val padding = size.width * 0.1f
				val width = size.width - padding * 2
				val height = size.height - padding * 2

				// Tab
				val tabPath = Path().apply {
					moveTo(padding, padding + height * 0.3f)
					lineTo(padding, padding + height * 0.15f)
					lineTo(padding + width * 0.35f, padding + height * 0.15f)
					lineTo(padding + width * 0.4f, padding + height * 0.3f)
				}
				drawPath(
					path = tabPath,
					color = color,
					style = Stroke(width = strokeWidth)
				)

				// Main folder body
				drawRect(
					color = color,
					topLeft = Offset(padding, padding + height * 0.3f),
					size = androidx.compose.ui.geometry.Size(width, height * 0.7f),
					style = Stroke(width = strokeWidth)
				)
			}

			else -> {
				// Default: generic folder icon
				val padding = size.width * 0.1f
				val width = size.width - padding * 2
				val height = size.height - padding * 2

				drawRect(
					color = color,
					topLeft = Offset(padding, padding + height * 0.3f),
					size = androidx.compose.ui.geometry.Size(width, height * 0.7f),
					style = Stroke(width = strokeWidth)
				)
			}
		}
	}
}
