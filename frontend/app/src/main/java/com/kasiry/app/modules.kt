package com.kasiry.app

import com.kasiry.app.repositories.*
import com.kasiry.app.viewmodel.CartViewModel
import com.kasiry.app.viewmodel.MainViewModel
import com.kasiry.app.viewmodel.ProductViewModel
import com.kasiry.app.viewmodel.ProfileViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    factory {
        ProfileRepository(
            context = androidContext()
        )
    }

    factory {
        CategoryRepository(
            context = androidContext()
        )
    }

    factory {
        AssetRepository(
            context = androidContext()
        )
    }

    factory {
        AuthRepository(
            context = androidContext()
        )
    }

    factory {
        ProductRepository(
            context = androidContext()
        )
    }

    single {
        MainViewModel(
            androidApplication(),
            profileRepository = get(),
        )
    }

    single {
        ProductViewModel(
            androidApplication(),
            productRepository = get(),
            assetRepository = get()
        )
    }

    single {
        CartViewModel(
            androidApplication(),
        )
    }

    single {
        ProfileViewModel(
            androidApplication(),
            authRepository = get(),
            profileRepository = get(),
        )
    }

    factory {
        EmployeeRepository(
            context = androidContext()
        )
    }
}