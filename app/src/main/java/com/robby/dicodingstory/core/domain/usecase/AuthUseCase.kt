package com.robby.dicodingstory.core.domain.usecase

import com.robby.dicodingstory.core.domain.model.User
import com.robby.dicodingstory.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {
    fun login(email: String, password: String): Flow<Resource<User>>
    fun register(name: String, email: String, password: String): Flow<Resource<Boolean>>
    fun isLogin(): Flow<Boolean>
}