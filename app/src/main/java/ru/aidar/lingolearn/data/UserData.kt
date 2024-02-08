package ru.aidar.lingolearn.data

import ru.aidar.lingolearn.utils.DarkThemeConfig

data class UserData(
    val shouldHideOnboarding: Boolean,
    val userDynamicColor: Boolean,
    val darkThemeConfig: DarkThemeConfig,
)
