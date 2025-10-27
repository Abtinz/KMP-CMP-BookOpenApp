package com.plcoding.bookpedia.core.presentation

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource


/**
 * A sealed interface to represent text that can be displayed in the UI.
 * This is a wrapper that allows for handling both dynamic strings from a data source (e.g., an API)
 * and static strings from local resources (e.g., strings.xml) in a unified way,
 * especially within ViewModels.
 */
sealed interface UiText {

    /**
     * Represents a UI text that is a simple, dynamic string value.
     * This is used for text that is generated at runtime and doesn't come from a static resource file.
     *
     * @property value The actual String content to be displayed.
     */
    data class DynamicString(
        val value: String
    ): UiText

    /**
     * Represents a piece of text that comes from a string resource.
     * This is useful for internationalization (i18n) and for keeping UI strings
     * in a central, manageable location (resources).
     *
     * @property id The [StringResource] identifier for the string.
     * @property args Optional variable arguments to be formatted into the string resource.
     *              This corresponds to placeholders like %1$s, %2$d, etc., in the resource string.
     */
    class StringResourceId(
        val id: StringResource,
        val args: Array<Any> = arrayOf()
    ): UiText

    @Composable
    fun asString(): String {
        return when(this) {
            is DynamicString -> value
            is StringResourceId -> stringResource(resource = id, formatArgs = args)
        }
    }
}