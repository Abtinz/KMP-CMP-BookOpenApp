package com.plcoding.bookpedia.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.plcoding.bookpedia.book.data.database.DatabaseFactory
import com.plcoding.bookpedia.book.data.database.FavoriteBookDatabase
import com.plcoding.bookpedia.book.data.network.KtorRemoteBookDataSource
import com.plcoding.bookpedia.book.data.network.RemoteBookDataSource
import com.plcoding.bookpedia.book.data.repository.DefaultBookRepository
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.book.presentation.SelectedBookViewModel
import com.plcoding.bookpedia.book.presentation.book_detail.BookDetailViewModel
import com.plcoding.bookpedia.book.presentation.book_list.BookListViewModel
import com.plcoding.bookpedia.core.data.HttpClientFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * A Koin module that provides platform-specific dependencies.
 *
 * This `expect` declaration is part of a Kotlin Multiplatform setup.
 * The `actual` implementation for each platform (e.g., Android, iOS)
 * will define the specific bindings required for that platform, such as
 * database drivers, context, or other platform-specific APIs.
 */
expect val platformModule: Module

/**
 * A Koin module that provides shared dependencies for the application,
 * common across different platforms (Android, iOS, etc.).
 *
 * This module is responsible for setting up:
 * - Networking components like the Ktor `HttpClient`.
 * - Data sources, such as the `RemoteBookDataSource`.
 * - Repositories, like the `BookRepository`.
 * - The application's database (`FavoriteBookDatabase`) and its DAOs (`favoriteBookDao`).
 * - Shared ViewModels used in the presentation layer.
 */
val sharedModule = module {
    single { HttpClientFactory.create(get()) }
    singleOf(::KtorRemoteBookDataSource).bind<RemoteBookDataSource>()
    singleOf(::DefaultBookRepository).bind<BookRepository>()

    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<FavoriteBookDatabase>().favoriteBookDao }

    viewModelOf(::BookListViewModel)
    viewModelOf(::BookDetailViewModel)
    viewModelOf(::SelectedBookViewModel)
}