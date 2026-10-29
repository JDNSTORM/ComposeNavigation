package com.navorjames.compose.navigation2

import androidx.navigation.NavController
import com.navorjames.compose.navigation2.component.NavigationComponent

/**
 * Navigates to a destination using a tab-like pattern, preserving the back stack of each tab.
 *
 * This extension function is designed for top-level navigation, such as a bottom navigation bar.
 * It ensures that when switching between tabs, the user's navigation history within each tab is
 * preserved. When navigating to a new tab, the back stack is popped up to the root of the
 * navigation graph, its state is saved, and the state of the destination tab is restored if it
 * was previously visited. This creates an intuitive user experience where the back button
 * behaves as expected within each tab, and pressing it on the root screen of a tab typically
 * exits the app.
 *
 * Example usage in a bottom navigation bar:
 * ```
 * onClick = {
 *     navController.tabNavigate(
 *         component = Screen.Profile,
 *         popUpComponent = Screen.Home // The start destination of your graph
 *     )
 * }
 * ```
 *
 * @param component The destination [NavigationComponent] to navigate to.
 * @param popUpComponent The root [NavigationComponent] of the navigation graph to pop up to.
 *                       This is usually the start destination.
 * @param popUpSaveState If `true`, the state of the popped back stack will be saved. Defaults to `true`.
 *
 * @see [navigate]
 * @see [androidx.navigation.NavOptionsBuilder.popUpTo]
 */
fun NavController.tabNavigate(
    component: NavigationComponent<*>,
    popUpComponent: NavigationComponent<*>,
    popUpSaveState: Boolean = true
) {
    if (component == popUpComponent) return
    navigate(component) {
        popUpTo(popUpComponent) {
            inclusive = true
            saveState = popUpSaveState
        }
        restoreState = true
        launchSingleTop = true
    }
}