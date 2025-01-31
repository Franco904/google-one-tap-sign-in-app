package com.example.one_tap_sign_in.core.presentation.utils

import com.example.one_tap_sign_in.R
import com.example.one_tap_sign_in.core.domain.utils.DataSourceError
import com.example.one_tap_sign_in.core.presentation.exceptions.CredentialManagerException

fun DataSourceError.toUiMessage() = when (this) {
    DataSourceError.PreferencesError.SerializationError -> {
        R.string.snackbar_preferences_serialization_error
    }

    DataSourceError.PreferencesError.SecurityError -> {
        R.string.snackbar_preferences_security_error
    }

    DataSourceError.PreferencesError.UnknownError -> {
        R.string.snackbar_preferences_unknown_error
    }

    DataSourceError.HttpError.BadRequest -> {
        R.string.snackbar_http_bad_request_error
    }

    DataSourceError.HttpError.Unauthorized -> {
        R.string.snackbar_http_unauthorized_error
    }

    DataSourceError.HttpError.Forbidden -> {
        R.string.snackbar_http_forbidden_error
    }

    DataSourceError.HttpError.NotFound -> {
        R.string.snackbar_http_not_found_error
    }

    DataSourceError.HttpError.ServerError -> {
        R.string.snackbar_http_server_error
    }

    DataSourceError.HttpError.UnknownHttpRemoteError -> {
        R.string.snackbar_http_unknown_http_response_error
    }

    DataSourceError.HttpError.SerializationError -> {
        R.string.snackbar_http_serialization_error
    }

    DataSourceError.HttpError.Timeout -> {
        R.string.snackbar_http_timeout_error
    }

    DataSourceError.HttpError.NetworkError -> {
        R.string.snackbar_http_network_internet
    }

    DataSourceError.HttpError.UnknownError -> {
        R.string.snackbar_http_unknown_error
    }
}

fun CredentialManagerException.toUiMessage() = when (this) {
    is CredentialManagerException.UnsupportedCrendentialTypeException -> {
        R.string.snackbar_credential_unsupported_type_error
    }

    is CredentialManagerException.UnsupportedApiException -> {
        R.string.snackbar_credential_unsupported_api_error
    }

    is CredentialManagerException.NoCredentialsFoundException -> {
        R.string.snackbar_credential_no_credentials_found_error
    }

    is CredentialManagerException.UnknownError -> {
        R.string.snackbar_credential_unknown_error
    }
}
