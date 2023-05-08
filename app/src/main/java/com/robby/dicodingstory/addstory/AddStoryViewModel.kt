package com.robby.dicodingstory.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.robby.dicodingstory.core.domain.usecase.StoryUseCase
import com.robby.dicodingstory.core.utils.Resource
import java.io.File

class AddStoryViewModel(private val storyUseCase: StoryUseCase) : ViewModel() {
    fun addStory(photo: File, description: String): LiveData<Resource<Boolean>> =
        storyUseCase.addStory(photo, description).asLiveData()
}