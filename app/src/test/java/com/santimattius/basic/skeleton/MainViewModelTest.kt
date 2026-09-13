package com.santimattius.basic.skeleton

import com.santimattius.basic.skeleton.tools.rules.MainCoroutinesTestRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val mainCoroutinesTestRule = MainCoroutinesTestRule()

    @Test
    fun `initial collection triggers sayHello and settles on the Hello message`() =
        runTest(mainCoroutinesTestRule.testDispatcher) {
            val random = mockk<Random> { every { nextBoolean() } returns true }
            val viewModel = MainViewModel(random)

            val states = mutableListOf<MainUiState>()
            val job = launch { viewModel.state.toList(states) }
            advanceUntilIdle()
            job.cancel()

            assertTrue(states.any { it.isLoading })
            assertFalse(states.last().isLoading)
            assertEquals("Hello, Android!", states.last().message)
        }

    @Test
    fun `sayHello sets message to Hello when random returns true`() =
        runTest(mainCoroutinesTestRule.testDispatcher) {
            val random = mockk<Random> { every { nextBoolean() } returns true }
            val viewModel = MainViewModel(random)

            val states = mutableListOf<MainUiState>()
            val job = launch { viewModel.state.toList(states) }
            advanceUntilIdle()

            viewModel.sayHello()
            advanceUntilIdle()
            job.cancel()

            assertFalse(states.last().isLoading)
            assertEquals("Hello, Android!", states.last().message)
        }

    @Test
    fun `sayHello sets message to Goodbye when random returns false`() =
        runTest(mainCoroutinesTestRule.testDispatcher) {
            val random = mockk<Random> { every { nextBoolean() } returns false }
            val viewModel = MainViewModel(random)

            val states = mutableListOf<MainUiState>()
            val job = launch { viewModel.state.toList(states) }
            advanceUntilIdle()

            viewModel.sayHello()
            advanceUntilIdle()
            job.cancel()

            assertFalse(states.last().isLoading)
            assertEquals("Goodbye, Android!", states.last().message)
        }
}
