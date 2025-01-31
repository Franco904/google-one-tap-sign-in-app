package com.example.one_tap_sign_in.core.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.one_tap_sign_in.core.presentation.utils.invokeAfterComposition
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
        showSnackbar: (String, Boolean) -> Unit,
    ) {
        composable<Destinations.Root> {
            RootScreen(
                viewModel = koinViewModel<RootViewModel>()
                    .invokeAfterComposition { init() },
                onNavigateToFirstDestination = { firstDestination ->
                    navController.navigate(firstDestination) {
                        val startDestination =
                            navController.graph.startDestinationRoute ?: return@navigate
                        popUpTo(startDestination) { inclusive = true }
                    }
                }
            )
        }

        composable<Destinations.SignIn> {
            SignInScreen(
                viewModel = koinViewModel<SignInViewModel>(),
                onSignInSuccess = {
                    navController.navigate(Destinations.Profile) {
                        val startDestination =
                            navController.graph.startDestinationRoute ?: return@navigate
                        popUpTo(startDestination) { inclusive = true }
                    }
                },
                showSnackbar = { message, isSuccess -> showSnackbar(message, isSuccess) }
            )
        }

        composable<Destinations.Profile> {
            ProfileScreen(
                viewModel = koinViewModel<ProfileViewModel>()
                    .invokeAfterComposition { init() },
                onSignOutSuccess = {
                    navController.navigate(Destinations.SignIn) {
                        val startDestination =
                            navController.graph.startDestinationRoute ?: return@navigate
                        popUpTo(startDestination) { inclusive = true }
                    }
                },
                showSnackbar = { message, isSuccess -> showSnackbar(message, isSuccess) }
            )
        }
    }
}
