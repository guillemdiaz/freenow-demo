package dev.guillemdiaz.freenowdemo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import dev.guillemdiaz.freenowdemo.core.network.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Holds app-wide UI state that needs to be shared across the entire app.
 * @param coroutineScope The scope used to convert flows into [StateFlow]s.
 * @param networkMonitor The monitor used to observe network connectivity changes.
 */
@Stable
class FreenowAppState(coroutineScope: CoroutineScope, networkMonitor: NetworkMonitor) {

    /**
     * Derived from [NetworkMonitor.isOnline] - true when the device has no active network.
     */
    val isOffline: StateFlow<Boolean> = networkMonitor.isOnline
        .map { !it }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )
}

/**
 * Creates and remembers a [FreenowAppState] instance across recompositions.
 */
@Composable
fun rememberFreenowAppState(
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): FreenowAppState = remember(networkMonitor, coroutineScope) {
    FreenowAppState(coroutineScope, networkMonitor)
}
