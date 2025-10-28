@file:OptIn(FlowPreview::class)

package com.plcoding.bookpedia.book.presentation.book_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.onError
import com.plcoding.bookpedia.core.domain.onSuccess
import com.plcoding.bookpedia.core.presentation.toUiText
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * The ViewModel for the book list screen.
 *
 * This class is responsible for managing the UI state, handling user actions, and interacting
 * with the [BookRepository] to fetch search results and favorite books. It uses Kotlin Flows
 * to create a reactive UI.
 *
 * @param bookRepository The repository used to fetch book data.
 */
class BookListViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {

    // A local cache to hold the last successful search results.
    private var cachedBooks = emptyList<Book>()
    // A reference to the running search coroutine, allowing it to be cancelled.
    private var searchJob: Job? = null
    // A reference to the coroutine observing favorite books, allowing it to be cancelled and restarted.
    private var observeFavoriteJob: Job? = null

    // The private, mutable state flow that holds the current state of the UI.
    private val _state = MutableStateFlow(BookListState())
    /**
     * The public, read-only state flow that the UI observes for updates.
     * It's configured to start observing the search query and favorite books when a collector
     * subscribes to it.
     *
     * - [stateIn] converts the cold flow into a hot [kotlinx.coroutines.flow.StateFlow], caching the latest value.
     * - [SharingStarted.WhileSubscribed(5000L)] keeps the upstream flow active for 5 seconds
     *   after the last collector disappears, which is useful for surviving configuration changes.
     */
    val state = _state
        .onStart {
            // Initialization logic when the flow is first collected.
            if(cachedBooks.isEmpty()) {
                observeSearchQuery()
            }
            observeFavoriteBooks()
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
    fun onAction(action: BookListAction) {
        when (action) {
            is BookListAction.OnBookClick -> {

            }

            is BookListAction.OnSearchQueryChange -> {
                // Updates the search query in the state as the user types.
                _state.update {
                    it.copy(searchQuery = action.query)
                }
            }

            is BookListAction.OnTabSelected -> {
                // Updates the selected tab index in the state.
                _state.update {
                    it.copy(selectedTabIndex = action.index)
                }
            }
        }
    }

    /**
     * Observes the local database for changes in the list of favorite books and updates the UI state accordingly.
     * Ensures that any previous observation job is cancelled to avoid multiple collectors.
     */
    private fun observeFavoriteBooks() {
        observeFavoriteJob?.cancel()
        observeFavoriteJob = bookRepository
            .getFavoriteBooks()
            .onEach { favoriteBooks ->
                _state.update { it.copy(
                    favoriteBooks = favoriteBooks
                ) }
            }
            .launchIn(viewModelScope)
    }

    /**
     * Observes changes to the search query from the UI state.
     * It uses flow operators to make the search efficient and responsive.
     */
    private fun observeSearchQuery() {
        state
            .map { it.searchQuery } // Extract the search query from the state.
            .distinctUntilChanged() // Only proceed if the query has actually changed.
            .debounce(500L) // Wait for 500ms of inactivity before processing the query to avoid excessive API calls.
            .onEach { query ->
                when {
                    // If the query is blank, clear the error and show the cached results.
                    query.isBlank() -> {
                        _state.update {
                            it.copy(
                                errorMessage = null,
                                searchResults = cachedBooks
                            )
                        }
                    }
                    // Only trigger a search if the query is at least 2 characters long.
                    query.length >= 2 -> {
                        searchJob?.cancel() // Cancel any ongoing search before starting a new one.
                        searchJob = searchBooks(query)
                    }
                }
            }
            .launchIn(viewModelScope) // Start collecting this flow within the ViewModel's lifecycle.
    }

    /**
     * Executes the book search by calling the repository.
     * It updates the UI state to show a loading indicator, and then updates again with
     * either the search results or an error message.
     *
     * @param query The search query to pass to the repository.
     * @return A [Job] representing the launched coroutine.
     */
    private fun searchBooks(query: String) = viewModelScope.launch {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        bookRepository
            .searchBooks(query)
            .onSuccess { searchResults ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        searchResults = searchResults
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        searchResults = emptyList(),
                        isLoading = false,
                        errorMessage = error.toUiText()
                    )
                }
            }
    }
}
