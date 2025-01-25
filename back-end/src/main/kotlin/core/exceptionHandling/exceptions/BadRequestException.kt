package com.example.core.exceptionHandling.exceptions

class BadRequestException(message: String) : Exception(
    "Request body data is invalid: $message"
)
