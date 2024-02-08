package ru.aidar.lingolearn.app

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.aidar.lingolearn.R
import ru.aidar.lingolearn.base.BaseNetworkMonitor
import ru.aidar.lingolearn.component.LlTopAppBar
import ru.aidar.lingolearn.navigation.LingoLearnNavHost
import ru.aidar.lingolearn.navigation.MainMenuRoute
import ru.aidar.lingolearn.navigation.NestedDictionaryRoute
import ru.aidar.lingolearn.navigation.NestedProfileRoute
import ru.aidar.lingolearn.navigation.rememberLingoLearnAppState

@Composable
fun LingoLearnApp(
    windowSizeClass: WindowSizeClass,
    networkMonitor: BaseNetworkMonitor,
    llAppState: LingoLearnAppState = rememberLingoLearnAppState(
        windowSizeClass = windowSizeClass,
        networkMonitor = networkMonitor,
    )
) {

    val snackbarHostState = remember { SnackbarHostState() }

    val isOffline by llAppState.isOffline.collectAsStateWithLifecycle()

    val notConnectedMessage = stringResource(R.string.not_connected)
    LaunchedEffect(isOffline) {
        if(isOffline) {
            snackbarHostState.showSnackbar(
                message = notConnectedMessage,
                duration = SnackbarDuration.Indefinite,
            )
        }
    }

    val destination = llAppState.currentDestination

    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                //.navigationBarsPadding()
                .fillMaxSize()
        ) {
            LlTopAppBar(
                onNavIconClick = llAppState::onBackPressed,
                isNavIconVisible = destination != MainMenuRoute.MENU_SCREEN.route,
                actionIcon =
                when(destination) {
                    MainMenuRoute.MENU_SCREEN.route -> Icons.Outlined.Notifications
                    NestedDictionaryRoute.GENERAL_DICTIONARY.route -> Icons.Default.Search
                    NestedProfileRoute.USER_PROFILE.route -> Icons.Outlined.Settings
                    else -> null
                },
                onActionIconClick = {}
            )
            LingoLearnNavHost(
                llAppState = llAppState,
            )
        }
    }
}