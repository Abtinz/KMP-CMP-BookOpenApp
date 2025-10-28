package com.plcoding.bookpedia.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Initializes the Koin dependency injection framework.
 * This function sets up the necessary modules for the application.
 *
 * @param config An optional Koin application declaration block for additional, platform-specific configuration.
 *               This is typically used in Android to provide the Android context.
 */
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule, platformModule)
    }
}