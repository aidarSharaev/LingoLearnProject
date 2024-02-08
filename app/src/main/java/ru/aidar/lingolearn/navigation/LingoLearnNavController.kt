package ru.aidar.lingolearn.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import ru.aidar.lingolearn.app.LingoLearnAppState
import ru.aidar.lingolearn.base.BaseNetworkMonitor

@Composable
fun rememberLingoLearnAppState(
    windowSizeClass: WindowSizeClass,
    networkMonitor: BaseNetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): LingoLearnAppState {
    return remember(
        navController,
        coroutineScope,
        windowSizeClass,
        networkMonitor,
    ) {
        LingoLearnAppState(
            coroutineScope,
            windowSizeClass,
            networkMonitor,
            navController,
        )
    }
}

//@Composable
//fun rememberLingoLearnNavController(
//    navController: NavHostController = rememberNavController()
//): LingoLearnNavController =
//    remember(navController) {
//        LingoLearnNavController(navController = navController)
//    }
//
//class LingoLearnNavController(
//    private val navController: NavHostController
//) {
//
//    fun navController() = this.navController
//
//    fun onBackPressed() {
//        navController.navigateUp()
//    }
//
//    /*fun navigateToAnotherDestination(route: String) {
//        val topLevelNavOptions = navOptions {
//            launchSingleTop = true
//            restoreState = true
//            popUpTo(findStartDestination(navController.graph).id) {
//                saveState = true
//            }
//        }
//
//        when(route) {
//            MainMenuRoute.NESTED_LEARN_ROUTE.route -> navController.navigateToLearn(topLevelNavOptions)
//            MainMenuRoute.NESTED_TEST_ROUTE.route -> navController.navigateToTestOfDay(
//                topLevelNavOptions
//            )
//
//            MainMenuRoute.NESTED_TRANSLATION_ROUTE.route -> navController.navigateToTranslator(
//                topLevelNavOptions
//            )
//
//            MainMenuRoute.NESTED_DICTIONARY_ROUTE.route -> navController.navigateToDictionary(
//                topLevelNavOptions
//            )
//
//            MainMenuRoute.NESTED_PROFILE_ROUTE.route -> navController.navigateToProfile(topLevelNavOptions)
//        }
//    }*/
//
//    fun navigateToAnotherDestination(route: String) = navController.navigate(route = route) {
//        popUpTo(navController.graph.findStartDestination().id) {
//            inclusive = true
//        }
//    }
//
//    fun navigateToNotification() = navController.navigateToNotification()
//
//    fun navigateTo(route: String) =
//        navController.navigate(route = route)
//
//    val currentDestination: NavDestination?
//        @Composable get() = navController.currentBackStackEntryAsState().value?.destination
//
//    private val NavGraph.startDestination: NavDestination?
//        get() = findNode(startDestinationId)
//
//    private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
//        return if(graph is NavGraph) {
//            findStartDestination(graph.startDestination!!)
//        } else {
//            graph
//        }
//    }
//
//}