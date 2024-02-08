import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.aidar.lingolearn.lingolearnapp.presentation.LingoLearnMainScreen
import ru.aidar.lingolearn.navigation.MainMenuRoute
import ru.aidar.lingolearn.navigation.NestedDictionaryRoute
import ru.aidar.lingolearn.navigation.NestedLearnRoute
import ru.aidar.lingolearn.navigation.NestedNotificationRoute
import ru.aidar.lingolearn.navigation.NestedProfileRoute
import ru.aidar.lingolearn.navigation.NestedTestOfDayRoute
import ru.aidar.lingolearn.navigation.NestedTranslationRoute
import ru.aidar.lingolearn.translator.presentaion.TranslatorScreen

//package ru.aidar.lingolearn.navigation
//
//import android.util.Log
//import androidx.navigation.NavController
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.NavOptions
//import androidx.navigation.compose.composable
//import androidx.navigation.navigation
//import ru.aidar.lingolearn.lingolearnapp.presentation.LingoLearnMainScreen
//import ru.aidar.lingolearn.translator.presentaion.TranslatorScreen
//
//fun NavController.navigateToLearn(navOptions: NavOptions) =
//    navigate(MainMenuRoute.NESTED_LEARN_ROUTE.route, navOptions)
//
//fun NavController.navigateToTestOfDay(navOptions: NavOptions) =
//    navigate(NestedTestOfDayRoute.TEST_BEGIN.route, navOptions)
//
//fun NavController.navigateToDictionary(navOptions: NavOptions) =
//    navigate(NestedDictionaryRoute.GENERAL_DICTIONARY.route, navOptions)
//
//fun NavController.navigateToProfile(navOptions: NavOptions) =
//    navigate(NestedTestOfDayRoute.TEST_BEGIN.route, navOptions)
//
//fun NavController.navigateToTranslator(navOptions: NavOptions) =
//    navigate(NestedTranslationRoute.TRANSLATOR.route, navOptions)
//
//fun NavController.navigateToNotification(navOptions: NavOptions? = null) =
//    navigate(NestedNotificationRoute.ALL_NOTIFICATION.route, navOptions)
//
//fun NavController.navigateToMenu(navOptions: NavOptions) =
//    navigate(LingoLearnAppEntry.route, navOptions)




fun NavGraphBuilder.testOfDayScreenGraph() {
    navigation(
        route = MainMenuRoute.NESTED_TEST_ROUTE.route,
        startDestination = NestedTestOfDayRoute.TEST_BEGIN.route
    ) {
        composable(route = NestedTestOfDayRoute.TEST_BEGIN.route) {
            //TranslatorScreen("Test")
        }
    }
}

fun NavGraphBuilder.dictionaryScreenGraph() {
    navigation(
        route = MainMenuRoute.NESTED_DICTIONARY_ROUTE.route,
        startDestination = NestedDictionaryRoute.GENERAL_DICTIONARY.route
    ) {
        composable(route = NestedDictionaryRoute.GENERAL_DICTIONARY.route) {
            //TranslatorScreen("Dictionary")
        }
    }
}

fun NavGraphBuilder.profileScreenGraph() {
    navigation(
        route = MainMenuRoute.NESTED_PROFILE_ROUTE.route,
        startDestination = NestedProfileRoute.USER_PROFILE.route
    ) {
        composable(route = NestedProfileRoute.USER_PROFILE.route) {
            //TranslatorScreen("Profile")
        }
    }
}

fun NavGraphBuilder.notificationScreenGraph(
) {
    navigation(
        route = MainMenuRoute.NESTED_NOTIFICATION_ROUTE.route,
        startDestination = NestedNotificationRoute.ALL_NOTIFICATION.route
    ) {
        composable(NestedNotificationRoute.ALL_NOTIFICATION.route) {
            //TranslatorScreen("Notification")
        }
    }
}


fun NavGraphBuilder.learnScreenGraph() {
    navigation(
        route = MainMenuRoute.NESTED_LEARN_ROUTE.route,
        startDestination = NestedLearnRoute.LANGUAGE_SELECTION.route
    ) {
        composable(route = NestedLearnRoute.LANGUAGE_SELECTION.route) {
            //TranslatorScreen("Learn")
        }
    }
}

fun NavGraphBuilder.mainScreenGraph(
    onRowClick: (String) -> Unit
) {
    composable(route = MainMenuRoute.MENU_SCREEN.route) {
        LingoLearnMainScreen(navigateTo = onRowClick)
    }

}
