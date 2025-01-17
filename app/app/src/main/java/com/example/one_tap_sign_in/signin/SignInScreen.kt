package com.example.one_tap_sign_in.signin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.one_tap_sign_in.R
import com.example.one_tap_sign_in.core.theme.AppCustomColors
import com.example.one_tap_sign_in.core.theme.AppTheme

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel,
) {
    val context = LocalContext.current
    val errorColor = MaterialTheme.colorScheme.error

    val snackbarHostState = remember { SnackbarHostState() }

    val isSigningIn by viewModel.isSigningIn.collectAsStateWithLifecycle()

    var snackbarContainerColor by remember { mutableStateOf(AppCustomColors.green300) }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { uiEvent ->
            when (uiEvent) {
                is SignInViewModel.UiEvents.Snackbar -> {
                    snackbarContainerColor =
                        if (uiEvent.isError) errorColor else AppCustomColors.green300

                    snackbarHostState.showSnackbar(
                        message = context.getString(uiEvent.messageId),
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = snackbarContainerColor,
                    )
                }
            )
        },
        modifier = modifier
    ) { contentPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(
                    start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
                    top = contentPadding.calculateTopPadding(),
                    end = contentPadding.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = contentPadding.calculateBottomPadding(),
                )
        ) {
            SignInBackground()
            SignInBox(
                isSigningIn = isSigningIn,
                onSignIn = {
                    if (!isSigningIn) {
                        viewModel.onSignIn()
                    }
                },
            )
        }
    }
}

@Composable
private fun SignInBox(
    isSigningIn: Boolean,
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        border = BorderStroke(
            width = 0.5.dp,
            color = MaterialTheme.colorScheme.outline,
        ),
        shape = MaterialTheme.shapes.extraSmall,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(28.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .size(72.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.sign_in_to_continue),
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.to_use_this_app_first_you_need_to_sign_in),
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(24.dp))
            GoogleSignInButton(
                isSigningIn = isSigningIn,
                onSignIn = onSignIn,
            )
        }
    }
}

@Composable
private fun SignInBackground() {
    Column {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxSize()
                .weight(1f)
        )
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .fillMaxSize()
                .weight(1f)
        )
    }
}

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
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.width(12.dp))
            AnimatedVisibility(isSigningIn) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.secondaryContainer,
                    strokeWidth = 2.dp,
                    modifier = Modifier
                        .size(16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun SignInScreenPreview(
    modifier: Modifier = Modifier,
) {
    AppTheme {
        SignInScreen(
            viewModel = SignInViewModel(),
        )
    }
}
