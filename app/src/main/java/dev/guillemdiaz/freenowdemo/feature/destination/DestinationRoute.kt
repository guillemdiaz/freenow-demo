package dev.guillemdiaz.freenowdemo.feature.destination

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.guillemdiaz.freenowdemo.feature.destination.state.DestinationViewEffect

/**
 * Smart wrapper for [DestinationScreen] that owns the ViewModel, collects state,
 * and handles navigation effects.
 * @param isOffline Indicates if the app has lost network connectivity.
 * @param onNavigateBack Callback to pop the backstack without returning a result.
 * @param onNavigateBackWithResult Callback to pop the backstack and pass the selected pickup/dropoff data back to the previous screen.
 * @param viewModel The Hilt-injected state holder for this feature.
 */
@Composable
fun DestinationRoute(
    isOffline: Boolean,
    onNavigateBack: () -> Unit,
    onNavigateBackWithResult: (String, String) -> Unit,
    viewModel: DestinationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DestinationViewEffect.NavigateBackWithResult -> {
                    onNavigateBackWithResult(effect.pickup, effect.dropoff)
                }
            }
        }
    }

    DestinationScreen(
        isOffline = isOffline,
        state = state,
        onIntent = viewModel::processIntent,
        onBackClick = onNavigateBack,
        onAddStopClick = { /* TODO */ }
    )
}
