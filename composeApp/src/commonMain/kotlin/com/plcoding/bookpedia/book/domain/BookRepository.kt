package com.plcoding.bookpedia.book.domain

import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.EmptyResult
import com.plcoding.bookpedia.core.domain.Result
import kotlinx.coroutines.flow.Flow

/**
 * Defines the contract for accessing book data, both from a remote source and a local database.
 * This repository handles searching for books, retrieving book details, and managing a collection of favorite books.
 */
interface BookRepository {
    /**
     * Searches for books based on a given query string.
     *
     * This function queries a remote data source to find books that match the search term.
     * It's a suspend function, designed to be called from a coroutine.
     *
     * @param query The search term to use for finding books (e.g., author, title).
     * @return A [Result] object containing either a [List] of [Book] objects on success,
     *         or a [DataError.Remote] on failure.
     */
    suspend fun searchBooks(
        query: String
    ): Result<List<Book>, DataError.Remote>
    /**
     * Retrieves the description for a specific book.
     *
     * @param bookId The unique identifier of the book.
     * @return A [Result] containing the book's description as a [String] on success,
     * or a [DataError] on failure. The description can be null if it's not available.
     */
    suspend fun getBookDescription(
        bookId: String
    ): Result<String?, DataError>

    /**
     * Retrieves a continuous stream of the user's favorite books.
     *
     * This function returns a [Flow] that emits a new list of [Book] objects
     * whenever the collection of favorite books changes (e.g., a book is added or removed).
     * This allows for reactive UI updates based on the favorite books data.
     *
     * @return A [Flow] that emits a `List<Book>` representing the current favorite books.
     */
    fun getFavoriteBooks(): Flow<List<Book>>

    /**
     * Checks if a book is marked as a favorite.
     *
     * This function returns a cold Flow that emits a boolean value indicating whether the book
     * with the given ID is in the user's favorites list. The Flow will emit a new value
     * whenever the favorite status of the book changes.
     *
     * @param id The unique identifier of the book to check.
     * @return A [Flow] that emits `true` if the book is a favorite, and `false` otherwise.
     */
    fun isBookFavorite(
        id: String
    ): Flow<Boolean>

    /**
     * Adds a book to the local favorites list.
     *
     * This is a suspend function that performs an asynchronous operation to save the given book
     * to the local database as a favorite. It returns a result indicating whether the operation
     * was successful or if a local data error occurred.
     *
     * @param book The [Book] object to be marked as a favorite.
     * @return An [EmptyResult] which is either [com.plcoding.bookpedia.core.domain.EmptyResult.Success]
     *         or [com.plcoding.bookpedia.core.domain.EmptyResult.Error] containing a [DataError.Local].
     */
    suspend fun markAsFavorite(book: Book): EmptyResult<DataError.Local>

    /**
     * Deletes a book from the local favorites list.
     *
     * This function is a suspend function, meaning it should be called from a coroutine scope.
     * It operates on the local data source to remove the book identified by the given ID.
     *
     * @param id The unique identifier of the book to be removed from favorites.
     */
    suspend fun deleteFromFavorites(
        id: String
    )
}