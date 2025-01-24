package com.example.user

import com.example.core.data.repositories.apis.UserRepository

class UserService(
    private val userRepository: UserRepository,
) {
    fun signInWithGoogle(idToken: String) {}
}
