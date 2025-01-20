package com.example.one_tap_sign_in.core.di

import com.example.one_tap_sign_in.profile.ProfileViewModel
import com.example.one_tap_sign_in.signin.SignInViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SignInViewModel(
            userRepository = get(),
        )
    }

    viewModel {
        ProfileViewModel(
            userRepository = get(),
        )
    }
}
