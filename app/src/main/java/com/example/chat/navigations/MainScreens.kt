package com.example.chat.navigations

sealed class MainScreens(val route: String) {
    object Main: MainScreens("main_main_screen")
    object Profile: MainScreens("main_profile_screen")
    object ProfileContent: MainScreens("main_profile_content_screen")
    object ProfileChangeData: MainScreens("main_profile_change_data")
    object ProfileChangeStyle: MainScreens("main_profile_change_style")

    fun withUid(uid: String): String {
        return buildString {
            append(route)
            append("/$uid")
        }
    }
}