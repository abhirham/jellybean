package org.jellyfin.androidtv.ui.composable.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text

/**
 * Loading State Composable
 *
 * Shows loading indicator with optional message for TV screens.
 *
 * @param message Optional loading message
 * @param modifier Modifier for the container
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun LoadingState(
	message: String = "Loading...",
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		// Simple loading indicator - can be enhanced with animation later
		Box(
			modifier = Modifier
				.size(64.dp)
				.background(
					color = MaterialTheme.colorScheme.primary,
					shape = CircleShape
				),
			contentAlignment = Alignment.Center
		) {
			Text(
				text = "...",
				style = MaterialTheme.typography.displaySmall,
				color = MaterialTheme.colorScheme.onPrimary
			)
		}

		Spacer(modifier = Modifier.height(24.dp))

		Text(
			text = message,
			style = MaterialTheme.typography.bodyLarge,
			color = MaterialTheme.colorScheme.onBackground
		)
	}
}

/**
 * Compact loading indicator for inline loading states
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun CompactLoadingIndicator(
	modifier: Modifier = Modifier,
	size: Int = 32
) {
	Box(
		modifier = modifier
			.size(size.dp)
			.background(
				color = MaterialTheme.colorScheme.primary,
				shape = CircleShape
			),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = "...",
			style = MaterialTheme.typography.labelSmall,
			color = MaterialTheme.colorScheme.onPrimary
		)
	}
}
