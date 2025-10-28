package com.plcoding.bookpedia.book.data.database

import androidx.room.RoomDatabase

/**
 * A platform-specific factory for creating a Room database builder.
 *
 * This `expect` class declares the common API for creating a `RoomDatabase.Builder`
 * for the `FavoriteBookDatabase`. Each platform (e.g., Android, iOS) must provide
 * an `actual` implementation that handles the platform-specific details of
 * database creation, such as specifying the database file location and context.
 */
expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<FavoriteBookDatabase>
}

//NOTE: BookDatabaseConstructor will be constructed uniquely in every app's specific
//domain like ios and android because of different implementations required for each os
//aim to build SQLite database instances