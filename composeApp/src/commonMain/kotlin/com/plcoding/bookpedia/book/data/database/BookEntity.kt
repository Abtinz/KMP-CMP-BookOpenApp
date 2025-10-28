package com.plcoding.bookpedia.book.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a book entity in the local database.
 * This data class is used by Room to create the 'BookEntity' table.
 *
 * @property id The unique identifier for the book, used as the primary key. This is typically sourced from an external API (e.g., OpenLibrary ID).
 * @property title The title of the book.
 * @property description A brief description or summary of the book. Can be null if not available.
 * @property imageUrl The URL for the book's cover image.
 * @property languages A list of languages the book is available in.
 * @property authors A list of the book's authors.
 * @property firstPublishYear The year the book was first published. Can be null if not available.
 * @property ratingsAverage The average rating of the book. Can be null if not available.
 * @property ratingsCount The total number of ratings the book has received. Can be null if not available.
 * @property numPagesMedian The median number of pages across different editions. Can be null if not available.
 * @property numEditions The total number of known editions for the book.
 */
@Entity
data class BookEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val title: String,
    val description: String?,
    val imageUrl: String,
    val languages: List<String>,
    val authors: List<String>,
    val firstPublishYear: String?,
    val ratingsAverage: Double?,
    val ratingsCount: Int?,
    val numPagesMedian: Int?,
    val numEditions: Int
)
