package com.robby.dicodingstory.core.domain.repository

import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface StoryRepository {
    fun getAllStories(): Flow<Resource<List<Story>>>
}