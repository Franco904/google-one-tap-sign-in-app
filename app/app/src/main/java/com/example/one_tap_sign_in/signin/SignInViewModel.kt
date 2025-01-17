package com.example.one_tap_sign_in.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class SignInViewModel : ViewModel() {
    private val _isSigningIn = MutableStateFlow(false)
    val isSigningIn = _isSigningIn.asStateFlow()

    fun onSignIn() {
        viewModelScope.launch {
            _isSigningIn.update { true }

            delay(1.seconds)

            _isSigningIn.update { false }
        }
    }
}
