package com.xenonware.calculator.ui.res

import android.os.Build
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xenon.mylibrary.theme.QuicksandTitleVariable
import com.xenon.mylibrary.values.CompactButtonSize
import com.xenonware.calculator.viewmodel.classes.HistoryItem
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class,
    ExperimentalHazeMaterialsApi::class
)
@Composable
fun HistoryLog(
    history: List<HistoryItem>,
    fraction: Float,
    onClearHistory: () -> Unit,
    hazeState: HazeState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        AnimatedContent(
            targetState = history.isEmpty(),
            transitionSpec = {
                fadeIn(tween(300)).togetherWith(fadeOut(tween(200)))
            },
            label = "HistoryStateTransition"
        ) { isEmpty ->
            if (isEmpty) {
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
                val listState = rememberLazyListState()
                val coroutineScope = rememberCoroutineScope()

                val isAtBottom by remember { derivedStateOf { !listState.canScrollBackward } }
                val isAtTop by remember { derivedStateOf { !listState.canScrollForward } }

                LaunchedEffect(history.size) {
                    if (history.isNotEmpty() && listState.firstVisibleItemIndex <= 2) {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .hazeSource(hazeState),
                        horizontalAlignment = Alignment.End,
                        reverseLayout = true,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        items(
                            items = history,
                            key = { it.id }
                        ) { entry ->
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier
                                    .animateItem(
                                        fadeInSpec = tween(300),
                                        fadeOutSpec = tween(300),
                                        placementSpec = tween(400)
                                    )
                                    .padding(vertical = 8.dp, horizontal = 4.dp)
                            ) {
                                Text(
                                    text = entry.expression,
                                    color = colorScheme.onSurface.copy(alpha = fraction * 0.6f),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Light
                                )
                                Text(
                                    text = "= ${entry.result}",
                                    color = colorScheme.onSurface.copy(alpha = fraction),
                                    fontSize = 24.sp,
                                    fontFamily = QuicksandTitleVariable,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(64.dp)
                            .graphicsLayer { alpha = if (isAtBottom) 0f else fraction }
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        colorScheme.surfaceDim.copy(alpha = fraction)
                                    )
                                )
                            )
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth()
                            .height(64.dp)
                            .graphicsLayer { alpha = if (isAtTop) 0f else fraction }
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        colorScheme.surfaceDim.copy(alpha = fraction),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    IconButton(
                        onClick = onClearHistory,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .clip(CircleShape)
                            .hazeEffect(
                                state = hazeState,
                                style = HazeMaterials.ultraThin(
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                        colorScheme.surfaceDim
                                    } else {
                                        colorScheme.surfaceDim
                                    }
                                )
                            )
                            .size(CompactButtonSize)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DeleteSweep,
                            contentDescription = "Clear history",
                            tint = colorScheme.error.copy(alpha = fraction * 0.8f)
                        )
                    }
                }
            }
        }
    }
}