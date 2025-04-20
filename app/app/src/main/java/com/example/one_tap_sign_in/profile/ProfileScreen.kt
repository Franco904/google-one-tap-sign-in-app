package com.example.one_tap_sign_in.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.one_tap_sign_in.R
import com.example.one_tap_sign_in.core.application.theme.AppTheme
import com.example.one_tap_sign_in.core.presentation.composables.AppCircularProgressIndicator
import com.example.one_tap_sign_in.core.presentation.dataSources.credentialManager.AppCredentialManager
import com.example.one_tap_sign_in.core.presentation.exceptions.CredentialManagerException
import com.example.one_tap_sign_in.core.presentation.utils.getActivity
import com.example.one_tap_sign_in.core.presentation.utils.uiConverters.toUiMessage
import com.example.one_tap_sign_in.profile.ProfileViewModel.UiEvents.DataSourceError
import com.example.one_tap_sign_in.profile.ProfileViewModel.UiEvents.DeleteUserSuccess
import com.example.one_tap_sign_in.profile.ProfileViewModel.UiEvents.EditUserSuccess
import com.example.one_tap_sign_in.profile.ProfileViewModel.UiEvents.RedirectToSignIn
import com.example.one_tap_sign_in.profile.ProfileViewModel.UiEvents.SignOutUserSuccess
import com.example.one_tap_sign_in.profile.composables.ConfirmDeleteUserDialog
import com.example.one_tap_sign_in.profile.composables.ConfirmSignOutUserDialog
import com.example.one_tap_sign_in.profile.composables.EditUserDialog
import com.example.one_tap_sign_in.profile.composables.ProfileDataSection
import com.example.one_tap_sign_in.profile.composables.ProfileScreenTopBar
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel(),
    onSignOutSuccess: () -> Unit = {},
    showSnackbar: (String, Boolean) -> Unit = { _, _ -> },
) {
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val userCredentialsUiState by viewModel.userCredentialsUiState.collectAsStateWithLifecycle()
    val userFormUiStateUiState by viewModel.userFormUiState.collectAsStateWithLifecycle()
    val isLoadingUser by viewModel.isLoadingUser.collectAsStateWithLifecycle()

    var isConfirmingSigningOut by remember { mutableStateOf(false) }
    var isEditingUserByDialog by remember { mutableStateOf(false) }
    var isConfirmingDeletingUser by remember { mutableStateOf(false) }

    var isSigningOut by remember { mutableStateOf(false) }
    var isDeletingUser by remember { mutableStateOf(false) }
    var isEditingUser by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadUser()

        viewModel.uiEvents.collect { uiEvent ->
            when (uiEvent) {
                is DataSourceError -> {
                    showSnackbar(context.getString(uiEvent.messageId), false)

                    isEditingUser = false
                    isDeletingUser = false
                    isSigningOut = false
                }

                is EditUserSuccess -> {
                    showSnackbar(context.getString(uiEvent.messageId), true)

                    isEditingUser = false
                }

                is DeleteUserSuccess -> {
                    val successMsg = R.string.snackbar_delete_user_succeeded
                    showSnackbar(context.getString(successMsg), true)

                    onSignOutSuccess()

                    isDeletingUser = false
                }

                is SignOutUserSuccess -> {
                    onSignOutSuccess()

                    isSigningOut = false
                }

                is RedirectToSignIn -> {
                    onSignOutSuccess()

                    isSigningOut = false
                }
            }
        }
    }

    Scaffold(
        topBar = {
            ProfileScreenTopBar(
                isEditingUser = isEditingUser,
                isDeletingUser = isDeletingUser,
                onEditUser = {
                    if (!isEditingUserByDialog) isEditingUserByDialog = true
                },
                onDeleteUser = {
                    if (!isConfirmingDeletingUser) isConfirmingDeletingUser = true
                },
            )
        },
        modifier = modifier
    ) { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = contentPadding.calculateStartPadding(LayoutDirection.Ltr) + 32.dp,
                    top = contentPadding.calculateTopPadding(),
                    end = contentPadding.calculateEndPadding(LayoutDirection.Ltr) + 32.dp,
                    bottom = contentPadding.calculateBottomPadding(),
                )
        ) {
            if (isEditingUserByDialog) {
                EditUserDialog(
                    displayName = userCredentialsUiState.displayName,
                    displayNameError = userFormUiStateUiState.displayNameError,
                    onDisplayNameTextChanged = viewModel::clearDisplayNameFieldError,
                    onEdit = { newUserName ->
                        isEditingUser = true
                        isEditingUserByDialog = false

                        viewModel.editUserName(newUserName)
                    },
                    onCancel = {
                        isEditingUserByDialog = false

                        viewModel.clearDisplayNameFieldError()
                    },
                )
            }

            if (isConfirmingDeletingUser) {
                ConfirmDeleteUserDialog(
                    onDelete = {
                        isDeletingUser = true
                        isConfirmingDeletingUser = false

                        coroutineScope.launch {
                            try {
                                AppCredentialManager.clearGoogleCredentialState(
                                    activityContext = context.getActivity(),
                                )

                                viewModel.deleteUser()
                            } catch (e: CredentialManagerException) {
                                showSnackbar(context.getString(e.toUiMessage()), false)
                            }
                        }
                    },
                    onCancel = { isConfirmingDeletingUser = false },
                )
            }

            if (isConfirmingSigningOut) {
                ConfirmSignOutUserDialog(
                    onSignOut = {
                        isSigningOut = true
                        isConfirmingSigningOut = false

                        coroutineScope.launch {
                            try {
                                AppCredentialManager.clearGoogleCredentialState(
                                    activityContext = context.getActivity(),
                                )

                                viewModel.signOutUser()
                            } catch (e: CredentialManagerException) {
                                showSnackbar(context.getString(e.toUiMessage()), false)

                                isSigningOut = false
                            }
                        }
                    },
                    onCancel = { isConfirmingSigningOut = false },
                )
            }

            if (isLoadingUser) {
                AppCircularProgressIndicator(
                    modifier = Modifier
                        .size(64.dp)
                )
            } else {
                ProfileDataSection(
                    profilePictureUrl = userCredentialsUiState.profilePictureUrl ?: "",
                    displayName = userCredentialsUiState.displayName ?: "",
                    isSigningOut = isSigningOut,
                    onSignOut = { isConfirmingSigningOut = true },
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview(
    modifier: Modifier = Modifier,
) {
    AppTheme {
        ProfileScreen()
    }
}
