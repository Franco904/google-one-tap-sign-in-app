package com.example.one_tap_sign_in

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.one_tap_sign_in.core.utils.presentation.invokeAfterComposition
import com.example.one_tap_sign_in.profile.ProfileScreen
import com.example.one_tap_sign_in.profile.ProfileViewModel
import com.example.one_tap_sign_in.signin.SignInScreen
import com.example.one_tap_sign_in.signin.SignInViewModel
import org.koin.androidx.compose.koinViewModel

object DestinationsHandler {
    fun NavGraphBuilder.destinations(navController: NavController) {
        composable<Destinations.SignIn> {
            SignInScreen(
                viewModel = koinViewModel<SignInViewModel>(),
                onSignInSucceded = {
                    navController.navigate(Destinations.Profile)
                }
            )
        }

        composable<Destinations.Profile> {
            ProfileScreen(
                viewModel = koinViewModel<ProfileViewModel>()
                    .invokeAfterComposition { init() },
                onNavigateUp = {
                    navController.navigateUp()
                },
            )
        }
    }
}
