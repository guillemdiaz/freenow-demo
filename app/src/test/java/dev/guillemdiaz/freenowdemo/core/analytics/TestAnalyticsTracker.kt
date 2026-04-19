package dev.guillemdiaz.freenowdemo.core.analytics

/**
 * A stub implementation of [AnalyticsTracker] for use in Unit Tests.
 * It records the last event so our tests can assert against it.
 */
class TestAnalyticsTracker : AnalyticsTracker {
    var lastEventName: String? = null
    var lastParameters: Map<String, Any>? = null

    override fun trackEvent(eventName: String, parameters: Map<String, Any>?) {
        lastEventName = eventName
        lastParameters = parameters
    }
}
