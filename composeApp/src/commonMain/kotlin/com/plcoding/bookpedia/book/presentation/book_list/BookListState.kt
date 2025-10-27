package com.plcoding.bookpedia.book.presentation.book_list

import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.core.presentation.UiText

/**
 * Represents the state of the book list screen.
 *
 * This data class holds all the necessary information to render the UI for the book list,
 * including search queries, results, favorite books, loading status, and error messages.
 *
 * @property searchQuery The current text entered by the user in the search bar. Defaults to "Kotlin".
 * @property searchResults The list of books returned from the search query.
 * @property favoriteBooks The list of books that the user has marked as favorites.
 * @property isLoading A boolean flag indicating whether a data loading operation (like a search) is in progress.
 * @property selectedTabIndex The index of the currently selected tab (e.g., 0 for "Search", 1 for "Favorites").
 * @property errorMessage A user-facing error message, wrapped in a [UiText] for localization. Null if there is no error.
 */
data class BookListState(
    val searchQuery: String = "Kotlin",
    val searchResults: List<Book> = emptyList(),
    val favoriteBooks: List<Book> = emptyList(),
    val isLoading: Boolean = true,
    val selectedTabIndex: Int = 0,
    val errorMessage: UiText? = null
)