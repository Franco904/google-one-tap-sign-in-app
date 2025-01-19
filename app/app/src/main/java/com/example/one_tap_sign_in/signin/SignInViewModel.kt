package com.example.one_tap_sign_in.signin

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.one_tap_sign_in.R
import com.example.one_tap_sign_in.core.data.repository.UserRepository
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
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

    fun handleSignInCredential(idToken: String) {
        Log.i("view model", "Id token received from Google: $idToken")

        viewModelScope.launch {
            userRepository.authenticateUser(idToken = idToken)

            _uiEvents.emit(UiEvents.Snackbar(
                messageId = R.string.snackbar_sign_in_succeded,
                isError = false,
            ))
        }
    }

    sealed interface UiEvents {
        data class Snackbar(
            @StringRes val messageId: Int,
            val isError: Boolean,
        ) : UiEvents
    }
}
