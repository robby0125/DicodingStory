package com.robby.dicodingstory.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.core.domain.usecase.StoryUseCase

class HomeViewModel(private val storyUseCase: StoryUseCase) : ViewModel() {
    fun getAllStories(): LiveData<PagingData<Story>> = storyUseCase.getAllStories().asLiveData()
}