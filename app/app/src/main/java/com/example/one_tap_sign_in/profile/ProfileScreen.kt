package com.example.one_tap_sign_in.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.one_tap_sign_in.R
import com.example.one_tap_sign_in.core.theme.AppCustomColors
import com.example.one_tap_sign_in.core.theme.AppTheme
import com.example.one_tap_sign_in.core.utils.presentation.getActivity
import com.example.one_tap_sign_in.profile.composables.DeleteUserDialog
import com.example.one_tap_sign_in.profile.composables.EditUserDialog
import com.example.one_tap_sign_in.profile.composables.ProfilePictureSection
import com.example.one_tap_sign_in.profile.composables.ProfileScreenTopBar
import com.example.one_tap_sign_in.profile.composables.SignOutButton
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel(),
    onSignOutSucceded: () -> Unit = {},
    showSnackbar: (String, Color) -> Unit = { _, _ -> },
) {
    val context = LocalContext.current
    val errorColor = MaterialTheme.colorScheme.error

    val userCredentialsUiState by viewModel.userCredentialsUiState.collectAsStateWithLifecycle()

    var isSigningOut by remember { mutableStateOf(false) }
    var isEditingUser by remember { mutableStateOf(false) }
    var isDeletingUser by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collectLatest { uiEvent ->
            when (uiEvent) {
                is ProfileViewModel.UiEvents.SignOutSucceded -> {
                    isSigningOut = false

                    showSnackbar(
                        context.getString(R.string.snackbar_sign_out_succeded),
                        AppCustomColors.green300,
                    )

                    onSignOutSucceded()
                }

                is ProfileViewModel.UiEvents.SignOutFailed -> {
                    isSigningOut = false

                    showSnackbar(
                        context.getString(R.string.snackbar_sign_out_failed),
                        errorColor,
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            ProfileScreenTopBar(
                onEditUser = { isEditingUser = true },
                onDeleteUser = { isDeletingUser = true },
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
            if (isEditingUser) {
                EditUserDialog(
                    displayName = userCredentialsUiState.displayName,
                    onEdit = { newDisplayName ->
                        viewModel.onEditUser(newDisplayName = newDisplayName)
                        isEditingUser = false
                    },
                    onCancel = { isEditingUser = false },
                )
            }
            if (isDeletingUser) {
                DeleteUserDialog(
                    onDelete = {
                        viewModel.onDeleteUser(
                            activityContext = context.getActivity(),
                        )
                        isDeletingUser = false
                    },
                    onCancel = { isDeletingUser = false },
                )
            }
            ProfilePictureSection(
                profilePictureUrl = userCredentialsUiState.profilePictureUrl,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = userCredentialsUiState.displayName ?: "Anonymous",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
            )
            Spacer(modifier = Modifier.height(24.dp))
            SignOutButton(
                isSigningOut = isSigningOut,
                onSignOut = {
                    if (!isSigningOut) {
                        isSigningOut = true

                        viewModel.onSignOutUser(
                            activityContext = context.getActivity(),
                        )
                    }
                }
            )
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
