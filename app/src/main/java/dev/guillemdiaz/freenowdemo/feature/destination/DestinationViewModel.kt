package dev.guillemdiaz.freenowdemo.feature.destination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.guillemdiaz.freenowdemo.feature.destination.state.DestinationViewEffect
import dev.guillemdiaz.freenowdemo.feature.destination.state.DestinationViewIntent
import dev.guillemdiaz.freenowdemo.feature.destination.state.DestinationViewState
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Destination feature, implemented following the MVI (Model-View-Intent) pattern.
 * Responsibilities:
 * - Holds and exposes the [DestinationViewState] as an observable [StateFlow].
 * - Processes user [DestinationViewIntent]s and translates them into state mutations or side effects.
 * - Emits one-shot [DestinationViewEffect]s via a [Channel].
 */
@HiltViewModel
class DestinationViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(DestinationViewState())
    val state = _state.asStateFlow()

    // Channel guarantees UI events are never dropped during configuration changes
    private val _effect = Channel<DestinationViewEffect>()
    val effect = _effect.receiveAsFlow()

    /**
     * Single entry point for all user interactions and system events.
     * Dispatches each [DestinationViewIntent] to the appropriate private handler.
     * @param intent The action triggered by the user or the system.
     */
    fun processIntent(intent: DestinationViewIntent) {
        when (intent) {
            is DestinationViewIntent.UpdatePickup -> updatePickup(intent.text)

            is DestinationViewIntent.UpdateDropoff -> updateDropoff(intent.text)

            is DestinationViewIntent.ConfirmClicked -> {
                viewModelScope.launch {
                    _effect.send(
                        DestinationViewEffect.NavigateBackWithResult(
                            pickup = _state.value.pickupText,
                            dropoff = _state.value.dropoffText
                        )
                    )
                }
            }
        }
    }

    /**
     * Updates the pickup text and recalculates whether the confirm button should be enabled.
     */
    private fun updatePickup(newText: String) {
        _state.update {
            it.copy(
                pickupText = newText,
                isConfirmEnabled = newText.isNotBlank() && it.dropoffText.isNotBlank()
            )
        }
    }

    /**
     * Updates the dropoff text and recalculates whether the confirm button should be enabled.
     */
    private fun updateDropoff(newText: String) {
        _state.update {
            it.copy(
                dropoffText = newText,
                isConfirmEnabled = newText.isNotBlank() && it.pickupText.isNotBlank()
            )
        }
    }
}
