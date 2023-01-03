package com.kasiry.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kasiry.app.models.data.Profile
import com.kasiry.app.repositories.ProfileRepository
import com.kasiry.app.utils.http.HttpCallback
import com.kasiry.app.utils.http.HttpState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
    private val profileRepository: ProfileRepository,
): AndroidViewModel(application) {
    private val _profile = MutableStateFlow<HttpState<Profile>?>(null)
    val profile = _profile.asStateFlow()

    fun getProfile(
        callback: (HttpCallback<Profile>.() -> Unit)? = null,
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            val result = profileRepository.get()
            _profile.value = result

            if (callback != null) {
                callback(HttpCallback(result))
            }
        }
    }
}