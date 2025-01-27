package com.example.one_tap_sign_in.profile.composables

import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.one_tap_sign_in.R

@Composable
fun DeleteUserDialog(
    onDelete: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        title = {
            Text(
                text = stringResource(R.string.dialog_delete_user_title),
                style = MaterialTheme.typography.titleMedium,
            )
        },
        text = {
            Text(
                text = stringResource(R.string.dialog_delete_user_description),
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        confirmButton = {
            TextButton(onClick = onDelete) {
                Text(
                    text = stringResource(id = R.string.dialog_delete_user_primary_button),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(
                    text = stringResource(id = R.string.dialog_delete_user_secondary_button),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        },
        onDismissRequest = {},
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .width(450.dp)
    )
}
