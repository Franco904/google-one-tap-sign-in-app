package com.example.one_tap_sign_in.core.presentation.utils.uiConverters

import com.example.one_tap_sign_in.R
import com.example.one_tap_sign_in.core.domain.utils.ValidationError

fun ValidationError.toUiMessage() = when (this) {
    ValidationError.UserName.IsBlank -> {
        R.string.inline_user_name_is_blank
    }

    ValidationError.UserName.LengthIsGreaterThan35Chars -> {
        R.string.inline_user_name_too_big
    }
}
