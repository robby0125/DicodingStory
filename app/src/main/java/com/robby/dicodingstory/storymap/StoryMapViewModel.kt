package com.robby.dicodingstory.storymap

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.core.domain.usecase.StoryUseCase
import com.robby.dicodingstory.core.utils.Resource

class StoryMapViewModel(private val storyUseCase: StoryUseCase) : ViewModel() {
    fun getAllStoriesWithLocation(): LiveData<Resource<List<Story>>> =
        storyUseCase.getAllStoriesWithLocation().asLiveData()
}