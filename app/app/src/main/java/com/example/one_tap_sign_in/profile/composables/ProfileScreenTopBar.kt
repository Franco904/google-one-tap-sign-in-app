package com.example.one_tap_sign_in.profile.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.one_tap_sign_in.R
import com.example.one_tap_sign_in.core.application.theme.AppTheme
import com.example.one_tap_sign_in.core.presentation.composables.AppCircularProgressIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenTopBar(
    isEditingUser: Boolean,
    isDeletingUser: Boolean,
    onEditUser: () -> Unit,
    onDeleteUser: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val outlineVariantColor = MaterialTheme.colorScheme.outlineVariant

    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.profile_screen_top_bar_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isEditingUser) {
                    AppCircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier
                            .size(16.dp)
                            .offset(x = -(16).dp)
                    )
                } else {
                    IconButton(
                        onClick = onEditUser,
                        modifier = Modifier
                            .padding(end = if (isDeletingUser) 24.dp else 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                if (isDeletingUser) {
                    AppCircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier
                            .size(16.dp)
                            .offset(x = -(16).dp)
                    )
                } else {
                    IconButton(
                        onClick = onDeleteUser,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        modifier = modifier
            .drawBehind {
                drawLine(
                    color = outlineVariantColor,
                    start = Offset(x = 0f, y = size.height),
                    end = Offset(x = size.width, y = size.height),
                    strokeWidth = 2.dp.toPx(),
                )
            }
    )
}

@Preview
@Composable
fun ProfileScreenTopBarPreview(modifier: Modifier = Modifier) {
    AppTheme {
        ProfileScreenTopBar(
            isEditingUser = true,
            isDeletingUser = false,
            onEditUser = {},
            onDeleteUser = {},
        )
    }
}
