package com.example.one_tap_sign_in.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.one_tap_sign_in.core.data.repository.UserRepository
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RootViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiEvents = MutableSharedFlow<UiEvents>()
    val uiEvents = _uiEvents.asSharedFlow()

    fun init() {
        checkIsUserSignedIn()
    }

    private fun checkIsUserSignedIn() {
        viewModelScope.launch {
            try {
                val isUserSignedIn = userRepository.readIsSignedIn() ?: false

                _uiEvents.emit(UiEvents.SignInState(isUserSignedIn))
            } catch (e: Exception) {
                e.printStackTrace()
                ensureActive()
            }
        }
    }

    sealed interface UiEvents {
        data class SignInState(val isUserSignedIn: Boolean) : UiEvents
    }
}
