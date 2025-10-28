package com.plcoding.bookpedia.book.presentation.book_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.plcoding.bookpedia.core.presentation.LightBlue

enum class ChipSize {
    SMALL, REGULAR
}

/**
 * A composable that displays a styled chip, typically used for showing small pieces of information
 * like categories, ratings, or other book-related metadata.
 *
 * The chip has a rounded corner shape, a light blue background, and centered content.
 * Its minimum width can be adjusted via the `size` parameter.
 *
 * @param modifier The modifier to be applied to the chip.
 * @param size The size of the chip, which determines its minimum width. Defaults to [ChipSize.REGULAR].
 * @param chipContent A composable lambda that defines the content to be displayed inside the chip.
 *                    The content is placed within a `RowScope`, allowing for horizontal arrangement.
 */
@Composable
fun BookChip(
    modifier: Modifier = Modifier,
    size: ChipSize = ChipSize.REGULAR,
    chipContent: @Composable RowScope.() -> Unit
) {
    Box(
        modifier = modifier
            .widthIn(
                min = when(size) {
                    ChipSize.SMALL -> 50.dp
                    ChipSize.REGULAR -> 80.dp
                }
            )
            .clip(RoundedCornerShape(16.dp))
            .background(LightBlue)
            .padding(
                vertical = 8.dp,
                horizontal = 12.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            chipContent()
        }
    }
}