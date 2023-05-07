package com.robby.dicodingstory.core.domain.usecase

import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface StoryUseCase {
    fun getAllStories(): Flow<Resource<List<Story>>>
}