package com.kasiry.app

import com.kasiry.app.repositories.ProfileRepository
import com.kasiry.app.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    single {
        ProfileRepository(
            context = androidContext()
        )
    }

    single {
        MainViewModel(
            androidApplication(),
            profileRepository = get(),
        )
    }
}