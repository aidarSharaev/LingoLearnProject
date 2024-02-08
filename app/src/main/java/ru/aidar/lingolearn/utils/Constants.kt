package ru.aidar.lingolearn.utils

data class AvailableLanguage(
    val language: String,
    val alphaCode: String
)

object Constants {

    private const val NUMBER_OF_NAVIGATION_IN_COLUMN = 5

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

}