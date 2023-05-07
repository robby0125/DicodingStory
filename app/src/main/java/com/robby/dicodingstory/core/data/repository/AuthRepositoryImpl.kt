package com.robby.dicodingstory.core.data.repository

import android.util.Log
import com.robby.dicodingstory.core.data.ApiService
import com.robby.dicodingstory.core.data.SessionManager
import com.robby.dicodingstory.core.domain.model.User
import com.robby.dicodingstory.core.domain.repository.AuthRepository
import com.robby.dicodingstory.core.utils.DataMapper
import com.robby.dicodingstory.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : AuthRepository {
    override fun login(email: String, password: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading())

        try {
            val response = apiService.login(email, password)

            if (!response.error) {
                val userResponse = response.userResponse
                val user = DataMapper.mapUserResponseToModel(userResponse)

                sessionManager.createLoginSession()
                sessionManager.saveUserToken(user.token)

                emit(Resource.Success(user))
            } else {
                emit(Resource.Error(response.message))
                Log.d(TAG, "login: ${response.message}")
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.toString()))
            Log.d(TAG, "login: $e")
        }
    }

    override fun register(name: String, email: String, password: String): Flow<Resource<Boolean>> =
        flow {
            emit(Resource.Loading())

            try {
                val response = apiService.register(name, email, password)

                if (!response.error) {
                    emit(Resource.Success(true))
                } else {
                    emit(Resource.Error(response.message))
                    Log.d(TAG, "register: ${response.message}")
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.toString()))
                Log.d(TAG, "register: $e")
            }
        }

    override fun isLogin(): Flow<Boolean> = sessionManager.isLogin()

    companion object {
        private const val TAG = "AuthRepositoryImpl"
    }
}