package com.santimattius.basic.skeleton.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppBar(
    title: String = "",
    navIcon: AppBarIconModel? = null,
    actions: @Composable RowScope.() -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.primary,
    titleContentColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = rememberNavIcon(navIcon),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            scrolledContainerColor = Color.Unspecified,
            navigationIconContentColor = titleContentColor,
            titleContentColor = titleContentColor,
            actionIconContentColor = titleContentColor
        ),
        actions = actions
    )
}

@Composable
private fun rememberNavIcon(navIcon: AppBarIconModel?): @Composable () -> Unit = remember(navIcon) {
    if (navIcon == null) {{}} else {{ AppBarIcon(navIcon) }}
}

@Composable
internal fun AppBarIcon(navIcon: AppBarIconModel) {
    IconButton(onClick = { navIcon.action() }) {
        Icon(imageVector = navIcon.icon, contentDescription = navIcon.contentDescription)
    }
}

internal data class AppBarIconModel(
    val icon: ImageVector,
    val contentDescription: String,
    val action: () -> Unit,
)