package com.example.one_tap_sign_in.signin

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.one_tap_sign_in.core.domain.repositories.UserRepository
import com.example.one_tap_sign_in.core.domain.utils.onError
import com.example.one_tap_sign_in.core.domain.utils.onSuccess
import com.example.one_tap_sign_in.core.presentation.utils.uiConverters.toUiMessage
import com.example.one_tap_sign_in.signin.models.GoogleUserCredentials
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignInViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiEvents = Channel<UiEvents>()
    val uiEvents = _uiEvents.receiveAsFlow()

    var didUserExplicitlySignOut = false

    fun checkUserDidExplicitlySignOut() {
        viewModelScope.launch {
            userRepository.didUserExplicitlySignOut()
                .onError { error ->
                    _uiEvents.send(UiEvents.DataSourceError(messageId = error.toUiMessage()))
                }
                .onSuccess { didExplicitlySignOut ->
                    didUserExplicitlySignOut = didExplicitlySignOut
                }
        }
    }

    fun onSignInUser(credentials: GoogleUserCredentials) {
        viewModelScope.launch {
            userRepository.signInUser(
                idToken = credentials.idToken,
                displayName = credentials.displayName,
                profilePictureUrl = credentials.profilePictureUrl,
            )
                .onError { error ->
                    _uiEvents.send(UiEvents.DataSourceError(messageId = error.toUiMessage()))
                }
                .onSuccess {
                    _uiEvents.send(UiEvents.SignInSuccess)
                }
        }
    }

    sealed interface UiEvents {
        data class DataSourceError(@StringRes val messageId: Int) : UiEvents

        data object SignInSuccess : UiEvents
    }
}
