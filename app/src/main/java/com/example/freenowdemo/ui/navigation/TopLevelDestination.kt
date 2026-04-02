package com.example.freenowdemo.ui.navigation

import androidx.annotation.StringRes

data class TopLevelDestination(
    val destination: NavDestination,
    val icon: Int,
    val selectedIcon: Int,
    @param:StringRes val label: Int
)
