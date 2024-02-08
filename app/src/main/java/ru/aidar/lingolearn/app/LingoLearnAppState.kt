package ru.aidar.lingolearn.app

import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.aidar.lingolearn.base.BaseNetworkMonitor


class LingoLearnAppState(
    val coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass,
    networkMonitor: BaseNetworkMonitor,
    val navController: NavHostController,
    // repository
) {
    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    fun navigationFromMenuTo(route: String) {
        navController.navigate(route)
    }

    fun onBackPressed() = navController.navigateUp()

    val currentDestination: String?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination?.route

}