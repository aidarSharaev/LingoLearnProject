package ru.aidar.lingolearn.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ru.aidar.lingolearn.R
import ru.aidar.lingolearn.navigation.MainMenuRoute

data class AvailableLanguage(
    val language: String,
    val alphaCode: String
) {
    override fun equals(other: Any?): Boolean {
        return (other is AvailableLanguage && this.language == other.language)
    }

    override fun hashCode(): Int {
        var result = language.hashCode()
        result = 31 * result + alphaCode.hashCode()
        return result
    }
}

data class RouteStringDraw(
    val route: String,
    @StringRes
    val stringRes: Int,
    @DrawableRes
    val drawRes: Int,
)

object Constants {

    const val ENGLISH_IS_ALWAYS_DOWNLOADED = 1


    val LANGUAGE_MAP = mapOf(
        "zh" to "CHINESE",
        "en" to "ENGLISH",
        "fr" to "FRENCH",
        "de" to "GERMAN",
        "he" to "HEBREW",
        "hi" to "HINDI",
        "ja" to "JAPANESE",
        "ru" to "RUSSIAN",
        "es" to "SPANISH",
    )


    const val NUMBER_OF_NAVIGATION_IN_COLUMN = 5

    const val ROW_WEIGHT = 1f / NUMBER_OF_NAVIGATION_IN_COLUMN

    val AVAILABLE_TRANSLATOR_LANGUAGES = listOf(
        AvailableLanguage("CHINESE", "zh"),
        AvailableLanguage("ENGLISH", "en"),
        AvailableLanguage("FRENCH", "fr"),
        AvailableLanguage("GERMAN", "de"),
        AvailableLanguage("HEBREW", "he"),
        AvailableLanguage("HINDI", "hi"),
        AvailableLanguage("JAPANESE", "ja"),
        AvailableLanguage("RUSSIAN", "ru"),
        AvailableLanguage("SPANISH", "es")
    )

    val ROUTE_STRING_DRAW_RES_MAIN_MENU = listOf(
        RouteStringDraw(
            route = MainMenuRoute.NESTED_LEARN_ROUTE.route,
            stringRes = R.string.study,
            drawRes = R.drawable.nav_learn
        ),
        RouteStringDraw(
            route = MainMenuRoute.NESTED_TEST_ROUTE.route,
            stringRes = R.string.test_of_the_day,
            drawRes = R.drawable.nav_test
        ),
        RouteStringDraw(
            route = MainMenuRoute.NESTED_DICTIONARY_ROUTE.route,
            stringRes = R.string.dictionary,
            drawRes = R.drawable.nav_dictionary
        ),

        RouteStringDraw(
            route = MainMenuRoute.NESTED_TRANSLATION_ROUTE.route,
            stringRes = R.string.translator,
            drawRes = R.drawable.nav_translate
        ),
        RouteStringDraw(
            route = MainMenuRoute.NESTED_PROFILE_ROUTE.route,
            stringRes = R.string.profile,
            drawRes = R.drawable.nav_profile
        ),
    )


    val LL_TRANSPARENT_TEXT_FIELD_COLOR: TextFieldColors
        @Composable get() = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )

    sealed interface LanguagesDownloadState {
        data object Creating : LanguagesDownloadState
        data object AllRight : LanguagesDownloadState
        data object OnlyOne : LanguagesDownloadState

    }

}