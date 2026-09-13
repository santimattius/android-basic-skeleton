package com.santimattius.basic.skeleton

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.santimattius.basic.skeleton.ui.component.BasicSkeletonContainer

@PreviewTest
@Preview(name = "Message", showBackground = true)
@Composable
fun MainScreenMessageScreenshotTest() {
    BasicSkeletonContainer {
        MainScreen(
            state = MainUiState(isLoading = false, message = "Hello, Android!"),
            onMainAction = {},
        )
    }
}

@PreviewTest
@Preview(name = "Loading", showBackground = true)
@Composable
fun MainScreenLoadingScreenshotTest() {
    BasicSkeletonContainer {
        MainScreen(
            state = MainUiState(isLoading = true),
            onMainAction = {},
        )
    }
}

@PreviewTest
@Preview(name = "Message - large font", showBackground = true, fontScale = 1.5f)
@Composable
fun MainScreenMessageLargeFontScreenshotTest() {
    BasicSkeletonContainer {
        MainScreen(
            state = MainUiState(isLoading = false, message = "Hello, Android!"),
            onMainAction = {},
        )
    }
}
