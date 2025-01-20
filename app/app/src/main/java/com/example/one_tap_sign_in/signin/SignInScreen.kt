package com.example.one_tap_sign_in.signin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import com.example.one_tap_sign_in.R
import com.example.one_tap_sign_in.core.infra.auth.GoogleCredentialManager
import com.example.one_tap_sign_in.core.theme.AppCustomColors
import com.example.one_tap_sign_in.core.theme.AppTheme
import com.example.one_tap_sign_in.core.utils.presentation.getActivity
import com.example.one_tap_sign_in.signin.composables.SignInBox
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = koinViewModel(),
    onSignInSucceded: () -> Unit = {},
) {
    val context = LocalContext.current
    val errorColor = MaterialTheme.colorScheme.error

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState =
        remember { SnackbarHostState() } // TODO: mover para cima para tentar fazer aparecer entre diferentes telas

    var isSigningIn by remember { mutableStateOf(false) }

    var snackbarContainerColor by remember { mutableStateOf(AppCustomColors.green300) }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { uiEvent ->
            when (uiEvent) {
                is SignInViewModel.UiEvents.Snackbar -> {
                    snackbarContainerColor =
                        if (uiEvent.isError) errorColor else AppCustomColors.green300
                    snackbarHostState.showSnackbar(message = context.getString(uiEvent.messageId))
                }

                is SignInViewModel.UiEvents.SignInSuccessed -> {
                    onSignInSucceded()
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
                        isSigningIn = true

                        // TODO: Move logic to view model
                        coroutineScope.launch {
                            val credentials = GoogleCredentialManager.chooseGoogleAccountForSignIn(
                                activityContext = context.getActivity(),
                                isSignIn = true,
                            ) ?: GoogleCredentialManager.chooseGoogleAccountForSignIn(
                                activityContext = context.getActivity(),
                                isSignIn = false, // sign up
                            )

                            isSigningIn = false

                            if (credentials != null) {
                                viewModel.signInUser(credentials = credentials)
                            } else {
                                snackbarContainerColor = errorColor
                                snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.snackbar_sign_in_failed),
                                )
                            }
                        }
                    }
                },
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

@Preview
@Composable
fun SignInScreenPreview(
    modifier: Modifier = Modifier,
) {
    AppTheme {
        SignInScreen()
    }
}
