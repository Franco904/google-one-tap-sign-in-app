package com.example.one_tap_sign_in.profile

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.one_tap_sign_in.core.domain.repositories.UserRepository
import com.example.one_tap_sign_in.core.domain.utils.DataSourceError
import com.example.one_tap_sign_in.core.domain.utils.onError
import com.example.one_tap_sign_in.core.domain.utils.onSuccess
import com.example.one_tap_sign_in.core.presentation.utils.toUiMessage
import com.example.one_tap_sign_in.profile.models.UserCredentialsUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiEvents = MutableSharedFlow<UiEvents>()
    val uiEvents = _uiEvents.asSharedFlow()

    private val _userCredentialsUiState = MutableStateFlow(UserCredentialsUiState())
    val userCredentialsUiState = _userCredentialsUiState.asStateFlow()

    fun init() {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            userRepository.watchUser().collect { result ->
                result
                    .onSuccess { user ->
                        if (user.isNull()) return@onSuccess

                        _userCredentialsUiState.update {
                            UserCredentialsUiState(
                                email = user.email,
                                displayName = user.name,
                                profilePictureUrl = user.profilePictureUrl,
                            )
                        }
                    }
                    .onError { error ->
                        val redirectErrors = listOf(
                            DataSourceError.HttpError.Unauthorized,
                            DataSourceError.HttpError.NotFound,
                        )
                        if (error in redirectErrors) {
                            _uiEvents.emit(UiEvents.RedirectToSignIn)
                        }

                        _uiEvents.emit(UiEvents.DataSourceError(messageId = error.toUiMessage()))
                    }
            }
        }
    }

    fun onEditUser(newDisplayName: String) {
        if (userCredentialsUiState.value.displayName == newDisplayName) return
        if (newDisplayName.isBlank() || newDisplayName.length > 35) return

        viewModelScope.launch {
            userRepository.updateUser(newName = newDisplayName)
                .onSuccess {
                    _uiEvents.emit(UiEvents.EditUserSuccess)
                }
                .onError { error ->
                    val redirectErrors = listOf(
                        DataSourceError.HttpError.Unauthorized,
                        DataSourceError.HttpError.NotFound,
                    )
                    if (error in redirectErrors) {
                        _uiEvents.emit(UiEvents.RedirectToSignIn)
                    }

                    _uiEvents.emit(UiEvents.DataSourceError(messageId = error.toUiMessage()))
                }
        }
    }

    fun onDeleteUser() {
        viewModelScope.launch {
            userRepository.deleteUser()
                .onSuccess {
                    _uiEvents.emit(UiEvents.DeleteUserSuccess)
                }
                .onError { error ->
                    val redirectErrors = listOf(
                        DataSourceError.HttpError.Unauthorized,
                        DataSourceError.HttpError.NotFound,
                    )
                    if (error in redirectErrors) {
                        _uiEvents.emit(UiEvents.RedirectToSignIn)
                    }

                    _uiEvents.emit(UiEvents.DataSourceError(messageId = error.toUiMessage()))
                }
        }
    }

    fun onSignOutUser() {
        viewModelScope.launch {
            userRepository.signOutUser()
                .onSuccess {
                    _uiEvents.emit(UiEvents.SignOutUserSuccess)
                }
                .onError { error ->
                    val redirectErrors = listOf(
                        DataSourceError.HttpError.Unauthorized,
                        DataSourceError.HttpError.NotFound,
                    )

                    if (error in redirectErrors) {
                        _uiEvents.emit(UiEvents.RedirectToSignIn)
                    } else {
                        _uiEvents.emit(UiEvents.DataSourceError(messageId = error.toUiMessage()))
                    }
                }
        }
    }

    sealed interface UiEvents {
        data class ValidationError(@StringRes val messageId: Int) : UiEvents

        data class DataSourceError(@StringRes val messageId: Int) : UiEvents

        data object EditUserSuccess : UiEvents

        data object DeleteUserSuccess : UiEvents

        data object SignOutUserSuccess : UiEvents

        data object RedirectToSignIn : UiEvents
    }
}
