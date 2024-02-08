package ru.aidar.lingolearn.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.aidar.lingolearn.utils.DarkThemeConfig

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean)

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)
}

class HUI() : UserDataRepository {
    override val userData: Flow<UserData> = flowOf(
        UserData(
            shouldHideOnboarding = true,
            userDynamicColor = true,
            darkThemeConfig = DarkThemeConfig.DARK
        )
    )

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
//        TODO("Not yet implemented")
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
//        TODO("Not yet implemented")
    }
}