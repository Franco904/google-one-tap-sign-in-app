package com.example.one_tap_sign_in.signin

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.one_tap_sign_in.core.data.repository.UserRepository
import com.example.one_tap_sign_in.core.infra.auth.GoogleCredentialManager
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SignInViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiEvents = MutableSharedFlow<UiEvents>()
    val uiEvents = _uiEvents.asSharedFlow()

    fun signInUser(activityContext: Activity) {
        viewModelScope.launch {
            try {
                val credentials = GoogleCredentialManager.chooseGoogleAccountForSignIn(
                    activityContext = activityContext,
                    isSignIn = true,
                ) ?: GoogleCredentialManager.chooseGoogleAccountForSignIn(
                    activityContext = activityContext,
                    isSignIn = false, // sign up
                )

                if (credentials == null) {
                    _uiEvents.emit(UiEvents.SignInFailed)
                    return@launch
                }

                userRepository.authenticateUser(idToken = credentials.idToken)
                userRepository.saveUserCredentials(
                    displayName = credentials.displayName,
                    profilePictureUrl = credentials.profilePictureUrl,
                )

                _uiEvents.emit(UiEvents.SignInSucceded)
            } catch (e: Exception) {
                e.printStackTrace()
                ensureActive() // checks for CancellationException

                _uiEvents.emit(UiEvents.SignInFailed)
            }
        }
    }

    sealed interface UiEvents {
        data object SignInSucceded : UiEvents

        data object SignInFailed : UiEvents
    }
}
