package com.example.one_tap_sign_in.signin

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.one_tap_sign_in.R
import com.example.one_tap_sign_in.core.data.repository.UserRepository
import com.example.one_tap_sign_in.signin.models.GoogleUserCredentials
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SignInViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiEvents = MutableSharedFlow<UiEvents>()
    val uiEvents = _uiEvents.asSharedFlow()

    fun signInUser(credentials: GoogleUserCredentials) {
        viewModelScope.launch {
            try {
                userRepository.authenticateUser(idToken = credentials.idToken)
                userRepository.saveUserCredentials(
                    displayName = credentials.displayName,
                    profilePictureUrl = credentials.profilePictureUrl,
                )

                _uiEvents.emit(UiEvents.SignInSuccessed)

                _uiEvents.emit(
                    UiEvents.Snackbar(
                        messageId = R.string.snackbar_sign_in_succeded,
                        isError = false,
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                viewModelScope.ensureActive() // checks for CancellationException

                _uiEvents.emit(
                    UiEvents.Snackbar(
                        messageId = R.string.snackbar_sign_in_failed,
                        isError = true,
                    )
                )
            }
        }
    }

    sealed interface UiEvents {
        data class Snackbar(
            @StringRes val messageId: Int,
            val isError: Boolean,
        ) : UiEvents

        data object SignInSuccessed : UiEvents
    }
}
