package com.plcoding.bookpedia.book.data.network

import com.plcoding.bookpedia.book.data.dto.BookWorkDto
import com.plcoding.bookpedia.book.data.dto.SearchResponseDto
import com.plcoding.bookpedia.book.data.network.ApiUtils.BASE_URL
import com.plcoding.bookpedia.book.data.network.ApiUtils.SEARCH_URL
import com.plcoding.bookpedia.core.data.safeCall
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * An implementation of [RemoteBookDataSource] that uses the Ktor HTTP client
 * to fetch book data from a remote API.
 *
 * This class is responsible for making the actual network calls and wrapping the
 * responses or any potential errors into a [Result] object using the [safeCall] utility function.
 *
 * @property httpClient The Ktor [HttpClient] instance used to perform network requests.
 */
class KtorRemoteBookDataSource(
    private val httpClient: HttpClient
): RemoteBookDataSource {

    /**
     * Searches for books based on a query string.
     *
     * It makes a GET request to the search endpoint of the API, passing the query,
     * result limit, and other parameters to filter and define the fields in the response.
     *
     * @param query The search term to look for books.
     * @param resultLimit The maximum number of search results to return.
     * @return A [Result] object which is either a [Result.Success] containing a [SearchResponseDto]
     *         or a [Result.Error] containing a [DataError.Remote].
     */
    override suspend fun searchBooks(
        query: String,
        resultLimit: Int?
    ): Result<SearchResponseDto, DataError.Remote> {
        return safeCall<SearchResponseDto> {
            httpClient.get(
                urlString = SEARCH_URL
            ) {
                parameter("q", query)
                parameter("limit", resultLimit)
                parameter("language", "eng")
                parameter("fields", "key,title,author_name,author_key,cover_edition_key,cover_i,ratings_average,ratings_count,first_publish_year,language,number_of_pages_median,edition_count")
            }
        }
    }

    /**
     * Fetches the detailed information for a specific book work using its ID.
     *
     * It makes a GET request to the API endpoint for a specific book work.
     *
     * @param bookWorkId The unique identifier of the book work (e.g., "OL45883W").
     * @return A [Result] object which is either a [Result.Success] containing a [BookWorkDto]
     *         or a [Result.Error] containing a [DataError.Remote].
     */
    override suspend fun getBookDetails(bookWorkId: String): Result<BookWorkDto, DataError.Remote> {
        return safeCall<BookWorkDto> {
            httpClient.get(
                urlString = "$BASE_URL/works/$bookWorkId.json"
            )
        }
    }
}
