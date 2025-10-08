package com.xenonware.calculator.ui.res

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.xenonware.calculator.ui.values.LargerSpacing
import com.xenonware.calculator.ui.values.MediumCornerRadius
import com.xenonware.calculator.ui.values.LargePadding

@Composable
fun InputGroup(
    modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(MediumCornerRadius))
            .background(colorScheme.secondaryContainer)
            .padding(horizontal = LargePadding, vertical = LargePadding),
        verticalArrangement = Arrangement.spacedBy(LargerSpacing),
        content = content
    )
}