package com.robby.dicodingstory.home

import android.Manifest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import com.robby.dicodingstory.R
import com.robby.dicodingstory.addstory.AddStoryActivity
import com.robby.dicodingstory.addstory.CameraActivity
import com.robby.dicodingstory.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeActivityTest {
    @get:Rule
    val activity = ActivityScenarioRule(HomeActivity::class.java)

    @get:Rule
    val permission: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun addStory_Success() {
        Intents.init()

        onView(withId(R.id.fab_add_story)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_add_story)).perform(click())

        intended(hasComponent(CameraActivity::class.java.name))

        onView(withId(R.id.btn_rotate_camera)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_rotate_camera)).perform(click())
        onView(withId(R.id.btn_capture)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_capture)).perform(click())

        intended(hasComponent(AddStoryActivity::class.java.name))

        onView(withId(R.id.ed_add_description)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_add_description)).perform(typeText("Adding story from UI Test"))

        pressBack()

        onView(withId(R.id.button_add)).check(matches(isDisplayed()))
        onView(withId(R.id.button_add)).perform(click())

        intended(hasComponent(HomeActivity::class.java.name))
    }
}