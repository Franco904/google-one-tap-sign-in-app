package com.example.core.security.session

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(val count: Int = 0)
