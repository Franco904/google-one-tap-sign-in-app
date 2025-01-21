package com.example.one_tap_sign_in.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.one_tap_sign_in.core.composables.AppCircularProgressIndicator
import com.example.one_tap_sign_in.core.navigation.Destinations
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun RootScreen(
    modifier: Modifier = Modifier,
    viewModel: RootViewModel = koinViewModel(),
    onNavigateToFirstDestination: (Any) -> Unit = { _ -> },
) {
    LaunchedEffect(Unit) {
        viewModel.uiEvents.collectLatest { uiEvent ->
            when (uiEvent) {
                is RootViewModel.UiEvents.SignInState -> {
                    val firstDestination: Any = if (uiEvent.isUserSignedIn) {
                        Destinations.Profile
                    } else Destinations.SignIn

                    onNavigateToFirstDestination(firstDestination)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier
    ) { contentPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = contentPadding.calculateEndPadding(LayoutDirection.Ltr),
                )
        ) {
            AppCircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
            )
        }
    }
}
