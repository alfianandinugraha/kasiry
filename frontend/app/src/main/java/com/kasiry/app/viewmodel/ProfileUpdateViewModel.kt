package com.kasiry.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kasiry.app.models.data.Profile
import com.kasiry.app.repositories.ProfileRepository
import com.kasiry.app.utils.http.HttpCallback
import com.kasiry.app.utils.http.HttpState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileUpdateViewModel(
    application: Application,
    private val profileRepository: ProfileRepository
): AndroidViewModel(application) {
    private val _update = MutableStateFlow<HttpState<Profile>?>(null)
    val update = _update.asStateFlow()

    fun update(
        profile: Profile,
        callback: HttpCallback<Profile>.() -> Unit,
    ) {
        _update.value = HttpState.Loading()

        viewModelScope.launch {
            val response = profileRepository.update(profile)
            _update.value = response

            callback(HttpCallback(response))
        }
    }
}