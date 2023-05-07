package com.robby.dicodingstory.core.domain.repository

import com.robby.dicodingstory.core.domain.model.User
import com.robby.dicodingstory.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String): Flow<Resource<User>>
    fun register(name: String, email: String, password: String): Flow<Resource<Boolean>>
    fun isLogin(): Flow<Boolean>
}