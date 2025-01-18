package com.example.one_tap_sign_in.signin

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.one_tap_sign_in.R
import com.example.one_tap_sign_in.core.data.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class SignInViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiEvents = MutableSharedFlow<UiEvents>()
    val uiEvents = _uiEvents.asSharedFlow()

    private val _isSigningIn = MutableStateFlow(false)
    val isSigningIn = _isSigningIn.asStateFlow()

    fun onSignIn() {
        viewModelScope.launch {
            _isSigningIn.update { true }

            delay(1.seconds)

            val snackbarEvent = UiEvents.Snackbar(
                messageId = R.string.snackbar_internal_server_error,
                isError = true,
            )

            _uiEvents.emit(snackbarEvent)
            _isSigningIn.update { false }
        }
    }

    sealed interface UiEvents {
        data class Snackbar(
            @StringRes val messageId: Int,
            val isError: Boolean,
        ) : UiEvents
    }
}
