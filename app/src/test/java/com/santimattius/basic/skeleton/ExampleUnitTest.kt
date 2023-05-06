package com.santimattius.basic.skeleton

import com.santimattius.basic.skeleton.tools.helpers.JsonLoader
import com.santimattius.basic.skeleton.tools.rules.MainCoroutinesTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ExampleUnitTest {

    @get:Rule
    val mainCoroutinesTestRule = MainCoroutinesTestRule()

    private val jsonLoader = JsonLoader()

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun load_Json_file() {
        val jsonContent = jsonLoader.load("hello.json")
        assertThat(jsonContent, containsString("Android"))
    }
}