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
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp

@Composable
fun CoverDisplaySelectionDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Is the current display the cover screen?") },
        text = {
            val containerSize = LocalWindowInfo.current.containerSize
            Column(Modifier.selectableGroup()) {
                Text("Will apply a custom more compact theme when on Cover Display.")
                Spacer(modifier = Modifier.height(2.dp))
                Text("Screen size: ${containerSize.width}x${containerSize.height} px")
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        })
}