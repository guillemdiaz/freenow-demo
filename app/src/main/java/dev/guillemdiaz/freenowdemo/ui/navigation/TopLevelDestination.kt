package dev.guillemdiaz.freenowdemo.ui.navigation

import androidx.annotation.StringRes

/**
 * Represents a top-level navigation destination shown in the bottom navigation bar.
 *
 * @param destination The nav destination this item navigates to
 * @param icon Drawable resource for the unselected state
 * @param selectedIcon Drawable resource for the selected state
 * @param label String resource for the accessibility label and nav bar text
 */
data class TopLevelDestination(
    val destination: NavDestination,
    val icon: Int,
    val selectedIcon: Int,
    @param:StringRes val label: Int
)
