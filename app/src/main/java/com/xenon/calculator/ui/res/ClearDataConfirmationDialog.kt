package com.xenon.calculator.ui.res

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.xenon.calculator.R

@Composable
fun ClearDataConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        containerColor = Color.Black,
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.clear_data_dialog), color = Color.White) },
        text = {
            Text(
                text = stringResource(R.string.clear_data_dialog),
                color = Color.White
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.confirm), color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel), color = Color.White)
            }
        }
    )
}