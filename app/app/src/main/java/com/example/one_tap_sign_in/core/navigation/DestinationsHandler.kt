package com.example.one_tap_sign_in.core.navigation

import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.one_tap_sign_in.core.utils.presentation.invokeAfterComposition
import com.example.one_tap_sign_in.profile.ProfileScreen
import com.example.one_tap_sign_in.profile.ProfileViewModel
import com.example.one_tap_sign_in.root.RootScreen
import com.example.one_tap_sign_in.root.RootViewModel
import com.example.one_tap_sign_in.signin.SignInScreen
import com.example.one_tap_sign_in.signin.SignInViewModel
import org.koin.androidx.compose.koinViewModel

object DestinationsHandler {
    fun NavGraphBuilder.destinations(
        navController: NavController,
        showSnackbar: (String, Color) -> Unit,
    ) {
        composable<Destinations.Root> {
            RootScreen(
                viewModel = koinViewModel<RootViewModel>()
                    .invokeAfterComposition { init() },
                onNavigateToFirstDestination = { firstDestination ->
                    navController.navigate(firstDestination)
                }
            )
        }

        composable<Destinations.SignIn> {
            SignInScreen(
                viewModel = koinViewModel<SignInViewModel>(),
                onSignInSucceded = {
                    navController.navigate(Destinations.Profile)
                },
                showSnackbar = { message, color ->
                    showSnackbar(message, color)
                }
            )
        }

        composable<Destinations.Profile> {
            ProfileScreen(
                viewModel = koinViewModel<ProfileViewModel>()
                    .invokeAfterComposition { init() },
                onSignOutSucceded = {
                    navController.navigateUp()
                },
                showSnackbar = { message, color ->
                    showSnackbar(message, color)
                }
            )
        }
    }
}
