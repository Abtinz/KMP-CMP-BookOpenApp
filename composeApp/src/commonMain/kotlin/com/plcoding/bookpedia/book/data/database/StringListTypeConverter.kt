package com.plcoding.bookpedia.book.data.database

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * A [TypeConverter] for Room to handle the conversion between a List of Strings and a single String.
 *
 * Room database can only store primitive data types. This converter allows Room to store a
 * `List<String>` by converting it into a JSON String for storage and converting it back
 * to a `List<String>` when retrieved from the database.
 */
object StringListTypeConverter {

    /**
     * Converts a JSON String back into a List of Strings.
     *
     * This method is used by Room when reading data from the database. It takes the
     * stored JSON string and decodes it into its original `List<String>` format.
     *
     * @param value The JSON string representation of the list from the database.
     * @return The deserialized `List<String>`.
     */
    @TypeConverter
    fun fromString(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    /**
     * Converts a List of Strings into a single JSON String for database storage.
     *
     * This method is used by Room when writing data to the database. It takes a `List<String>`
     * and encodes it into a JSON string, which can be stored in a single database column.
     *
     * @param list The `List<String>` to be converted.
     * @return A JSON string representation of the list.
     */
    @TypeConverter
    fun fromList(list: List<String>): String {
        return Json.encodeToString(list)
    }
}
