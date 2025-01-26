package com.example.one_tap_sign_in.signin

import android.app.Activity
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.one_tap_sign_in.R
import com.example.one_tap_sign_in.core.data.repositories.UserRepository
import com.example.one_tap_sign_in.core.infra.auth.GoogleCredentialManager
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

    fun signInUser(activityContext: Activity) {
        viewModelScope.launch {
            try {
                // To show Google's select account UI
                val credentials = getGoogleUserCredentials(activityContext)

                if (credentials == null) {
                    _uiEvents.emit(
                        UiEvents.SignInFailed(messageId = R.string.snackbar_sign_in_no_google_accounts),
                    )
                    return@launch
                }

                userRepository.signInUser(
                    idToken = credentials.idToken,
                    displayName = credentials.displayName,
                    profilePictureUrl = credentials.profilePictureUrl,
                )

                _uiEvents.emit(UiEvents.SignInSucceded)
            } catch (e: Exception) {
                e.printStackTrace()
                ensureActive() // checks for CancellationException

                _uiEvents.emit(
                    UiEvents.SignInFailed(messageId = R.string.snackbar_sign_in_failed),
                )
            }
        }
    }

    private suspend fun getGoogleUserCredentials(activityContext: Activity): GoogleUserCredentials? {
        return GoogleCredentialManager.chooseGoogleAccountForSignIn(
            activityContext = activityContext,
            isSignIn = true,
        ) ?: GoogleCredentialManager.chooseGoogleAccountForSignIn(
            activityContext = activityContext,
            isSignIn = false, // sign up
        )
    }

    sealed interface UiEvents {
        data object SignInSucceded : UiEvents

        data class SignInFailed(@StringRes val messageId: Int) : UiEvents
    }
}
