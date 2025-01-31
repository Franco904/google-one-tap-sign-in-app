package com.example.one_tap_sign_in.core.presentation.exceptions

import androidx.credentials.exceptions.ClearCredentialUnsupportedException
import androidx.credentials.exceptions.GetCredentialUnsupportedException
import androidx.credentials.exceptions.NoCredentialException

sealed class CredentialManagerException(message: String) : Exception(message) {
    class UnsupportedCrendentialTypeException(type: String) :
        CredentialManagerException("Crendential type '$type' is unsupported.")

    class UnsupportedApiException(message: String?) :
        CredentialManagerException("Credential Manager unsupported: $message.")

    class NoCredentialsFoundException(message: String?) :
        CredentialManagerException("No crendentials found: $message.")

    class UnknownError(message: String?) : CredentialManagerException("Unexpected error: $message.")
}

fun Exception.toAuthRemoteApiException(): CredentialManagerException {
    return when (this) {
        is GetCredentialUnsupportedException, is ClearCredentialUnsupportedException -> {
            CredentialManagerException.UnsupportedApiException(message = "$cause: $message")
        }

        is NoCredentialException -> {
            CredentialManagerException.NoCredentialsFoundException(message = "$cause: $message")
        }

        else -> {
            CredentialManagerException.UnknownError(message = "$cause: $message")
        }
    }
}
