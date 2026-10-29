package com.navorjames.compose.navigation2.component

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry

/**
 * [NavigationDestination] represents a screen or a logical section within the navigation graph.
 * It defines the UI content for a specific destination and handles events triggered within it.
 *
 * This interface extends [NavigationComponent], allowing for a modular and type-safe way to
 * define destinations in a navigation system.
 *
 * @param DestinationEvent The type of event that can be triggered by the destination. This allows
 *                         for communication from the destination back to a higher-level component
 *                         or the navigation host.
 */
interface NavigationDestination<DestinationEvent>: NavigationComponent<DestinationEvent> {
    @Composable
    fun AnimatedContentScope.Content(entry: NavBackStackEntry, onEvent: (DestinationEvent) -> Unit)
    operator fun ((Unit) -> Unit).invoke() = invoke(Unit)
}
