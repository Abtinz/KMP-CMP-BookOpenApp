@file:OptIn(ExperimentalForeignApi::class)

package com.plcoding.bookpedia.book.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

/**
 * The iOS-specific implementation of the [DatabaseFactory].
 *
 * This `actual` class provides the concrete implementation for creating a Room database
 * builder on the iOS platform. It determines the correct file path for the database
 * within the app's documents directory.
 */
actual class DatabaseFactory {

    /**
     * Creates a Room database builder configured for the iOS environment.
     *
     * It constructs the full path for the database file by locating the app's
     * standard document directory and appending the database name.
     *
     * @return A [RoomDatabase.Builder] instance for the [FavoriteBookDatabase].
     */
    actual fun create(): RoomDatabase.Builder<FavoriteBookDatabase> {
        // Construct the full path to the database file (e.g., ".../Documents/favorite_books.db").
        val dbFile = documentDirectory() + "/${FavoriteBookDatabase.DB_NAME}"
        return Room.databaseBuilder<FavoriteBookDatabase>(
            name = dbFile
        )
    }

    /**
     * Retrieves the path to the app's documents directory on the iOS file system.
     *
     * This private helper function uses Apple's native `NSFileManager` API to find the
     * appropriate location for storing user-specific data files.
     *
     * @return A [String] containing the absolute path to the documents directory.
     * @throws IllegalArgumentException if the directory path cannot be resolved.
     */
    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory, // The standard directory for user documents.
            inDomain = NSUserDomainMask,   // Search in the user's home directory.
            appropriateForURL = null,
            create = false,
            error = null
        )
        // Ensure the path is not null before returning, crashing if it is, as the app cannot function without it.
        return requireNotNull(documentDirectory?.path)
    }
}
