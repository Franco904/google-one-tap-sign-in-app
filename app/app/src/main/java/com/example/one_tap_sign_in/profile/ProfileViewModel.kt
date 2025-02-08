package com.example.one_tap_sign_in.profile

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.one_tap_sign_in.R
import com.example.one_tap_sign_in.core.domain.repositories.UserRepository
import com.example.one_tap_sign_in.core.domain.utils.ValidationError
import com.example.one_tap_sign_in.core.domain.utils.onError
import com.example.one_tap_sign_in.core.domain.utils.onErrors
import com.example.one_tap_sign_in.core.domain.utils.onSuccess
import com.example.one_tap_sign_in.core.domain.validators.interfaces.UserValidator
import com.example.one_tap_sign_in.core.presentation.utils.uiConverters.toUiMessage
import com.example.one_tap_sign_in.profile.models.UserCredentialsUiState
import com.example.one_tap_sign_in.profile.models.UserFormUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userValidator: UserValidator,
    private val userRepository: UserRepository,
) : ViewModel() {
    private var isInitialized = false

    private val _uiEvents = Channel<UiEvents>()
    val uiEvents = _uiEvents.receiveAsFlow()

    private val _userCredentialsUiState = MutableStateFlow(UserCredentialsUiState())
    val userCredentialsUiState = _userCredentialsUiState.asStateFlow()

    private val _userFormUiState = MutableStateFlow(UserFormUiState())
    val userFormUiState: StateFlow<UserFormUiState> = _userFormUiState

    private val _isLoadingUser = MutableStateFlow(true)
    val isLoadingUser = _isLoadingUser.asStateFlow()

    fun loadUser() {
        if (isInitialized) return

        viewModelScope.launch {
            _isLoadingUser.update { true }

            userRepository.watchUser().collect { result ->
                result
                    .onError { error ->
                        _isLoadingUser.update { false }

                        if (error in userRepository.redirectErrors) {
                            _uiEvents.send(UiEvents.RedirectToSignIn)
                        }

                        _uiEvents.send(UiEvents.DataSourceError(messageId = error.toUiMessage()))
                    }
                    .onSuccess { user ->
                        _isLoadingUser.update { false }

                        if (user.isNull()) return@onSuccess

                        _userCredentialsUiState.update {
                            UserCredentialsUiState.fromUser(user)
                        }
                    }
            }
        }

        isInitialized = true
    }

    fun onEditUser(newDisplayName: String?) {
        viewModelScope.launch {
            val newUser = userCredentialsUiState.value.toUser().copy(name = newDisplayName)

            userValidator.validate(newUser)
                .onErrors { errors ->
                    val displayNameError = errors.firstOrNull { it is ValidationError.UserName }

                    _userFormUiState.update {
                        it.copy(displayNameError = displayNameError?.toUiMessage())
                    }
                }
                .onSuccess {
                    if (newDisplayName == userCredentialsUiState.value.displayName) {
                        _uiEvents.send(
                            UiEvents.EditUserSuccess(messageId = R.string.snackbar_edit_user_no_data_changed)
                        )
                        return@launch
                    }

                    updateUser(newDisplayName = newDisplayName)
                }
        }
    }

    private suspend fun updateUser(newDisplayName: String?) {
        userRepository.updateUser(newName = newDisplayName ?: "")
            .onError { error ->
                if (error in userRepository.redirectErrors) {
                    _uiEvents.send(UiEvents.RedirectToSignIn)
                }

                _uiEvents.send(
                    UiEvents.DataSourceError(messageId = error.toUiMessage()),
                )
            }
            .onSuccess {
                _uiEvents.send(
                    UiEvents.EditUserSuccess(messageId = R.string.snackbar_edit_user_succeeded)
                )
            }
    }

    fun clearDisplayNameFieldError() {
        _userFormUiState.update { it.copy(displayNameError = null) }
    }

    fun onDeleteUser() {
        viewModelScope.launch {
            userRepository.deleteUser()
                .onError { error ->
                    if (error in userRepository.redirectErrors) {
                        _uiEvents.send(UiEvents.RedirectToSignIn)
                    }

                    _uiEvents.send(UiEvents.DataSourceError(messageId = error.toUiMessage()))
                }
                .onSuccess {
                    _uiEvents.send(UiEvents.DeleteUserSuccess)
                }
        }
    }

    fun onSignOutUser() {
        viewModelScope.launch {
            userRepository.signOutUser()
                .onError { error ->
                    if (error in userRepository.redirectErrors) {
                        _uiEvents.send(UiEvents.RedirectToSignIn)
                    } else {
                        _uiEvents.send(UiEvents.DataSourceError(messageId = error.toUiMessage()))
                    }
                }
                .onSuccess {
                    _uiEvents.send(UiEvents.SignOutUserSuccess)
                }
        }
    }

    sealed interface UiEvents {
        data class DataSourceError(@StringRes val messageId: Int) : UiEvents

        data class EditUserSuccess(@StringRes val messageId: Int) : UiEvents

        data object DeleteUserSuccess : UiEvents

        data object SignOutUserSuccess : UiEvents

        data object RedirectToSignIn : UiEvents
    }
}
