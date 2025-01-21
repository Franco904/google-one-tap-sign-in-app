package com.example.one_tap_sign_in

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.one_tap_sign_in.DestinationsHandler.destinations
import com.example.one_tap_sign_in.core.composables.AppSnackbarHost
import com.example.one_tap_sign_in.core.theme.AppCustomColors
import com.example.one_tap_sign_in.core.theme.AppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AppTheme {
                val coroutineScope = rememberCoroutineScope()

                val snackbarHostState = remember { SnackbarHostState() }
                var snackbarContainerColor by remember { mutableStateOf(AppCustomColors.green300) }

                val navController = rememberNavController()

                Scaffold(
                    snackbarHost = {
                        AppSnackbarHost(
                            snackbarHostState = snackbarHostState,
                            snackbarContainerColor = snackbarContainerColor,
                        )
                    },
                ) { contentPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Destinations.SignIn,
                        builder = {
                            destinations(
                                navController = navController,
                                showSnackbar = { message, color ->
                                    coroutineScope.launch {
                                        snackbarContainerColor = color
                                        snackbarHostState.showSnackbar(message = message)
                                    }
                                }
                            )
                        },
                        modifier = Modifier
                            .padding(
                                start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
                                end = contentPadding.calculateEndPadding(LayoutDirection.Ltr),
                            )
                    )
                }
            }
        }
    }
}
