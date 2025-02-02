package com.example.one_tap_sign_in.root

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.one_tap_sign_in.core.domain.repositories.UserRepository
import com.example.one_tap_sign_in.core.domain.utils.onErrorAsync
import com.example.one_tap_sign_in.core.domain.utils.onSuccessAsync
import com.example.one_tap_sign_in.core.presentation.utils.uiConverters.toUiMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RootViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private var isInitialized = false

    private val _uiEvents = Channel<UiEvents>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun checkIsUserSignedIn() {
        if (isInitialized) return

        viewModelScope.launch {
            userRepository.isUserSignedIn()
                .onSuccessAsync { isUserSignedIn ->
                    _uiEvents.send(UiEvents.SignInState(isUserSignedIn))
                }
                .onErrorAsync { error ->
                    _uiEvents.send(UiEvents.DataSourceError(messageId = error.toUiMessage()))
                }
        }

        isInitialized = true
    }

    sealed interface UiEvents {
        data class DataSourceError(@StringRes val messageId: Int) : UiEvents

        data class SignInState(val isUserSignedIn: Boolean) : UiEvents
    }
}
