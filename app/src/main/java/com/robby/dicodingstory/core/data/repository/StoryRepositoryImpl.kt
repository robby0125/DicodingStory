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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryRepositoryImpl(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : StoryRepository {
    override fun getAllStories(location: Int): Flow<Resource<List<Story>>> = flow {
        emit(Resource.Loading())

        try {
            val token = sessionManager.getUserToken().first()
            val response = apiService.getAllStories("Bearer $token", location)

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

    override fun getDetailStory(id: String): Flow<Resource<Story>> = flow {
        emit(Resource.Loading())

        try {
            val token = sessionManager.getUserToken().first()
            val response = apiService.getDetailStory("Bearer $token", id)

            if (!response.error) {
                val story = DataMapper.mapStoryResponseToModel(response.story)
                emit(Resource.Success(story))
            } else {
                emit(Resource.Error(response.message))
                Log.d(TAG, "getDetailStory: ${response.message}")
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.toString()))
            Log.d(TAG, "getDetailStory: $e")
        }
    }

    override fun addStory(photo: File, description: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())

        try {
            val requestDescription = description.toRequestBody("text/plain".toMediaType())
            val requestPhoto = photo.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart = MultipartBody.Part.createFormData(
                "photo",
                photo.name,
                requestPhoto
            )

            val token = sessionManager.getUserToken().first()
            val response = apiService.addStory("Bearer $token", imageMultipart, requestDescription)

            if (!response.error) {
                emit(Resource.Success(true))
            } else {
                emit(Resource.Error(response.message))
                Log.d(TAG, "addStory: ${response.message}")
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.toString()))
            Log.d(TAG, "addStory: $e")
        }
    }

    companion object {
        private const val TAG = "StoryRepositoryImpl"
    }
}