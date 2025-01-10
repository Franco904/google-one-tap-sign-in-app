package com.example.one_tap_sign_in

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.one_tap_sign_in.signin.SignInScreen

object RouteHandler {
    fun NavGraphBuilder.routes(navController: NavController) {
        composable<Routes.SignIn> {
            SignInScreen()
        }
    }
}
