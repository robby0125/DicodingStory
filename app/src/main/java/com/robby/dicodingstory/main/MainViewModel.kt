package com.robby.dicodingstory.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.robby.dicodingstory.core.domain.usecase.AuthUseCase

class MainViewModel(authUseCase: AuthUseCase) : ViewModel() {
    val isLogin: LiveData<Boolean> = authUseCase.isLogin().asLiveData()
}