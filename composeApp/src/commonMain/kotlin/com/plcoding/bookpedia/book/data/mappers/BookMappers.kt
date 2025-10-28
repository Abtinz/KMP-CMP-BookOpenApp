package com.plcoding.bookpedia.book.data.mappers

import com.plcoding.bookpedia.book.data.database.BookEntity
import com.plcoding.bookpedia.book.data.dto.SearchedBookDto
import com.plcoding.bookpedia.book.domain.Book

/**
 * Maps a [SearchedBookDto] from the Open Library API to a [Book] domain model.
 *
 * This function handles the transformation of data from the network layer (DTO)
 * to the domain layer. It extracts the book ID from the full key, constructs the
 * cover image URL using the available keys, and handles nullable fields by providing
 * default or empty values.
 *
 * @receiver The [SearchedBookDto] instance to map.
 * @return A [Book] object populated with data from the DTO.
 */
fun SearchedBookDto.toBook(): Book {
    return Book(
        id = id.substringAfterLast("/"),
        title = title,
        imageUrl = if(coverKey != null) {
            "https://covers.openlibrary.org/b/olid/${coverKey}-L.jpg"
        } else {
            "https://covers.openlibrary.org/b/id/${coverAlternativeKey}-L.jpg"
        },
        authors = authorNames ?: emptyList(),
        description = null,
        languages = languages ?: emptyList(),
        firstPublishYear = firstPublishYear.toString(),
        averageRating = ratingsAverage,
        ratingCount = ratingsCount,
        numPages = numPagesMedian,
        numEditions = numEditions ?: 0
    )
}

/**
 * Maps a [Book] domain model to a [BookEntity] for database storage.
 * This is an extension function on the [Book] class.
 *
 * @return A [BookEntity] instance with data mapped from the [Book] object.
 */
fun Book.toBookEntity(): BookEntity {
    return BookEntity(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        languages = languages,
        authors = authors,
        firstPublishYear = firstPublishYear,
        ratingsAverage = averageRating,
        ratingsCount = ratingCount,
        numPagesMedian = numPages,
        numEditions = numEditions
    )
}

fun BookEntity.toBook(): Book {
    return Book(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        languages = languages,
        authors = authors,
        firstPublishYear = firstPublishYear,
        averageRating = ratingsAverage,
        ratingCount = ratingsCount,
        numPages = numPagesMedian,
        numEditions = numEditions
    )
}