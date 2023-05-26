package com.robby.dicodingstory.di

import com.robby.dicodingstory.addstory.AddStoryViewModel
import com.robby.dicodingstory.authentication.AuthViewModel
import com.robby.dicodingstory.detail.DetailViewModel
import com.robby.dicodingstory.home.HomeViewModel
import com.robby.dicodingstory.main.MainViewModel
import com.robby.dicodingstory.storymap.StoryMapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { AddStoryViewModel(get()) }
    viewModel { StoryMapViewModel(get()) }
}