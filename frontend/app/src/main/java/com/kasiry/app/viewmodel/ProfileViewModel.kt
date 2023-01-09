package com.kasiry.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kasiry.app.models.data.Profile
import com.kasiry.app.repositories.AuthRepository
import com.kasiry.app.repositories.ProfileRepository
import com.kasiry.app.utils.datastore.accessToken
import com.kasiry.app.utils.http.HttpCallback
import com.kasiry.app.utils.http.HttpState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    application: Application,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
): AndroidViewModel(application) {
    private val _profileState = MutableStateFlow<HttpState<Profile>?>(null)
    val profileState = _profileState.asStateFlow()

    private val _logout = MutableStateFlow<HttpState<Any>?>(null)
    val logout = _logout.asStateFlow()

    fun logout(
        callback: HttpCallback<Any>.() -> Unit,
    ): Job {
        _logout.value = HttpState.Loading()
        val context = getApplication<Application>().applicationContext

        return viewModelScope.launch {
            val response = authRepository.logout()
            _logout.value = response

            context.accessToken = null

            callback(HttpCallback(response))
        }
    }

    fun setProfile(profile: Profile) {
        _profileState.value = HttpState.Success(profile)
    }

    fun removeProfile() {
        _profileState.value = HttpState.Error("Profile not found")
    }

    fun getProfile(
        callback: (HttpCallback<Profile>.() -> Unit)? = null,
    ) {
        _profileState.value = HttpState.Loading()

        viewModelScope.launch(Dispatchers.Main) {
            val result = profileRepository.get()
            _profileState.value = result

            if (callback != null) {
                callback(HttpCallback(result))
            }
        }
    }

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