package com.plcoding.bookpedia.book.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the favorite books table.
 *
 * This interface defines the methods for interacting with the `BookEntity` table in the
 * Room database. It provides functionality to insert, update, query, and delete favorite books.
 */
@Dao
interface FavoriteBookDao {

    /**
     * Inserts a book entity into the database or updates it if it already exists.
     * The "upsert" operation is based on the primary key of the [BookEntity]. If a book
     * with the same ID already exists, its record will be updated with the new data.
     *
     * @param book The [BookEntity] to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(book: BookEntity)

    /**
     * Retrieves all favorite books from the database as a reactive stream.
     *
     * @return A [Flow] that emits a list of all [BookEntity] objects. The flow will
     *         automatically emit a new list whenever the data in the `BookEntity` table changes.
     */
    @Query("SELECT * FROM BookEntity")
    fun getAllFavoriteBooks(): Flow<List<BookEntity>>

    /**
     * Retrieves a single favorite book by its unique ID.
     * This is a one-shot suspend function.
     *
     * @param id The unique identifier of the book to retrieve.
     * @return The [BookEntity] with the specified ID, or `null` if no such book is found.
     */
    @Query("SELECT * FROM BookEntity WHERE id = :id")
    suspend fun getFavoriteBook(id: String): BookEntity?

    /**
     * Deletes a favorite book from the database using its unique ID.
     *
     * @param id The unique identifier of the book to delete.
     */
    @Query("DELETE FROM BookEntity WHERE id = :id")
    suspend fun deleteFavoriteBook(id: String)
}
