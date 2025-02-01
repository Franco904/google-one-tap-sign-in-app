package com.example.one_tap_sign_in.profile.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.one_tap_sign_in.R
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun EditUserDialog(
    displayName: String?,
    displayNameError: Int?,
    onDisplayNameTextChanged: () -> Unit,
    onEdit: (String?) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val fieldFocusRequester = remember { FocusRequester() }

    var currentDisplayName: String? by remember { mutableStateOf(displayName) }

    LaunchedEffect(Unit) {
        delay(250.milliseconds)
        fieldFocusRequester.requestFocus()
    }

    LaunchedEffect(displayName) {
        currentDisplayName = displayName
    }

    AlertDialog(
        title = {
            Text(
                text = stringResource(R.string.dialog_edit_user_title),
                style = MaterialTheme.typography.titleMedium,
            )
        },
        text = {
            EditDisplayNameTextField(
                value = currentDisplayName ?: "",
                onValueChange = { input ->
                    currentDisplayName = input
                    onDisplayNameTextChanged()
                },
                isInvalid = displayNameError != null,
                displayNameError = displayNameError,
                onDone = {
                    onEdit(currentDisplayName)
                },
                fieldFocusRequester = fieldFocusRequester,
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onEdit(currentDisplayName ?: "")
                    currentDisplayName = null
                },
            ) {
                Text(
                    text = stringResource(id = R.string.dialog_edit_user_primary_button),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onCancel()
                currentDisplayName = null
            }) {
                Text(
                    text = stringResource(id = R.string.dialog_edit_user_secondary_button),
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

@Composable
fun EditDisplayNameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isInvalid: Boolean,
    displayNameError: Int?,
    onDone: KeyboardActionScope.() -> Unit,
    fieldFocusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        isError = isInvalid,
        textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
        placeholder = {
            Text(
                text = stringResource(R.string.dialog_edit_user_edit_name_placeholder),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Label,
                contentDescription = stringResource(R.string.dialog_edit_user_edit_name_placeholder),
                tint = MaterialTheme.colorScheme.primary,
            )
        },
        supportingText = {
            if (displayNameError != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(displayNameError),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        },
        singleLine = true,
        keyboardActions = KeyboardActions(
            onDone = onDone,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .focusRequester(fieldFocusRequester)
    )
}
