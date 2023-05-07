package com.robby.dicodingstory.core.data.repository

import android.util.Log
import com.robby.dicodingstory.core.data.ApiService
import com.robby.dicodingstory.core.data.SessionManager
import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.core.domain.repository.StoryRepository
import com.robby.dicodingstory.core.utils.DataMapper
import com.robby.dicodingstory.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class StoryRepositoryImpl(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : StoryRepository {
    override fun getAllStories(): Flow<Resource<List<Story>>> = flow {
        emit(Resource.Loading())

        try {
            val token = sessionManager.getUserToken().first()
            val response = apiService.getAllStories("Bearer $token")

            if (!response.error) {
                val listStory = response.listStory.map { DataMapper.mapStoryResponseToModel(it) }
                emit(Resource.Success(listStory))
            } else {
                emit(Resource.Error(response.message))
                Log.d(TAG, "getAllStories: ${response.message}")
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.toString()))
            Log.d(TAG, "getAllStories: $e")
        }
    }

    companion object {
        private const val TAG = "StoryRepositoryImpl"
    }
}