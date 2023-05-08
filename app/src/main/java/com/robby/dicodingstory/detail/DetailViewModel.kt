package com.robby.dicodingstory.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.core.domain.usecase.StoryUseCase
import com.robby.dicodingstory.core.utils.Resource

class DetailViewModel(private val storyUseCase: StoryUseCase) : ViewModel() {
    fun getDetailStory(id: String): LiveData<Resource<Story>> =
        storyUseCase.getDetailStory(id).asLiveData()
}