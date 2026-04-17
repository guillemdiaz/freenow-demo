package com.example.freenowdemo.core.analytics

/**
 * A generic interface for tracking user events and system actions.
 * Decouples the app from any specific analytics SDK (like Firebase or Mixpanel).
 */
interface AnalyticsTracker {

    /**
     * Tracks a named event with optional metadata.
     * @param eventName Identifier for the event (e.g. "booking_confirmed").
     * @param parameters Optional key-value pairs with extra context (e.g. vehicle type, price).
     */
    fun trackEvent(eventName: String, parameters: Map<String, Any>? = null)
}
