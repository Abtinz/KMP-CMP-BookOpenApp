package com.plcoding.bookpedia.book.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * The Room database for storing favorite books.
 *
 * This database is responsible for persisting `BookEntity` objects, which represent
 * the user's favorite books. It uses a `StringListTypeConverter` to handle
 * the conversion of lists of strings (like authors) for storage in the database.
 * The database is constructed using a custom `BookDatabaseConstructor`.
 *
 * @property favoriteBookDao The Data Access Object (DAO) for interacting with the favorite books table.
 */
@Database(
    entities = [BookEntity::class],
    version = 1
)
@TypeConverters(
    StringListTypeConverter::class
)
@ConstructedBy(BookDatabaseConstructor::class)
abstract class FavoriteBookDatabase: RoomDatabase() {
    abstract val favoriteBookDao: FavoriteBookDao

    companion object {
        const val DB_NAME = "book.db"
    }
}

//NOTE: BookDatabaseConstructor will be constructed uniquely in every app's specific
//domain like ios and android because of different implementations required for each os
//aim to build SQLite database instances