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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.one_tap_sign_in.core.infra.auth.GoogleCredentialManager
import com.example.one_tap_sign_in.core.theme.AppCustomColors
import com.example.one_tap_sign_in.core.theme.AppTheme
import com.example.one_tap_sign_in.core.utils.presentation.getActivity
import com.example.one_tap_sign_in.profile.composables.ProfileScreenTopBar
import com.example.one_tap_sign_in.profile.composables.SignOutButton
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel(),
    onNavigateUp: () -> Unit = {},
) {
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState =
        remember { SnackbarHostState() } // TODO: mover para cima para tentar fazer aparecer entre diferentes telas

    val userCredentialsUiState by viewModel.userCredentialsUiState.collectAsStateWithLifecycle()

    var isSigningOut by remember { mutableStateOf(false) }

    var snackbarContainerColor by remember { mutableStateOf(AppCustomColors.green300) }

    Scaffold(
        topBar = {
            ProfileScreenTopBar(
                onNavigateUp = onNavigateUp,
            )
        }
    ) { contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(
                    start = contentPadding.calculateStartPadding(LayoutDirection.Ltr) + 32.dp,
                    top = contentPadding.calculateTopPadding(),
                    end = contentPadding.calculateEndPadding(LayoutDirection.Ltr) + 32.dp,
                    bottom = contentPadding.calculateBottomPadding(),
                )
        ) {
            if (userCredentialsUiState.profilePictureUrl != null) {
                AsyncImage(
                    model = userCredentialsUiState.profilePictureUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
            }
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

                        // TODO: Move logic to view model
                        coroutineScope.launch {
                            GoogleCredentialManager.clearStateOnSignUp(
                                activityContext = context.getActivity(),
                            )

                            isSigningOut = false

                            viewModel.signOutUser()

//                            snackbarContainerColor = AppCustomColors.green300
//                            snackbarHostState.showSnackbar(
//                                message = context.getString(R.string.snackbar_sign_out_succeded),
//                            )

                            onNavigateUp()
                        }
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
