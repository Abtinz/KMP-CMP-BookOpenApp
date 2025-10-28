package com.plcoding.bookpedia.book.data.dto

import kotlinx.serialization.Serializable

/**
 * A data transfer object representing the description of a book.
 * This class is used for serializing/deserializing the 'description' field from the API response.
 *
 * @property value The actual description text of the book as a [String].
 */
@Serializable
data class DescriptionDto(
    val value: String
)
