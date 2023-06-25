package com.robby.dicodingstory.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.robby.dicodingstory.core.data.local.entity.RemoteKeys
import com.robby.dicodingstory.core.data.local.entity.StoryEntity

@Database(
    entities = [StoryEntity::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}