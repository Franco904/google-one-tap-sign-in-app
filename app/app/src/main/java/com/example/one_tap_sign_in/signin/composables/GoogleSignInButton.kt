package com.example.one_tap_sign_in.signin.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.one_tap_sign_in.R
import com.example.one_tap_sign_in.core.presentation.composables.AppCircularProgressIndicator

@Composable
fun GoogleSignInButton(
    isSigningIn: Boolean,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
        ),
        shape = MaterialTheme.shapes.extraSmall,
        onClick = onSignIn,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(
                    vertical = 4.dp,
                    horizontal = 16.dp,
                )
        ) {
            Image(
                painter = painterResource(R.drawable.ic_google_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(if (isSigningIn) R.string.please_wait else R.string.sign_in_with_google),
                style = MaterialTheme.typography.labelSmall,
            )
            AnimatedVisibility(isSigningIn) {
                Row {
                    Spacer(modifier = Modifier.width(12.dp))
                    AppCircularProgressIndicator(
                        modifier = Modifier
                            .size(16.dp)
                    )
                }
            }
        }
    }
}
