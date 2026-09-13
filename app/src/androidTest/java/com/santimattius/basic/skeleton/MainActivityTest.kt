package com.santimattius.basic.skeleton

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.waitUntilDoesNotExist
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun launchingTheAppResolvesTheRealHiltGraphAndRendersAMessage() {
        composeRule.waitUntilDoesNotExist(
            hasTestTag(TestTags.LOADING_INDICATOR),
            timeoutMillis = 5_000,
        )

        composeRule.onNodeWithContentDescription("Say Hello Android").assertExists()
    }

    @Test
    fun tappingTheFabTriggersANewMessage() {
        composeRule.waitUntilDoesNotExist(
            hasTestTag(TestTags.LOADING_INDICATOR),
            timeoutMillis = 5_000,
        )

        composeRule.onNodeWithContentDescription("Say Hello Android").performClick()

        composeRule.waitUntilDoesNotExist(
            hasTestTag(TestTags.LOADING_INDICATOR),
            timeoutMillis = 5_000,
        )
    }
}
