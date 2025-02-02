package com.example.core.presentation.exceptionHandling.exceptions

class SessionExpiredException
    : IllegalStateException("User session expired. Sign in again.")
