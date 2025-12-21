package com.xenonware.calculator.ui.res

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    modifier: Modifier = Modifier
) {
    if (history.isEmpty()) {
        Text(
            text = "No history yet",
            color = Color.Gray.copy(alpha = fraction),
            fontSize = 18.sp,
            modifier = modifier.padding(16.dp)
        )
        return
    }

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        reverseLayout = true
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
}