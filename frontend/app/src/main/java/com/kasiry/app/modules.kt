package com.kasiry.app

import com.kasiry.app.repositories.*
import com.kasiry.app.viewmodel.MainViewModel
import com.kasiry.app.viewmodel.ProductViewModel
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
        CategoryRepository(
            context = androidContext()
        )
    }

    factory {
        AssetRepository(
            context = androidContext()
        )
    }

    single {
        MainViewModel(
            androidApplication(),
            profileRepository = get(),
        )
    }

    factory {
        ProductRepository(
            context = androidContext()
        )
    }

    single {
        ProductViewModel(
            androidApplication(),
            productRepository = get(),
        )
    }

    factory {
        EmployeeRepository(
            context = androidContext()
        )
    }
}