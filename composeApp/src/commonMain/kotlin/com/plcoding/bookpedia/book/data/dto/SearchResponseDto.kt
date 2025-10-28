package com.plcoding.bookpedia.book.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the top-level response from the book search API.
 * This data transfer object (DTO) is used to deserialize the JSON response
 * from the search endpoint.
 *
 * @property results A list of [SearchedBookDto] objects, which represent the individual books found in the search.
 * The JSON key for this field is "docs".
 */
@Serializable
data class SearchResponseDto(
    @SerialName("docs") val results: List<SearchedBookDto>
)
