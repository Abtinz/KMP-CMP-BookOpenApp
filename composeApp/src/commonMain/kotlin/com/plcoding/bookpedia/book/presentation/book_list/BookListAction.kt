package com.plcoding.bookpedia.book.presentation.book_list

import com.plcoding.bookpedia.book.domain.Book

/**
 * Represents all possible user actions that can be performed on the Book List screen.
 * This sealed interface is used to pass events from the UI to the ViewModel.
 */
sealed interface BookListAction {
    data class OnSearchQueryChange(val query: String): BookListAction
    data class OnBookClick(val book: Book): BookListAction
    data class OnTabSelected(val index: Int): BookListAction
}