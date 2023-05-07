package com.robby.dicodingstory.core.domain.usecase

import com.robby.dicodingstory.core.domain.model.User
import com.robby.dicodingstory.core.domain.repository.AuthRepository
import com.robby.dicodingstory.core.utils.Resource
import kotlinx.coroutines.flow.Flow

class AuthUseCaseImpl(private val repository: AuthRepository) : AuthUseCase {
    override fun login(email: String, password: String): Flow<Resource<User>> =
        repository.login(email, password)

    override fun register(name: String, email: String, password: String): Flow<Resource<Boolean>> =
        repository.register(name, email, password)

    override fun isLogin(): Flow<Boolean> = repository.isLogin()
}