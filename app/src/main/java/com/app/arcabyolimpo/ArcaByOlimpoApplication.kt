package com.app.arcabyolimpo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom Application class for the app.
 *
 * This class initializes Dagger Hilt for dependency injection
 * across the entire application. By annotating it with
 * @HiltAndroidApp, Hilt automatically generates and attaches
 * the base dependency graph to the application lifecycle.
 */
@HiltAndroidApp
class ArcaByOlimpoApplication : Application()
