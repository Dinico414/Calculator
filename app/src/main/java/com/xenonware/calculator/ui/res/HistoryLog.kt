package com.xenonware.calculator.ui.res

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ClearAll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xenon.mylibrary.theme.QuicksandTitleVariable
import com.xenonware.calculator.viewmodel.classes.HistoryItem

@Composable
fun HistoryLog(
    history: List<HistoryItem>,
    fraction: Float,
    onClearHistory: () -> Unit,  // New callback to clear history
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        if (history.isEmpty()) {
            // Centered "No history yet" message
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No history yet",
                    color = Color.Gray.copy(alpha = fraction),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            // History list with clear button at top-start
            Box {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.End,
                    reverseLayout = true,
                    verticalArrangement = Arrangement.Bottom // Helps with reverseLayout feel
                ) {
                    items(
                        items = history,
                        key = { it.id }
                    ) { entry ->
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier
                                .animateItem()
                                .padding(vertical = 8.dp, horizontal = 4.dp)
                        ) {
                            Text(
                                text = entry.expression,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = fraction * 0.6f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Light
                            )
                            Text(
                                text = "= ${entry.result}",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = fraction),
                                fontSize = 24.sp,
                                fontFamily = QuicksandTitleVariable,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onClearHistory,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ClearAll,
                        contentDescription = "Clear history",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = fraction * 0.8f)
                    )
                }
            }
        }
    }
}