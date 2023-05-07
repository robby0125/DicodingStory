package com.robby.dicodingstory.core.di

import com.robby.dicodingstory.core.data.ApiService
import com.robby.dicodingstory.core.data.SessionManager
import com.robby.dicodingstory.core.data.repository.AuthRepositoryImpl
import com.robby.dicodingstory.core.data.repository.StoryRepositoryImpl
import com.robby.dicodingstory.core.domain.repository.AuthRepository
import com.robby.dicodingstory.core.domain.repository.StoryRepository
import com.robby.dicodingstory.core.domain.usecase.AuthUseCase
import com.robby.dicodingstory.core.domain.usecase.AuthUseCaseImpl
import com.robby.dicodingstory.core.domain.usecase.StoryUseCase
import com.robby.dicodingstory.core.domain.usecase.StoryUseCaseImpl
import com.robby.dicodingstory.utils.dataStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val dataModule = module {
    single {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        retrofit.create(ApiService::class.java)
    }

    single { SessionManager(androidContext().dataStore) }
}

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<StoryRepository> { StoryRepositoryImpl(get(), get()) }
}

val useCaseModule = module {
    single<AuthUseCase> { AuthUseCaseImpl(get()) }
    single<StoryUseCase> { StoryUseCaseImpl(get()) }
}