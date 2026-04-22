package dev.guillemdiaz.freenowdemo.core.analytics

import android.util.Log
import jakarta.inject.Inject

/**
 * A debug implementation of [AnalyticsTracker] that outputs events to Logcat.
 */
class ConsoleAnalyticsTracker @Inject constructor() : AnalyticsTracker {

    /**
     * Formats and logs the event to Logcat under the "ANALYTICS_TRACKER" tag.
     * Output example: Event: [booking_confirmed] | Params: {vehicle_type=TAXI, price=16.60}
     */
    override fun trackEvent(eventName: String, parameters: Map<String, Any>?) {
        val paramsString = parameters?.entries?.joinToString { "${it.key}=${it.value}" } ?: "none"
        Log.d("ANALYTICS_TRACKER", "Event: [$eventName] | Params: {$paramsString}")
    }
}
