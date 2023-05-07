package com.robby.dicodingstory.core.domain.usecase

import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.core.domain.repository.StoryRepository
import com.robby.dicodingstory.core.utils.Resource
import kotlinx.coroutines.flow.Flow

class StoryUseCaseImpl(private val storyRepository: StoryRepository) : StoryUseCase {
    override fun getAllStories(): Flow<Resource<List<Story>>> = storyRepository.getAllStories()
}