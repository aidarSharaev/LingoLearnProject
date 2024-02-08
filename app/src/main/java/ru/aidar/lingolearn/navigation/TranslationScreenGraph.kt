package ru.aidar.lingolearn.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.aidar.lingolearn.translator.presentaion.TranslatorScreen
import ru.aidar.lingolearn.translator.presentaion.TranslatorViewModel

fun NavGraphBuilder.translationScreenGraph(navController: NavHostController) {
    navigation(
        route = MainMenuRoute.NESTED_TRANSLATION_ROUTE.route,
        startDestination = NestedTranslationRoute.TRANSLATOR.route
    ) {
        composable(route = NestedTranslationRoute.TRANSLATOR.route) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(MainMenuRoute.NESTED_TRANSLATION_ROUTE.route)
            }
            val viewModel = hiltViewModel<TranslatorViewModel>(parentEntry)
            TranslatorScreen(viewModel = viewModel)
        }
    }
}
