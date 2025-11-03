package org.jellyfin.androidtv.ui.composable.focus

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type

/**
 * D-Pad Key Event Handler
 *
 * Handles D-pad input for TV remote controls.
 * Provides callbacks for directional navigation and action buttons.
 */

/**
 * D-pad event types
 */
sealed class DPadEvent {
	data object Up : DPadEvent()
	data object Down : DPadEvent()
	data object Left : DPadEvent()
	data object Right : DPadEvent()
	data object Center : DPadEvent()
	data object Back : DPadEvent()
}

/**
 * Handles D-pad key events
 *
 * @param onEvent Callback for D-pad events, return true if handled
 */
@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.onDPadEvent(
	onEvent: (DPadEvent) -> Boolean
): Modifier = composed {
	this.onKeyEvent { keyEvent ->
		if (keyEvent.type == KeyEventType.KeyDown) {
			val dpadEvent = keyEvent.toDPadEvent() ?: return@onKeyEvent false
			onEvent(dpadEvent)
		} else {
			false
		}
	}
}

/**
 * Converts KeyEvent to DPadEvent
 */
@OptIn(ExperimentalComposeUiApi::class)
private fun KeyEvent.toDPadEvent(): DPadEvent? = when (key) {
	Key.DirectionUp, Key.W -> DPadEvent.Up
	Key.DirectionDown, Key.S -> DPadEvent.Down
	Key.DirectionLeft, Key.A -> DPadEvent.Left
	Key.DirectionRight, Key.D -> DPadEvent.Right
	Key.DirectionCenter, Key.Enter, Key.NumPadEnter -> DPadEvent.Center
	Key.Back, Key.Escape -> DPadEvent.Back
	else -> null
}

/**
 * Handles D-pad navigation for scrollable content
 *
 * @param onUp Callback for up navigation
 * @param onDown Callback for down navigation
 * @param onLeft Callback for left navigation
 * @param onRight Callback for right navigation
 * @param onSelect Callback for center/select button
 */
fun Modifier.handleDPadNavigation(
	onUp: (() -> Unit)? = null,
	onDown: (() -> Unit)? = null,
	onLeft: (() -> Unit)? = null,
	onRight: (() -> Unit)? = null,
	onSelect: (() -> Unit)? = null,
	onBack: (() -> Unit)? = null,
): Modifier = onDPadEvent { event ->
	when (event) {
		DPadEvent.Up -> onUp?.invoke()
		DPadEvent.Down -> onDown?.invoke()
		DPadEvent.Left -> onLeft?.invoke()
		DPadEvent.Right -> onRight?.invoke()
		DPadEvent.Center -> onSelect?.invoke()
		DPadEvent.Back -> onBack?.invoke()
	}?.let { true } ?: false
}
