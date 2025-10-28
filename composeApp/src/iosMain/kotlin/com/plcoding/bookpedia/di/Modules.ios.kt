package com.plcoding.bookpedia.di

import com.plcoding.bookpedia.book.data.database.DatabaseFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * A Koin module that provides platform-specific dependencies for the iOS target.
 *
 * This module configures the following dependencies:
 * - [HttpClientEngine]: Provides the `Darwin` engine for Ktor, which is the native
 *   networking engine for iOS and other Apple platforms.
 * - [DatabaseFactory]: Provides the factory for creating the platform-specific
 *   database driver instance (SQLite).
 */
actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { Darwin.create() }
        single { DatabaseFactory() }
    }