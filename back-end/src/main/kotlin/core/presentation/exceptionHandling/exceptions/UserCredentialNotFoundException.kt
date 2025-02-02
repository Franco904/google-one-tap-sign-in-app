package com.example.core.presentation.exceptionHandling.exceptions

class UserCredentialNotFoundException
    : IllegalStateException("User credential not found for the provided id token.")
