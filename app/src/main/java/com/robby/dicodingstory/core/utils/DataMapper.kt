package com.robby.dicodingstory.core.utils

import com.robby.dicodingstory.core.data.response.StoryResponse
import com.robby.dicodingstory.core.data.response.UserResponse
import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.core.domain.model.User

object DataMapper {
    fun mapUserResponseToModel(userResponse: UserResponse): User = User(
        userId = userResponse.userId,
        name = userResponse.name,
        token = userResponse.token
    )

    fun mapStoryResponseToModel(storyResponse: StoryResponse): Story = Story(
        id = storyResponse.id,
        name = storyResponse.name,
        description = storyResponse.description,
        photoUrl = storyResponse.photoUrl,
        createdAt = storyResponse.createdAt,
        lat = storyResponse.lat,
        lon = storyResponse.lon
    )
}