package com.plcoding.bookpedia.book.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) for a book entry retrieved from a search query.
 * This class models the structure of a single book result from the search API.
 *
 * @property id The unique identifier for the book, often a path like "/works/OL45883W".
 * @property title The title of the book.
 * @property languages A list of language codes in which the book is available. Can be null.
 * @property coverAlternativeKey An integer identifier for a book cover image, used as a fallback. Can be null.
 * @property authorKeys A list of unique keys for the authors of the book. Can be null.
 * @property authorNames A list of the names of the authors. Can be null.
 * @property coverKey The unique key for the primary cover edition of the book. Can be null.
 * @property firstPublishYear The year the book was first published. Can be null.
 * @property ratingsAverage The average rating of the book. Can be null.
 * @property ratingsCount The total number of ratings the book has received. Can be null.
 * @property numPagesMedian The median number of pages across all editions of the book. Can be null.
 * @property numEditions The total count of different editions of the book. Can be null.
 */
@Serializable
data class SearchedBookDto(
    @SerialName("key") val id: String,
    @SerialName("title") val title: String,
    @SerialName("language") val languages: List<String>? = null,
    @SerialName("cover_i") val coverAlternativeKey: Int? = null,
    @SerialName("author_key") val authorKeys: List<String>? = null,
    @SerialName("author_name") val authorNames: List<String>? = null,
    @SerialName("cover_edition_key") val coverKey: String? = null,
    @SerialName("first_publish_year") val firstPublishYear: Int? = null,
    @SerialName("ratings_average") val ratingsAverage: Double? = null,
    @SerialName("ratings_count") val ratingsCount: Int? = null,
    @SerialName("number_of_pages_median") val numPagesMedian: Int? = null,
    @SerialName("edition_count") val numEditions: Int? = null,
)
