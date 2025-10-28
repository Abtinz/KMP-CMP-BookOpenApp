package com.plcoding.bookpedia.book.presentation.book_detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * A composable that displays a title followed by some content, arranged vertically
 * in a column and centered horizontally.
 *
 * @param title The string to be displayed as the title.
 * @param modifier The modifier to be applied to the `Column` layout.
 * @param content The composable lambda to be displayed below the title.
 */
@Composable
fun TitledContent(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall
        )
        content()
    }
}