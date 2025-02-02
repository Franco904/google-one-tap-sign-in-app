package com.example.core.presentation.exceptionHandling.exceptions

import com.example.core.domain.utils.ValidationError

class InvalidSessionException(val error: ValidationError) : Exception("Invalid session data.")
