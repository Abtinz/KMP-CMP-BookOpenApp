package com.plcoding.bookpedia.book.data.database

import androidx.room.RoomDatabaseConstructor

/**
 * A multiplatform constructor for the [FavoriteBookDatabase].
 *
 * This `expect object` defines the common API for creating an instance
 * of the Room database. Platform-specific `actual` implementations
 * will provide the concrete database builder for Android and iOS.
 */
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object BookDatabaseConstructor: RoomDatabaseConstructor<FavoriteBookDatabase> {
    override fun initialize(): FavoriteBookDatabase
}