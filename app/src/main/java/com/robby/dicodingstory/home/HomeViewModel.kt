package com.robby.dicodingstory.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.core.domain.usecase.StoryUseCase
import com.robby.dicodingstory.core.utils.Resource

class HomeViewModel(private val storyUseCase: StoryUseCase) : ViewModel() {
    fun getAllStories(): LiveData<Resource<List<Story>>> = storyUseCase.getAllStories().asLiveData()
}