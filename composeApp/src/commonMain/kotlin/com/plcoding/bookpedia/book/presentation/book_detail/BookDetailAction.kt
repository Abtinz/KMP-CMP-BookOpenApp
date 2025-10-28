package com.plcoding.bookpedia.book.presentation.book_detail

import com.plcoding.bookpedia.book.domain.Book

/**
 * Represents the various user actions that can occur on the book detail screen.
 * This sealed interface is used to pass events from the UI to the ViewModel.
 */
sealed interface BookDetailAction {
    data object OnBackClick: BookDetailAction
    data object OnFavoriteClick: BookDetailAction
    data class OnSelectedBookChange(val book: Book): BookDetailAction
}