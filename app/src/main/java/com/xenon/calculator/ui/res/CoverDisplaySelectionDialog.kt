package com.xenon.calculator.ui.res

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xenon.calculator.R

@Composable
fun CoverDisplaySelectionDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    AlertDialog(
        containerColor = Color.Black,
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.cover_dialog_title), color = Color.White) },
        text = {
            val containerSize = LocalWindowInfo.current.containerSize
            Column(Modifier.selectableGroup()) { // Selectable group not strictly needed here
                Text(stringResource(R.string.cover_dialog_description), color = Color.White)
                Spacer(modifier = Modifier.height(8.dp)) // Increased spacer slightly
                Text("Screen size: ${containerSize.width}x${containerSize.height} px", color = Color.LightGray)
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.yes), color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel), color = Color.White)
            }
        }
    )
}