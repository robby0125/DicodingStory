package com.robby.dicodingstory.core.domain.usecase

import androidx.paging.PagingData
import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File

interface StoryUseCase {
    fun getAllStories(): Flow<PagingData<Story>>
    fun getAllStoriesWithLocation(): Flow<Resource<List<Story>>>
    fun getDetailStory(id: String): Flow<Resource<Story>>
    fun addStory(photo: File, description: String, lat: Double?, lon: Double?): Flow<Resource<Boolean>>
}