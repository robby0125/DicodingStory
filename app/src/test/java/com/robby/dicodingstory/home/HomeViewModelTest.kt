package com.robby.dicodingstory.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.robby.dicodingstory.core.domain.model.Story
import com.robby.dicodingstory.core.domain.usecase.StoryUseCase
import com.robby.dicodingstory.utils.DataDummy
import com.robby.dicodingstory.utils.getOrAwaitValue
import com.robby.dicodingstory.utils.toList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyUseCase: StoryUseCase
    private lateinit var homeViewModel: HomeViewModel

    private val dummyStories = DataDummy.generateDummyStories()

    @Before
    fun setup() {
        homeViewModel = HomeViewModel(storyUseCase)
    }

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setupDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getAllStories should provide expected results`() = runTest {
        val expectedStories = PagingData.from(dummyStories)

        `when`(storyUseCase.getAllStories()).thenReturn(flowOf(expectedStories))

        val actualStories = homeViewModel.getAllStories().getOrAwaitValue()

        verify(storyUseCase).getAllStories()
        assertNotNull(actualStories)

        val actualStoryList = actualStories.toList()

        assertEquals(dummyStories.size, actualStoryList.size)
        assertEquals(dummyStories.first(), actualStoryList.first())
    }

    @Test
    fun `when getAllStories is empty should be return zero story list`() = runTest {
        val expectedStories = PagingData.from(listOf<Story>())

        `when`(storyUseCase.getAllStories()).thenReturn(flowOf(expectedStories))

        val actualStories = homeViewModel.getAllStories().getOrAwaitValue()

        verify(storyUseCase).getAllStories()
        assertEquals(0, actualStories.toList().size)
    }
}