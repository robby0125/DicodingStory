package com.robby.dicodingstory.core.utils

import com.robby.dicodingstory.core.data.local.entity.StoryEntity
import com.robby.dicodingstory.core.data.remote.response.StoryResponse
import com.robby.dicodingstory.core.data.remote.response.UserResponse
import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.core.domain.model.User

fun StoryResponse.toEntity(): StoryEntity =
    StoryEntity(id, name, description, photoUrl, createdAt, lat, lon)

fun StoryResponse.toModel(): Story = Story(id, name, description, photoUrl, createdAt, lat, lon)

fun StoryEntity.toModel(): Story = Story(id, name, description, photoUrl, createdAt, lat, lon)

fun UserResponse.toModel(): User = User(
    userId,
    name,
    token
)