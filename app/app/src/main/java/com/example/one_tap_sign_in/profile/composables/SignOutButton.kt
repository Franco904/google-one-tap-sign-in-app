package com.example.one_tap_sign_in.profile.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.one_tap_sign_in.R
import com.example.one_tap_sign_in.core.presentation.composables.AppCircularProgressIndicator

@Composable
fun SignOutButton(
    isSigningOut: Boolean,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
        ),
        shape = MaterialTheme.shapes.extraSmall,
        onClick = onSignOut,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp,
                )
        ) {
            Text(
                text = stringResource(
                    if (isSigningOut) R.string.please_wait else R.string.profile_screen_google_sign_out_text,
                ),
                style = MaterialTheme.typography.labelSmall,
            )
            AnimatedVisibility(isSigningOut) {
                Row {
                    Spacer(modifier = Modifier.width(12.dp))
                    AppCircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier
                            .size(16.dp)
                    )
                }
            }
        }
    }
}