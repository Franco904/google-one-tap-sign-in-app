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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import com.example.one_tap_sign_in.R
import com.example.one_tap_sign_in.core.theme.AppCustomColors
import com.example.one_tap_sign_in.core.theme.AppTheme
import com.example.one_tap_sign_in.core.utils.presentation.getActivity
import com.example.one_tap_sign_in.signin.composables.SignInBox
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = koinViewModel(),
    onSignInSucceded: () -> Unit = {},
    showSnackbar: (String, Color) -> Unit = { _, _ -> },
) {
    val context = LocalContext.current
    val errorColor = MaterialTheme.colorScheme.error

    var isSigningIn by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collectLatest { uiEvent ->
            when (uiEvent) {
                is SignInViewModel.UiEvents.SignInSucceded -> {
                    isSigningIn = false

                    showSnackbar(
                        context.getString(R.string.snackbar_sign_in_succeded),
                        AppCustomColors.green300,
                    )

                    onSignInSucceded()
                }

                is SignInViewModel.UiEvents.SignInFailed -> {
                    isSigningIn = false

                    showSnackbar(
                        context.getString(uiEvent.messageId),
                        errorColor,
                    )
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
                        isSigningIn = true

                        viewModel.signInUser(
                            activityContext = context.getActivity(),
                        )
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
