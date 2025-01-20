package com.example.one_tap_sign_in.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.one_tap_sign_in.core.data.repository.UserRepository
import com.example.one_tap_sign_in.profile.models.UserCredentialsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _userCredentialsUiState = MutableStateFlow(UserCredentialsUiState())
    val userCredentialsUiState = _userCredentialsUiState.asStateFlow()

    fun init() {
        viewModelScope.launch {
            val (displayName, profilePictureUrl) = userRepository.readUserCredentials()

            _userCredentialsUiState.update {
                UserCredentialsUiState(
                    displayName = displayName,
                    profilePictureUrl = profilePictureUrl,
                )
            }
        }
    }

    suspend fun signOutUser() {
        userRepository.deleteUserCredentials()
    }
}
