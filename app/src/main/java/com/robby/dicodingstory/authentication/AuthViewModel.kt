package com.robby.dicodingstory.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.robby.dicodingstory.core.domain.model.User
import com.robby.dicodingstory.core.domain.usecase.AuthUseCase
import com.robby.dicodingstory.core.utils.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AuthViewModel(private val authUseCase: AuthUseCase) : ViewModel() {
    fun login(email: String, password: String): LiveData<Resource<User>> =
        authUseCase.login(email, password).asLiveData()

    fun register(name: String, email: String, password: String): LiveData<Resource<Boolean>> =
        authUseCase.register(name, email, password).asLiveData()

    fun logout() {
        viewModelScope.launch {
            authUseCase.logout().collect()
        }
    }
}