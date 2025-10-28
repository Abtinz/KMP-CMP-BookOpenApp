package com.plcoding.bookpedia.book.presentation.book_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.plcoding.bookpedia.app.Route
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * The ViewModel for the book detail screen.
 *
 * This class is responsible for managing the UI state for a single book's detail view.
 * It fetches the book's description, observes its favorite status, and handles user
 * actions like toggling the favorite status. The book's ID is retrieved from the
 * navigation arguments via [SavedStateHandle].
 *
 * @param bookRepository The repository used to fetch book data and manage favorites.
 * @param savedStateHandle A handle to the saved state and navigation arguments,
 *                         used here to retrieve the book ID.
 */
class BookDetailViewModel(
    private val bookRepository: BookRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    /**
     * Retrieves the book ID from the navigation arguments using a typesafe navigation helper.
     * This ID is used for all data fetching operations within this ViewModel.
     */
    private val bookId = savedStateHandle.toRoute<Route.BookDetail>().id

    // The private, mutable state flow that holds the current state of the UI.
    private val _state = MutableStateFlow(BookDetailState())

    /**
     * The public, read-only state flow that the UI observes for updates.
     * It's configured to start fetching the book description and observing its favorite
     * status as soon as a collector subscribes to it.
     *
     * - [stateIn] converts the cold flow into a hot [StateFlow], caching the latest value.
     * - [SharingStarted.WhileSubscribed(5000L)] keeps the upstream flow active for 5 seconds
     *   after the last collector disappears, useful for surviving configuration changes.
     */
    val state = _state
        .onStart {
            // Initialization logic when the flow is first collected.
            fetchBookDescription()
            observeFavoriteStatus()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    /**
     * Handles UI actions dispatched from the view.
     *
     * @param action The specific user action to be processed.
     */
    fun onAction(action: BookDetailAction) {
        when(action) {
            /**
             * This action is triggered when the selected book data (which might be passed
             * from the previous screen) is available, updating the state with this initial data.
             */
            is BookDetailAction.OnSelectedBookChange -> {
                _state.update { it.copy(
                    book = action.book
                ) }
            }
            /**
             * Handles the click on the favorite button. It checks the current favorite
             * status from the state and either adds the book to or removes it from favorites.
             */
            is BookDetailAction.OnFavoriteClick -> {
                viewModelScope.launch {
                    if(state.value.isFavorite) {
                        bookRepository.deleteFromFavorites(bookId)
                    } else {
                        // Ensure the book data is available before marking as favorite.
                        state.value.book?.let { book ->
                            bookRepository.markAsFavorite(book)
                        }
                    }
                }
            }
            // Ignores any other actions.
            else -> Unit
        }
    }

    /**
     * Observes the favorite status of the current book from the repository.
     * The returned [Flow] emits a new boolean value whenever the favorite status changes,
     * which then updates the UI state.
     */
    private fun observeFavoriteStatus() {
        bookRepository
            .isBookFavorite(bookId)
            .onEach { isFavorite ->
                _state.update { it.copy(
                    isFavorite = isFavorite
                ) }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Fetches the full description for the current book from the repository.
     * On success, it updates the book object in the state with the new description
     * and sets the loading state to false.
     */
    private fun fetchBookDescription() {
        viewModelScope.launch {
            bookRepository
                .getBookDescription(bookId)
                .onSuccess { description ->
                    _state.update { it.copy(
                        // Updates the description of the existing book object in the state.
                        book = it.book?.copy(
                            description = description
                        ),
                        isLoading = false
                    ) }
                }
        }
    }
}
