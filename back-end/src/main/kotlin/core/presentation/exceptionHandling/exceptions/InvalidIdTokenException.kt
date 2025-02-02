package com.example.core.presentation.exceptionHandling.exceptions

import com.example.core.domain.utils.ValidationError

class InvalidIdTokenException(val error: ValidationError) : Exception("Invalid id token.")
