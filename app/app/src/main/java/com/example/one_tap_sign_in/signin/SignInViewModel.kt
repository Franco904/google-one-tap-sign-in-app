package com.example.one_tap_sign_in.signin

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.one_tap_sign_in.core.domain.repositories.UserRepository
import com.example.one_tap_sign_in.core.domain.utils.onError
import com.example.one_tap_sign_in.core.domain.utils.onSuccess
import com.example.one_tap_sign_in.core.presentation.utils.toUiMessage
import com.example.one_tap_sign_in.root.RootViewModel.UiEvents
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
                .onSuccess { didExplicitlySignOut ->
                    didUserExplicitlySignOut = didExplicitlySignOut
                }
                .onError { error ->
                    _uiEvents.send(UiEvents.DataSourceError(messageId = error.toUiMessage()))
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
                .onSuccess {
                    _uiEvents.send(UiEvents.SignInSuccess)
                }
                .onError { error ->
                    _uiEvents.send(UiEvents.DataSourceError(messageId = error.toUiMessage()))
                }
        }
    }

    sealed interface UiEvents {
        data class DataSourceError(@StringRes val messageId: Int) : UiEvents

        data object SignInSuccess : UiEvents
    }
}
