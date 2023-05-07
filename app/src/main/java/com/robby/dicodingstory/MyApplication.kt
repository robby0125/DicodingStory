package com.robby.dicodingstory

import android.app.Application
import com.robby.dicodingstory.core.di.dataModule
import com.robby.dicodingstory.core.di.repositoryModule
import com.robby.dicodingstory.core.di.useCaseModule
import com.robby.dicodingstory.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
                    dataModule,
                    repositoryModule,
                    useCaseModule,
                    appModule
                )
            )
        }
    }
}