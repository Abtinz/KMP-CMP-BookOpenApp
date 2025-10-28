package com.plcoding.bookpedia.book.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

/**
 * The desktop-specific implementation of the [DatabaseFactory].
 *
 * This `actual` class provides the concrete implementation for creating a Room database
 * builder on desktop platforms (Windows, macOS, Linux). It determines the appropriate
 * platform-specific directory for application data and ensures it exists before
 * creating the database file path.
 */
actual class DatabaseFactory {
    /**
     * Creates a Room database builder configured for the desktop environment.
     *
     * It identifies the operating system and constructs the database path in the standard
     * application support/data directory for that OS.
     *
     * - **Windows:** `%APPDATA%/Bookpedia/`
     * - **macOS:** `~/Library/Application Support/Bookpedia/`
     * - **Linux/Other:** `~/.local/share/Bookpedia/`
     *
     * @return A [RoomDatabase.Builder] instance for the [FavoriteBookDatabase].
     */
    actual fun create(): RoomDatabase.Builder<FavoriteBookDatabase> {
        // Get the operating system name and the user's home directory path.
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")

        // Determine the appropriate application data directory based on the OS.
        val appDataDir = when {
            os.contains("win") -> File(System.getenv("APPDATA"), "Bookpedia")
            os.contains("mac") -> File(userHome, "Library/Application Support/Bookpedia")
            else -> File(userHome, ".local/share/Bookpedia") // A common standard for Linux.
        }

        // If the application data directory does not exist, create it.
        if (!appDataDir.exists()) {
            appDataDir.mkdirs()
        }

        // Create the final database file object within the application data directory.
        val dbFile = File(appDataDir, FavoriteBookDatabase.DB_NAME)

        // Return a Room database builder configured with the absolute path to the database file.
        // Note: For desktop, Room.databaseBuilder requires a String path, not a name.
        return Room.databaseBuilder<FavoriteBookDatabase>(dbFile.absolutePath)
    }
}
