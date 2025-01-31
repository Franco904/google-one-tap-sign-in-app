package com.example.one_tap_sign_in.root

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.one_tap_sign_in.core.domain.repositories.UserRepository
import com.example.one_tap_sign_in.core.domain.utils.onError
import com.example.one_tap_sign_in.core.domain.utils.onSuccess
import com.example.one_tap_sign_in.core.presentation.utils.toUiMessage
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
            userRepository.isUserSignedIn()
                .onSuccess { isUserSignedIn ->
                    _uiEvents.emit(UiEvents.SignInState(isUserSignedIn))
                }
                .onError { error ->
                    _uiEvents.emit(UiEvents.DataSourceError(messageId = error.toUiMessage()))
                }
        }
    }

    sealed interface UiEvents {
        data class DataSourceError(@StringRes val messageId: Int) : UiEvents

        data class SignInState(val isUserSignedIn: Boolean) : UiEvents
    }
}
