package com.example.one_tap_sign_in

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.one_tap_sign_in.profile.ProfileScreen
import com.example.one_tap_sign_in.signin.SignInScreen
import org.koin.androidx.compose.koinViewModel

object DestinationsHandler {
    fun NavGraphBuilder.destinations(navController: NavController) {
        composable<Destinations.SignIn> {
            SignInScreen(
                viewModel = koinViewModel(),
                onSignInSuccessed = {
                    navController.navigate(Destinations.Profile)
                }
            )
        }

        composable<Destinations.Profile> {
            ProfileScreen(
                onNavigateUp = {
                    navController.navigateUp()
                },
            )
        }
    }
}
