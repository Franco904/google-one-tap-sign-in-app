package com.example.one_tap_sign_in.profile

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.one_tap_sign_in.core.data.repositories.UserRepository
import com.example.one_tap_sign_in.core.infra.auth.GoogleCredentialManager
import com.example.one_tap_sign_in.profile.models.UserCredentialsUiState
import kotlinx.coroutines.ensureActive
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
        getUserCredentials()
    }

    private fun getUserCredentials() {
        viewModelScope.launch {
            userRepository.watchUser().collect { user ->
                _userCredentialsUiState.update {
                    UserCredentialsUiState(
                        email = user.email,
                        displayName = user.name,
                        profilePictureUrl = user.profilePictureUrl,
                    )
                }
            }
        }
    }

    fun onEditUser(newDisplayName: String) {
        viewModelScope.launch {
            if (newDisplayName.length > 35) return@launch

            userRepository.updateUser(newName = newDisplayName)
        }
    }

    fun onDeleteUser(activityContext: Activity) {
        viewModelScope.launch {
            try {
                GoogleCredentialManager.clearStateOnSignUp(activityContext)

                userRepository.deleteUser()

                _uiEvents.emit(UiEvents.SignOutSucceded)
            } catch (e: Exception) {
                e.printStackTrace()
                ensureActive()

                _uiEvents.emit(UiEvents.SignOutFailed)
            }
        }
    }

    fun onSignOutUser(activityContext: Activity) {
        viewModelScope.launch {
            try {
                GoogleCredentialManager.clearStateOnSignUp(activityContext)

                userRepository.signOutUser()

                _uiEvents.emit(UiEvents.SignOutSucceded)
            } catch (e: Exception) {
                e.printStackTrace()
                ensureActive()

                _uiEvents.emit(UiEvents.SignOutFailed)
            }
        }
    }

    sealed interface UiEvents {
        data object SignOutSucceded : UiEvents

        data object SignOutFailed : UiEvents
    }
}
