package com.robby.dicodingstory.core.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.robby.dicodingstory.core.data.SessionManager
import com.robby.dicodingstory.core.data.StoryRemoteMediator
import com.robby.dicodingstory.core.data.local.db.StoryDatabase
import com.robby.dicodingstory.core.data.remote.ApiService
import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.core.domain.repository.StoryRepository
import com.robby.dicodingstory.core.utils.Resource
import com.robby.dicodingstory.core.utils.toModel
import com.robby.dicodingstory.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryRepositoryImpl(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : StoryRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getAllStories(): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, sessionManager),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).flow.map { pagingData ->
            pagingData.map { storyEntity ->
                storyEntity.toModel()
            }
        }
    }

    override fun getAllStoriesWithLocation(): Flow<Resource<List<Story>>> = flow {
        emit(Resource.Loading())

        try {
            val token = sessionManager.getUserToken().first()
            val response = apiService.getAllStories("Bearer $token", location = 1)

            if (!response.error) {
                val listStory = response.listStory.map { it.toModel() }
                emit(Resource.Success(listStory))
            } else {
                emit(Resource.Error(response.message))
                Log.d(TAG, "getAllStoriesWithLocation: ${response.message}")
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.toString()))
            Log.d(TAG, "getAllStoriesWithLocation: $e")
        }
    }

    override fun getDetailStory(id: String): Flow<Resource<Story>> = flow {
        emit(Resource.Loading())

        try {
            val token = sessionManager.getUserToken().first()
            val response = apiService.getDetailStory("Bearer $token", id)

            if (!response.error) {
                val story = response.story.toModel()
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

    override fun addStory(
        photo: File,
        description: String,
        lat: Double?,
        lon: Double?
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())

        wrapEspressoIdlingResource {
            try {
                val requestDescription = description.toRequestBody("text/plain".toMediaType())
                val requestPhoto = photo.asRequestBody("image/jpeg".toMediaType())
                val imageMultipart = MultipartBody.Part.createFormData(
                    "photo",
                    photo.name,
                    requestPhoto
                )

                val token = sessionManager.getUserToken().first()
                val response = if (token.isNotEmpty()) {
                    apiService.addStory(
                        "Bearer $token",
                        imageMultipart,
                        requestDescription,
                        latitude = lat,
                        longitude = lon
                    )
                } else {
                    apiService.addStoryAsGuest(
                        imageMultipart,
                        requestDescription,
                        latitude = lat,
                        longitude = lon
                    )
                }

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
    }

    companion object {
        private const val TAG = "StoryRepositoryImpl"
    }
}