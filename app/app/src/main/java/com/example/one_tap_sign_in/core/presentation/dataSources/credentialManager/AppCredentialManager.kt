package com.example.one_tap_sign_in.core.presentation.dataSources.credentialManager

import android.app.Activity
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.one_tap_sign_in.core.data.constants.CLIENT_ID
import com.example.one_tap_sign_in.core.data.utils.CryptoUtils
import com.example.one_tap_sign_in.core.presentation.exceptions.CredentialManagerException
import com.example.one_tap_sign_in.core.presentation.exceptions.asCredentialManagerException
import com.example.one_tap_sign_in.signin.models.GoogleUserCredentials
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

object AppCredentialManager {
    suspend fun chooseGoogleAccount(
        activityContext: Activity,
        isSignIn: Boolean,
        mustEnableAutoSelect: Boolean,
    ): GoogleUserCredentials {
        val credentialManager = CredentialManager.create(activityContext)

        val googleIdOption: GetGoogleIdOption = if (isSignIn) {
            GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId(CLIENT_ID)
                .setAutoSelectEnabled(mustEnableAutoSelect)
                .setNonce(CryptoUtils.generateNonce())
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
            val isSupportedCredentialType = credential is CustomCredential
                    && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL

            if (!isSupportedCredentialType) {
                throw CredentialManagerException.UnsupportedCrendentialTypeException(
                    type = credential.type,
                )
            }

            // Extracting the ID token to validate and authenticate on the back-end server.
            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(credential.data)

            return GoogleUserCredentials(
                idToken = googleIdTokenCredential.idToken,
                displayName = googleIdTokenCredential.displayName,
                profilePictureUrl = googleIdTokenCredential.profilePictureUri?.toString(),
            )
        } catch (e: Exception) {
            if (e is CredentialManagerException) throw e

            throw e.asCredentialManagerException()
        }
    }

    suspend fun clearGoogleCredentialState(activityContext: Activity) {
        val credentialManager = CredentialManager.create(activityContext)
        val clearCredentialStateRequest = ClearCredentialStateRequest()

        try {
            credentialManager.clearCredentialState(clearCredentialStateRequest)
        } catch (e: Exception) {
            if (e is CredentialManagerException) throw e

            throw e.asCredentialManagerException()
        }
    }
}
