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
 * Error State Composable
 *
 * Shows error message with optional retry action for TV screens.
 *
 * @param title Error title (e.g., "Something went wrong")
 * @param message Detailed error message
 * @param onRetry Optional retry action callback
 * @param retryText Text for retry button
 * @param modifier Modifier for the container
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ErrorState(
	title: String = "Something went wrong",
	message: String = "An error occurred while loading content.",
	onRetry: (() -> Unit)? = null,
	retryText: String = "Retry",
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier
			.fillMaxSize()
			.padding(horizontal = 48.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		// Error icon (using text for now, can be replaced with icon)
		Text(
			text = "⚠️",
			style = MaterialTheme.typography.displayMedium,
			color = MaterialTheme.colorScheme.error
		)

		Spacer(modifier = Modifier.height(24.dp))

		Text(
			text = title,
			style = MaterialTheme.typography.headlineMedium,
			color = MaterialTheme.colorScheme.onBackground,
			textAlign = TextAlign.Center
		)

		Spacer(modifier = Modifier.height(16.dp))

		Text(
			text = message,
			style = MaterialTheme.typography.bodyLarge,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(horizontal = 32.dp)
		)

		if (onRetry != null) {
			Spacer(modifier = Modifier.height(32.dp))

			Button(
				onClick = onRetry
			) {
				Text(text = retryText)
			}
		}
	}
}

/**
 * Network error state specifically for connection issues
 */
@Composable
fun NetworkErrorState(
	onRetry: (() -> Unit)? = null,
	modifier: Modifier = Modifier
) {
	ErrorState(
		title = "No Connection",
		message = "Unable to connect to the Jellyfin server. Please check your network connection and try again.",
		onRetry = onRetry,
		retryText = "Retry Connection",
		modifier = modifier
	)
}
