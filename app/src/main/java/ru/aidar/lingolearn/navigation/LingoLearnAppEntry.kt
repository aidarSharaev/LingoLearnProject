package ru.aidar.lingolearn.navigation

interface BaseRouteEnum {
    val route: String
}

object LingoLearnAppEntry : BaseRouteEnum {
    override val route: String = "app"
}

enum class MainMenuRoute(override val route: String) : BaseRouteEnum {
    MENU_SCREEN(route = "app/menuScreen"),
    NESTED_LEARN_ROUTE(route = "app/learnGraph"),
    NESTED_TEST_ROUTE(route = "${LingoLearnAppEntry.route}/testGraph"),
    NESTED_TRANSLATION_ROUTE(route = "${LingoLearnAppEntry.route}/translationGraph"),
    NESTED_DICTIONARY_ROUTE(route = "${LingoLearnAppEntry.route}/dictionaryGraph"),
    NESTED_PROFILE_ROUTE(route = "${LingoLearnAppEntry.route}/profileGraph"),
    NESTED_NOTIFICATION_ROUTE(route = "${LingoLearnAppEntry.route}/notificationGraph"),
}

enum class NestedTranslationRoute(
    override val route: String
) : BaseRouteEnum {
    TRANSLATOR(route = "${MainMenuRoute.NESTED_TRANSLATION_ROUTE.route}/translatorScreen")
}

enum class NestedLearnRoute(
    override val route: String
) : BaseRouteEnum {
    LANGUAGE_SELECTION(route = "app/menuScreen/languageSelectionScreen")
}

enum class NestedProfileRoute(
    override val route: String
) : BaseRouteEnum {
    USER_PROFILE(route = "${MainMenuRoute.NESTED_PROFILE_ROUTE.route}/userProfileScreen")
}

enum class NestedTestOfDayRoute(override val route: String) : BaseRouteEnum {
    TEST_BEGIN(route = "${MainMenuRoute.NESTED_TEST_ROUTE.route}/testBeginScreen")
}

enum class NestedDictionaryRoute(override val route: String) : BaseRouteEnum {
    GENERAL_DICTIONARY(route = "${MainMenuRoute.NESTED_DICTIONARY_ROUTE.route}/userProfileScreen")
}

enum class NestedNotificationRoute(override val route: String) : BaseRouteEnum {
    ALL_NOTIFICATION(route = "${MainMenuRoute.NESTED_NOTIFICATION_ROUTE.route}/allNotificationScreen")
}
