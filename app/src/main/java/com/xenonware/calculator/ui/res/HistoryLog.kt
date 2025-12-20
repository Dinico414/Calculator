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

@Composable
fun HistoryLog(
    history: List<Pair<String, String>>,
    fraction: Float,  // ← FIXED: Float, not Int
    modifier: Modifier = Modifier
) {
    val alpha = 1f - fraction  // 1f when collapsed → hidden, 0f when expanded → visible

    if (history.isEmpty()) {
        Text(
            text = "No history yet",
            color = Color.Gray.copy(alpha = alpha),
            fontSize = 18.sp,
            modifier = modifier.padding(16.dp)
        )
        return
    }

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        reverseLayout = true  // newest at the bottom
    ) {
        items(
            items = history,
            key = { it.first + it.second }  // good stable key
        ) { (expr, res) ->
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .animateItem()
                    .padding(vertical = 8.dp, horizontal = 4.dp)
            ) {
                Text(
                    text = expr,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha * 0.6f),
                    fontSize = 16.sp
                )
                Text(
                    text = "= $res",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}