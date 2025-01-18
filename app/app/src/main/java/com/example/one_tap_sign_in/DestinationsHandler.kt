package com.example.one_tap_sign_in

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.one_tap_sign_in.signin.SignInScreen
import org.koin.androidx.compose.koinViewModel

object DestinationsHandler {
    fun NavGraphBuilder.destinations() {
        composable<Destinations.SignIn> {
            SignInScreen(
                viewModel = koinViewModel(),
            )
        }
    }
}
