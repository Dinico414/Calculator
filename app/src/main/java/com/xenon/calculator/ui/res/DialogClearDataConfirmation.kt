package com.xenon.calculator.ui.res

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xenon.calculator.R
import com.xenon.calculator.ui.values.SmallCornerRadius // Assuming you have this
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.FluentMaterials

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun DialogClearDataConfirmation(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    hazeState: HazeState // Added HazeState
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.errorContainer,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.clear_data_dialog_title),
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        },
        text = {
            Text(
                text = stringResource(R.string.clear_data_dialog_description),
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        },
        confirmButton = {
            FilledTonalButton(
                onClick = onConfirm,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text(
                    stringResource(R.string.confirm)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        },
    )
}