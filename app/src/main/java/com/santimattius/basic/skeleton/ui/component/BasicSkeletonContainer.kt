package com.santimattius.basic.skeleton.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.santimattius.basic.skeleton.ui.theme.BasicSkeletonTheme

@Composable
@Suppress("MaxLineLength", "ForbiddenComment")
fun BasicSkeletonContainer(content: @Composable () -> Unit) {
    BasicSkeletonTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            //TODO: workaround ISSUE: https://issuetracker.google.com/issues/336842920
            CompositionLocalProvider(
                androidx.lifecycle.compose.LocalLifecycleOwner provides androidx.compose.ui.platform.LocalLifecycleOwner.current,
            ) {
                content()
            }
        }
    }
}