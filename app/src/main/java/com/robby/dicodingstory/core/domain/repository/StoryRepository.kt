package com.robby.dicodingstory.core.domain.repository

import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.core.utils.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File

interface StoryRepository {
    fun getAllStories(location: Int = 0): Flow<Resource<List<Story>>>
    fun getDetailStory(id: String): Flow<Resource<Story>>
    fun addStory(photo: File, description: String): Flow<Resource<Boolean>>
}