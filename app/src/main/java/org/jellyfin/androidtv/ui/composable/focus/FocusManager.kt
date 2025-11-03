package org.jellyfin.androidtv.ui.composable.focus

import androidx.compose.foundation.focusable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged

/**
 * TV Focus Manager
 *
 * Manages D-pad focus for TV navigation. Provides utilities for:
 * - Focus tracking and restoration
 * - D-pad input handling
 * - Focus indicators
 * - Hierarchical focus management
 */

/**
 * Modifier for TV-style focusable elements with visual feedback
 *
 * @param onFocus Callback when element gains focus
 * @param onFocusLost Callback when element loses focus
 */
fun Modifier.tvFocusable(
	onFocus: () -> Unit = {},
	onFocusLost: () -> Unit = {},
): Modifier = composed {
	var isFocused by remember { mutableStateOf(false) }

	this
		.onFocusChanged { focusState ->
			val wasFocused = isFocused
			isFocused = focusState.isFocused

			when {
				isFocused && !wasFocused -> onFocus()
				!isFocused && wasFocused -> onFocusLost()
			}
		}
		.focusable()
}

/**
 * Remembers a FocusRequester for programmatic focus control
 */
@Composable
fun rememberTvFocusRequester(): FocusRequester {
	return remember { FocusRequester() }
}

/**
 * Modifier to attach a FocusRequester and make element TV-focusable
 */
fun Modifier.tvFocusRequester(focusRequester: FocusRequester): Modifier {
	return this
		.focusRequester(focusRequester)
		.focusable()
}

/**
 * Focus group for managing focus within a collection of items
 *
 * @param itemCount Number of items in the group
 * @param initialFocusIndex Index of initially focused item
 */
@Composable
fun rememberFocusGroup(
	itemCount: Int,
	initialFocusIndex: Int = 0
): FocusGroup {
	return remember(itemCount) {
		FocusGroup(
			itemCount = itemCount,
			initialFocusIndex = initialFocusIndex
		)
	}
}

/**
 * Focus group state manager
 */
class FocusGroup(
	private val itemCount: Int,
	initialFocusIndex: Int = 0
) {
	var focusedIndex by mutableStateOf(initialFocusIndex)
		private set

	val focusRequesters = List(itemCount) { FocusRequester() }

	fun moveFocusTo(index: Int) {
		if (index in 0 until itemCount) {
			focusedIndex = index
			focusRequesters[index].requestFocus()
		}
	}

	fun moveFocusNext() {
		val nextIndex = (focusedIndex + 1) % itemCount
		moveFocusTo(nextIndex)
	}

	fun moveFocusPrevious() {
		val prevIndex = if (focusedIndex > 0) focusedIndex - 1 else itemCount - 1
		moveFocusTo(prevIndex)
	}

	fun requestInitialFocus() {
		focusRequesters[focusedIndex].requestFocus()
	}
}
