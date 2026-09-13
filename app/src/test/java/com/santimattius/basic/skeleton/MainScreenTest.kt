package com.santimattius.basic.skeleton

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.santimattius.basic.skeleton.ui.component.BasicSkeletonContainer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class MainScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `loading state shows the loading indicator`() {
        composeTestRule.setContent {
            BasicSkeletonContainer {
                MainScreen(state = MainUiState(isLoading = true), onMainAction = {})
            }
        }

        composeTestRule.onNodeWithTag(TestTags.LOADING_INDICATOR).assertExists()
    }

    @Test
    fun `loaded state shows the message instead of the loading indicator`() {
        composeTestRule.setContent {
            BasicSkeletonContainer {
                MainScreen(
                    state = MainUiState(isLoading = false, message = "Hello, Android!"),
                    onMainAction = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Hello, Android!").assertExists()
        composeTestRule.onNodeWithTag(TestTags.LOADING_INDICATOR).assertDoesNotExist()
    }

    @Test
    fun `tapping the fab invokes onMainAction`() {
        var actionInvoked = false

        composeTestRule.setContent {
            BasicSkeletonContainer {
                MainScreen(
                    state = MainUiState(isLoading = false, message = "Hello, Android!"),
                    onMainAction = { actionInvoked = true },
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Say Hello Android").performClick()

        assert(actionInvoked) { "Expected onMainAction to be invoked after tapping the FAB" }
    }
}
