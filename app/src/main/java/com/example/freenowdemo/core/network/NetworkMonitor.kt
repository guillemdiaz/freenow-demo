package com.example.freenowdemo.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Observes the device's network connectivity state and exposes it as a [Flow].
 * Uses [ConnectivityManager.NetworkCallback] to react to connectivity changes in real time,
 * so consumers are notified immediately when the device goes online or offline.
 */
class NetworkMonitor @Inject constructor(@param:ApplicationContext private val context: Context) {

    /**
     * A [Flow] that emits true when the device has an active network connection
     * and false when connectivity is lost. Emits the current state immediately upon collection.
     */
    val isOnline: Flow<Boolean> = callbackFlow {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }
            override fun onLost(network: Network) {
                trySend(false)
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)

        // Emit initial state
        val isConnected = connectivityManager.activeNetwork != null
        trySend(isConnected)

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }
}
