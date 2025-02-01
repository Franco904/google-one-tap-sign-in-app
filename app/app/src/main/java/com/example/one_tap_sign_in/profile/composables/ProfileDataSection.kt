package com.example.one_tap_sign_in.profile.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ProfileDataSection(
    profilePictureUrl: String,
    displayName: String,
    isSigningOut: Boolean,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = profilePictureUrl,
        contentDescription = null,
        modifier = modifier
            .size(64.dp)
            .clip(CircleShape)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = displayName,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
    )
    Spacer(modifier = Modifier.height(24.dp))
    // TODO: Add sign out confirmation dialog
    SignOutButton(
        isSigningOut = isSigningOut,
        onSignOut = onSignOut,
    )
}
