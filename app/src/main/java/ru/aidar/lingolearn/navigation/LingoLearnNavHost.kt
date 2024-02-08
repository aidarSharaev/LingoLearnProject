package ru.aidar.lingolearn.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import dictionaryScreenGraph
import learnScreenGraph
import mainScreenGraph
import notificationScreenGraph
import profileScreenGraph
import ru.aidar.lingolearn.app.LingoLearnAppState
import testOfDayScreenGraph

@Composable
fun LingoLearnNavHost(
    llAppState: LingoLearnAppState,
    startDestination: String = MainMenuRoute.MENU_SCREEN.route,
) {

    val navController = llAppState.navController

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        mainScreenGraph(
            onRowClick = llAppState::navigationFromMenuTo
        )
        learnScreenGraph()
        testOfDayScreenGraph()
        translationScreenGraph(
            navController = navController
        )
        dictionaryScreenGraph()
        profileScreenGraph()
        notificationScreenGraph()
    }
}