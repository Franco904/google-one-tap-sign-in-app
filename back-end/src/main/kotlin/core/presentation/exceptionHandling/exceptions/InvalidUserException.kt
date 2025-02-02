package com.example.core.presentation.exceptionHandling.exceptions

import com.example.core.domain.utils.ValidationError

class InvalidUserException(val error: ValidationError) : Exception("Invalid user data.")
