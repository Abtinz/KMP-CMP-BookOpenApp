package com.plcoding.bookpedia.book.domain

/**
 * Represents a book entity with its detailed information.
 *
 * This data class models a book, containing various attributes fetched from a data source.
 * It's a core domain model for the application.
 *
 * @property id The unique identifier for the book.
 * @property title The title of the book.
 * @property imageUrl The URL for the book's cover image.
 * @property authors A list of authors who wrote the book.
 * @property description A summary or description of the book's content.
 *                      Can be null if not available.
 * @property languages A list of languages the book is available in.
 * @property firstPublishYear The year the book was first published. Can be null if unknown.
 * @property averageRating The average user rating for the book, on a scale (e.g., 1-5). Can be null.
 * @property ratingCount The total number of ratings the book has received. Can be null.
 * @property numPages The number of pages in the book. Can be null if not specified.
 * @property numEditions The total number of editions published for this book.
 */
data class Book(
    val id: String,
    val title: String,
    val imageUrl: String,
    val authors: List<String>,
    val description: String?,
    val languages: List<String>,
    val firstPublishYear: String?,
    val averageRating: Double?,
    val ratingCount: Int?,
    val numPages: Int?,
    val numEditions: Int
)
