package com.example.freenowdemo.feature.destination

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.freenowdemo.feature.destination.state.DestinationViewEffect

/**
 * Smart wrapper for [DestinationScreen] that owns the ViewModel, collects state,
 * and handles navigation effects.
 */
@Composable
fun DestinationRoute(
    isOffline: Boolean,
    onNavigateBack: () -> Unit,
    onNavigateBackWithResult: () -> Unit,
    viewModel: DestinationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DestinationViewEffect.NavigateBackWithResult -> onNavigateBackWithResult()
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
