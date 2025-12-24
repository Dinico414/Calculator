package com.xenonware.calculator.ui.layouts.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.xenon.mylibrary.theme.QuicksandTitleVariable
import com.xenon.mylibrary.values.LargePadding
import com.xenon.mylibrary.values.LargerPadding
import com.xenonware.calculator.viewmodel.CalculatorViewModel

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun SmallCalculatorScreen(viewModel: CalculatorViewModel) {
    Log.d("CalculatorDebug", "CompactLandscapeCalculatorScreen Composing")
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            CoverLandscapeDisplaySection(
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(LargerPadding)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = screenHeight * 0.75f)
        )
    }
}

@Composable
fun CoverLandscapeDisplaySection(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = viewModel.displayInputWithSeparators,
            style = MaterialTheme.typography.displaySmall.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Light,
                lineHeight = 0.8.em
            ),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.Start,
            maxLines = 4,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(end = LargePadding)
        )

        Text(
            text = viewModel.result,
            style = MaterialTheme.typography.displaySmall.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = QuicksandTitleVariable,
                lineHeight = 0.8.em
            ),
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.End,
            maxLines = 4,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = LargePadding)
        )
    }
}