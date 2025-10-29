@file:OptIn(ExperimentalLayoutApi::class)

package com.plcoding.bookpedia.book.presentation.book_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.description_unavailable
import cmp_bookpedia.composeapp.generated.resources.languages
import cmp_bookpedia.composeapp.generated.resources.pages
import cmp_bookpedia.composeapp.generated.resources.rating
import cmp_bookpedia.composeapp.generated.resources.synopsis
import com.plcoding.bookpedia.book.presentation.book_detail.components.BlurredImageBackground
import com.plcoding.bookpedia.book.presentation.book_detail.components.BookChip
import com.plcoding.bookpedia.book.presentation.book_detail.components.ChipSize
import com.plcoding.bookpedia.book.presentation.book_detail.components.TitledContent
import com.plcoding.bookpedia.core.presentation.DarkGreen
import com.plcoding.bookpedia.core.presentation.SandYellow
import com.plcoding.bookpedia.core.presentation.primary
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round

/**
 * A root composable that connects the [BookDetailViewModel] to the [BookDetailScreen].
 *
 * This function is responsible for collecting the state from the `viewModel`,
 * and passing it down to the stateless `BookDetailScreen`. It also handles
 * actions from the UI, such as the back button click, by delegating them
 * to the `viewModel` or the `onBackClick` callback.
 *
 * @param viewModel The view model that holds the state and business logic for the book detail screen.
 * @param onBackClick A lambda function to be invoked when the user clicks the back button,
 *                    typically used for navigation.
 */
@Composable
fun BookDetailScreenRoot(
    viewModel: BookDetailViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BookDetailScreen(
        state = state,
        onAction = { action ->
            when(action) {
                is BookDetailAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

/**
 * A private composable that displays the detailed information of a book.
 * It's a stateless component that receives the current state and a lambda to handle user actions.
 * The UI is built with a blurred image background and a scrollable column for the book's details.
 *
 * @param state The current state of the book detail screen, containing the book information,
 * loading status, and favorite status.
 * @param onAction A callback function to be invoked when a user action occurs, such as clicking
 * the back button or the favorite icon.
 */
@Composable
private fun BookDetailScreen(
    state: BookDetailState,
    onAction: (BookDetailAction) -> Unit
) {
    BlurredImageBackground(
        imageUrl = state.book?.imageUrl,
        isFavorite = state.isFavorite,
        onFavoriteClick = {
            onAction(BookDetailAction.OnFavoriteClick)
        },
        onBackClick = {
            onAction(BookDetailAction.OnBackClick)
        },
        modifier = Modifier.fillMaxSize()
    ) {
        if(state.book != null) {
            Column(
                modifier = Modifier
                    .widthIn(max = 700.dp)
                    .fillMaxWidth()
                    .padding(
                        vertical = 16.dp,
                        horizontal = 24.dp
                    )
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = state.book.title,
                    color = primary,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = state.book.authors.joinToString(),
                    color =  primary,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    state.book.averageRating?.let { rating ->
                        TitledContent(
                            title = stringResource(Res.string.rating),
                        ) {
                            BookChip {
                                Text(
                                    text = "${round(rating * 10) / 10.0}",
                                    color = primary
                                )
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = SandYellow
                                )
                            }
                        }
                    }
                    state.book.numPages?.let { pageCount ->
                        TitledContent(
                            title = stringResource(Res.string.pages),
                        ) {
                            BookChip {
                                Text(
                                    text = pageCount.toString(),
                                    color =primary
                                )
                            }
                        }
                    }
                }
                if(state.book.languages.isNotEmpty()) {
                    TitledContent(
                        title = stringResource(Res.string.languages),
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                    ) {
                        FlowRow(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.wrapContentSize(Alignment.Center)
                        ) {
                            state.book.languages.forEach { language ->
                                BookChip(
                                    size = ChipSize.SMALL,
                                    modifier = Modifier.padding(2.dp)
                                ) {
                                    Text(
                                        text = language.uppercase(),
                                        color = primary,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
                Text(
                    text = stringResource(Res.string.synopsis),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .fillMaxWidth()
                        .padding(
                            top = 24.dp,
                            bottom = 8.dp
                        )
                )
                if(state.isLoading) {
                    //loading our details
                    CircularProgressIndicator(
                        color=DarkGreen
                    )
                } else {
                    Text(
                        text = if(state.book.description.isNullOrBlank()) {
                            stringResource(Res.string.description_unavailable)
                        } else {
                            state.book.description
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify,
                        color = if(state.book.description.isNullOrBlank()) {
                            Color.Black.copy(alpha = 0.4f)
                        } else Color.Black,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}