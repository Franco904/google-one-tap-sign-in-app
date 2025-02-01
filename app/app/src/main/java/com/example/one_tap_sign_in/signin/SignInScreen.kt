package com.example.one_tap_sign_in.signin

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.example.one_tap_sign_in.core.presentation.dataSources.credentialManager.AppCredentialManager
import com.example.one_tap_sign_in.core.presentation.exceptions.CredentialManagerException
import com.example.one_tap_sign_in.core.presentation.theme.AppTheme
import com.example.one_tap_sign_in.core.presentation.utils.getActivity
import com.example.one_tap_sign_in.core.presentation.utils.uiConverters.toUiMessage
import com.example.one_tap_sign_in.signin.SignInViewModel.UiEvents.DataSourceError
import com.example.one_tap_sign_in.signin.SignInViewModel.UiEvents.SignInSuccess
import com.example.one_tap_sign_in.signin.composables.SignInBox
import com.example.one_tap_sign_in.signin.models.GoogleUserCredentials
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

private const val TAG = "SignInScreen"

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = koinViewModel(),
    onSignInSuccess: () -> Unit = {},
    showSnackbar: (String, Boolean) -> Unit = { _, _ -> },
) {
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    var isSigningIn by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.checkUserDidExplicitlySignOut()

        viewModel.uiEvents.collectLatest { uiEvent ->
            when (uiEvent) {
                is DataSourceError -> {
                    isSigningIn = false

                    showSnackbar(context.getString(uiEvent.messageId), false)
                }

                is SignInSuccess -> {
                    isSigningIn = false

                    onSignInSuccess()
                }
            }
        }
    }

    Scaffold(
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
                        coroutineScope.launch {
                            isSigningIn = true

                            try {
                                // To show Google's sign in dialog
                                val credentials = getGoogleUserCredentials(
                                    activityContext = context.getActivity(),
                                    didUserExplicitlySignOut = viewModel.didUserExplicitlySignOut,
                                )

                                viewModel.onSignInUser(credentials = credentials)
                            } catch (e: CredentialManagerException) {
                                Log.e(TAG, "${e.message}")

                                isSigningIn = false

                                showSnackbar(context.getString(e.toUiMessage()), false)
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

private suspend fun getGoogleUserCredentials(
    activityContext: Activity,
    didUserExplicitlySignOut: Boolean
): GoogleUserCredentials {
    return try {
        AppCredentialManager.chooseGoogleAccount(
            activityContext = activityContext,
            isSignIn = true,
            mustEnableAutoSelect = !didUserExplicitlySignOut,
        )
    } catch (e: CredentialManagerException) {
        AppCredentialManager.chooseGoogleAccount(
            activityContext = activityContext,
            isSignIn = false,
            mustEnableAutoSelect = false,
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
