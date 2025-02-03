package com.example.one_tap_sign_in.core.application.di.modules

import com.example.one_tap_sign_in.profile.ProfileViewModel
import com.example.one_tap_sign_in.root.RootViewModel
import com.example.one_tap_sign_in.signin.SignInViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        RootViewModel(
            userRepository = get(),
        )
    }

    viewModel {
        SignInViewModel(
            userRepository = get(),
        )
    }

    viewModel {
        ProfileViewModel(
            userValidator = get(),
            userRepository = get(),
        )
    }
}
