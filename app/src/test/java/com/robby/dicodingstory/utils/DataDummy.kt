package com.robby.dicodingstory.utils

import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.core.domain.model.User

object DataDummy {
    fun generateDummyStories(): List<Story> {
        val storyList = ArrayList<Story>()

        for (i in 0..10) {
            val story = Story(
                i.toString(),
                "Story $i",
                "Dummy story $i",
                "Story photo $i",
                "Story create date $i"
            )

            storyList.add(story)
        }

        return storyList
    }

    fun generateDummyUser(): User = User(
        "user1",
        "user dummy",
        "user_dummy_token"
    )
}