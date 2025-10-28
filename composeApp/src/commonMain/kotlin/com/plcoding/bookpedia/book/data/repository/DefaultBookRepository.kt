package com.plcoding.bookpedia.book.data.repository

import androidx.sqlite.SQLiteException
import com.plcoding.bookpedia.book.data.database.FavoriteBookDao
import com.plcoding.bookpedia.book.data.mappers.toBook
import com.plcoding.bookpedia.book.data.mappers.toBookEntity
import com.plcoding.bookpedia.book.data.network.RemoteBookDataSource
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.EmptyResult
import com.plcoding.bookpedia.core.domain.Result
import com.plcoding.bookpedia.core.domain.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Default implementation of the [BookRepository].
 * This class acts as the single source of truth for book-related data, coordinating
 * between the remote data source (network) and the local data source (database).
 *
 * @property remoteBookDataSource The data source for fetching book data from the network.
 * @property favoriteBookDao The Data Access Object for interacting with the local favorites database.
 */
class DefaultBookRepository(
    private val remoteBookDataSource: RemoteBookDataSource,
    private val favoriteBookDao: FavoriteBookDao
): BookRepository {

    /**
     * Searches for books via the remote data source and maps the DTOs to domain [Book] models.
     *
     * @param query The search term to use for finding books.
     * @return A [Result] containing a list of [Book] objects on success, or a [DataError.Remote] on failure.
     */
    override suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote> {
        return remoteBookDataSource
            .searchBooks(query)
            .map { dto ->
                // Maps the list of result DTOs to a list of domain Book objects.
                dto.results.map { it.toBook() }
            }
    }

    /**
     * Retrieves the description of a book.
     * It first attempts to find the book in the local favorites database. If found, it returns
     * the local description. Otherwise, it fetches the book details from the remote source.
     *
     * @param bookId The unique ID of the book.
     * @return A [Result] containing the book's description string (which can be null) on success,
     *         or a [DataError] on failure.
     */
    override suspend fun getBookDescription(bookId: String): Result<String?, DataError> {
        val localResult = favoriteBookDao.getFavoriteBook(bookId)

        return if(localResult == null) {
            // If the book is not in the local database, fetch from the remote source.
            remoteBookDataSource
                .getBookDetails(bookId)
                .map { it.description }
        } else {
            // If found locally, return the description from the database.
            Result.Success(localResult.description)
        }
    }

    /**
     * Retrieves a real-time flow of all favorite books from the local database.
     *
     * @return A [Flow] emitting a list of [Book] objects. The flow will emit a new list
     *         whenever the favorite books data changes in the database.
     */
    override fun getFavoriteBooks(): Flow<List<Book>> {
        return favoriteBookDao
            .getFavoriteBooks()
            .map { bookEntities ->
                // Maps the list of database entities to a list of domain Book objects.
                bookEntities.map { it.toBook() }
            }
    }

    /**
     * Returns a flow that indicates whether a book with the given ID is marked as a favorite.
     *
     * @param id The unique ID of the book to check.
     * @return A [Flow] emitting `true` if the book is a favorite, and `false` otherwise.
     *         The flow will update automatically if the book's favorite status changes.
     */
    override fun isBookFavorite(id: String): Flow<Boolean> {
        return favoriteBookDao
            .getFavoriteBooks()
            .map { bookEntities ->
                // Checks if any book in the list of favorites has the matching ID.
                bookEntities.any { it.id == id }
            }
    }

    /**
     * Marks a book as a favorite by inserting or updating it in the local database.
     * This operation is commonly known as "upsert".
     *
     * @param book The [Book] to be marked as a favorite.
     * @return An [EmptyResult] indicating success or a [DataError.Local] on failure,
     *         such as when the disk is full.
     */
    override suspend fun markAsFavorite(book: Book): EmptyResult<DataError.Local> {
        return try {
            favoriteBookDao.upsert(book.toBookEntity())
            Result.Success(Unit) // Unit indicates a successful operation with no return value.
        } catch(e: SQLiteException) {
            // Catches database-related exceptions, like disk space issues.
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    /**
     * Deletes a book from the local favorites database.
     *
     * @param id The unique ID of the book to remove from favorites.
     */
    override suspend fun deleteFromFavorites(id: String) {
        favoriteBookDao.deleteFavoriteBook(id)
    }
}
