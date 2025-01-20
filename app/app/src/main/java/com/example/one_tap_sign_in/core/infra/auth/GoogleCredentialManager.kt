package com.example.one_tap_sign_in.core.infra.auth

import android.app.Activity
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.example.one_tap_sign_in.core.constants.CLIENT_ID
import com.example.one_tap_sign_in.core.utils.data.CryptoUtils
import com.example.one_tap_sign_in.signin.models.GoogleUserCredentials
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

object GoogleCredentialManager {
    suspend fun chooseGoogleAccountForSignIn(
        activityContext: Activity,
        isSignIn: Boolean = true,
        mustEnableAutoSelectAccount: Boolean = true,
    ): GoogleUserCredentials? {
        val credentialManager = CredentialManager.create(activityContext)

        val googleIdOption: GetGoogleIdOption = if (isSignIn) {
            GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId(CLIENT_ID)
                .setAutoSelectEnabled(mustEnableAutoSelectAccount)
                .setNonce(CryptoUtils().generateNonce())
                .build()
        } else {
            GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(CLIENT_ID)
                .build()
        }

        val credentialRequest: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                context = activityContext,
                request = credentialRequest,
            )

            val credential = result.credential
            val isExpectedCredentialType = credential is CustomCredential
                    && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL

            return if (isExpectedCredentialType) {
                // Extracting the ID token to validate and authenticate on the back-end server.
                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)

                GoogleUserCredentials(
                    idToken = googleIdTokenCredential.idToken,
                    displayName = googleIdTokenCredential.displayName,
                    profilePictureUrl = googleIdTokenCredential.profilePictureUri?.toString(),
                )
            } else {
                Log.e("SignInScreen", "Unexpected type of credential.")
                null
            }
        } catch (e: GetCredentialException) {
            e.printStackTrace()
            return null
        }
    }

    suspend fun clearStateOnSignUp(activityContext: Activity) {
        val credentialManager = CredentialManager.create(activityContext)
        val clearCredentialStateRequest = ClearCredentialStateRequest()

        credentialManager.clearCredentialState(clearCredentialStateRequest)
    }
}