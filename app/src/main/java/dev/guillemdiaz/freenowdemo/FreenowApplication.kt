package dev.guillemdiaz.freenowdemo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Base Application class required to trigger Hilt's code generation and
 * initialize the dependency injection graph.
 */
@HiltAndroidApp
class FreenowApplication : Application()
