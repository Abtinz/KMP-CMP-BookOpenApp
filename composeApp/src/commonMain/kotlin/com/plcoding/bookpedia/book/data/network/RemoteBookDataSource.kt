package com.plcoding.bookpedia.book.data.network

import com.plcoding.bookpedia.book.data.dto.BookWorkDto
import com.plcoding.bookpedia.book.data.dto.SearchResponseDto
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result

/**
 * Defines the contract for a remote data source for book-related data.
 * This interface abstracts the underlying data fetching mechanism (e.g., a REST API)
 * for retrieving book information from a remote server.
 */
interface RemoteBookDataSource {
    /**
     * Searches for books based on a given query.
     *
     * This function communicates with the remote API to find books matching the search term.
     * It allows specifying a limit on the number of results returned.
     *
     * @param query The search term to look for (e.g., book title, author).
     * @param resultLimit An optional integer to limit the number of search results. Defaults to the API's default if null.
     * @return A [Result] object which is either a [Result.Success] containing a [SearchResponseDto]
     * or a [Result.Error] containing a [DataError.Remote].
     */
    suspend fun searchBooks(
        query: String,
        resultLimit: Int? = null
    ): Result<SearchResponseDto, DataError.Remote>

    /**
     * Fetches the detailed information for a specific book work from the remote data source.
     *
     * @param bookWorkId The unique identifier for the book work (e.g., "OL45883W").
     * @return A [Result] object containing either the [BookWorkDto] on success or a [DataError.Remote] on failure.
     */
    suspend fun getBookDetails(bookWorkId: String): Result<BookWorkDto, DataError.Remote>
}