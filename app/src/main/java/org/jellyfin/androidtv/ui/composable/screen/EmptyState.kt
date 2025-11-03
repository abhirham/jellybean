package org.jellyfin.androidtv.ui.composable.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text

/**
 * Empty State Composable
 *
 * Shows empty state with icon, message, and optional action for TV screens.
 *
 * @param title Empty state title (e.g., "No content found")
 * @param message Detailed empty state message
 * @param icon Optional icon/emoji to display
 * @param actionText Optional action button text
 * @param onAction Optional action callback
 * @param modifier Modifier for the container
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun EmptyState(
	title: String,
	message: String,
	icon: String = "📭",
	actionText: String? = null,
	onAction: (() -> Unit)? = null,
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier
			.fillMaxSize()
			.padding(horizontal = 48.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		// Icon
		Text(
			text = icon,
			style = MaterialTheme.typography.displayLarge,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)

		Spacer(modifier = Modifier.height(24.dp))

		// Title
		Text(
			text = title,
			style = MaterialTheme.typography.headlineMedium,
			color = MaterialTheme.colorScheme.onBackground,
			textAlign = TextAlign.Center
		)

		Spacer(modifier = Modifier.height(16.dp))

		// Message
		Text(
			text = message,
			style = MaterialTheme.typography.bodyLarge,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(horizontal = 32.dp)
		)

		// Optional action button
		if (actionText != null && onAction != null) {
			Spacer(modifier = Modifier.height(32.dp))

			Button(onClick = onAction) {
				Text(text = actionText)
			}
		}
	}
}

/**
 * No content found empty state
 */
@Composable
fun NoContentState(
	contentType: String = "content",
	modifier: Modifier = Modifier
) {
	EmptyState(
		title = "No $contentType Found",
		message = "There's no $contentType available to display at the moment.",
		icon = "📭",
		modifier = modifier
	)
}

/**
 * No search results empty state
 */
@Composable
fun NoSearchResultsState(
	query: String,
	onClearSearch: (() -> Unit)? = null,
	modifier: Modifier = Modifier
) {
	EmptyState(
		title = "No Results",
		message = "We couldn't find anything matching \"$query\". Try a different search term.",
		icon = "🔍",
		actionText = if (onClearSearch != null) "Clear Search" else null,
		onAction = onClearSearch,
		modifier = modifier
	)
}

/**
 * Library empty state (for new users)
 */
@Composable
fun EmptyLibraryState(
	modifier: Modifier = Modifier
) {
	EmptyState(
		title = "Your Library is Empty",
		message = "Start adding content to your Jellyfin server to see it here.",
		icon = "📚",
		modifier = modifier
	)
}
