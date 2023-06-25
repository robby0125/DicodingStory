package com.robby.dicodingstory.core.domain.usecase

import androidx.paging.PagingData
import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.core.domain.repository.StoryRepository
import com.robby.dicodingstory.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File

class StoryUseCaseImpl(private val storyRepository: StoryRepository) : StoryUseCase {
    override fun getAllStories(): Flow<PagingData<Story>> =
        storyRepository.getAllStories()

    override fun getAllStoriesWithLocation(): Flow<Resource<List<Story>>> =
        storyRepository.getAllStoriesWithLocation()

    override fun getDetailStory(id: String): Flow<Resource<Story>> =
        storyRepository.getDetailStory(id)

    override fun addStory(photo: File, description: String, lat: Double?, lon: Double?): Flow<Resource<Boolean>> =
        storyRepository.addStory(photo, description, lat, lon)
}