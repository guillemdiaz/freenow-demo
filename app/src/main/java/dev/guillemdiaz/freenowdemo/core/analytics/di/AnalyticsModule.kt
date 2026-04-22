package dev.guillemdiaz.freenowdemo.core.analytics.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.guillemdiaz.freenowdemo.core.analytics.AnalyticsTracker
import dev.guillemdiaz.freenowdemo.core.analytics.ConsoleAnalyticsTracker
import jakarta.inject.Singleton

/**
 * Hilt module that binds the analytics interface to its console implementation.
 * Installed in [SingletonComponent] so a single tracker instance is shared app-wide.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    /**
     * Tells Hilt to inject [ConsoleAnalyticsTracker] whenever [AnalyticsTracker] is requested.
     * Swap the binding here to switch to a different analytics provider (e.g. Firebase).
     */
    @Binds
    @Singleton
    abstract fun bindAnalyticsTracker(consoleAnalyticsTracker: ConsoleAnalyticsTracker): AnalyticsTracker
}
