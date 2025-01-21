package com.example.one_tap_sign_in.core.composables

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppCircularProgressIndicator(
    modifier: Modifier = Modifier,
) {
    CircularProgressIndicator(
        color = MaterialTheme.colorScheme.primary,
        trackColor = MaterialTheme.colorScheme.secondaryContainer,
        strokeWidth = 2.dp,
        modifier = modifier
    )
}