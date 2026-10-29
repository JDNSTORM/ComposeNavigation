package com.navorjames.compose.navigation2.composable

import com.navorjames.compose.navigation2.ComponentEventHandler
import com.navorjames.compose.navigation2.component.NavigationComponent

/**
 * Represents a composable element within a navigation graph.
 * This interface defines the structure for components that can be navigated to
 * and interacted with within a navigation hierarchy.
 *
 * @param E The type of events that can be emitted by the navigation component.
 * @param C The type of the [NavigationComponent] this composable is associated with. Must extend [NavigationComponent]
 * @param UpperEvent The type of events that this composable can propagate to its parent/upper level in the navigation graph.
 *                   These events can either be propagated to `ComposeNavigation` or to a `GraphComposable`.
 */
sealed interface NavigationComposable<E, C: NavigationComponent<E>, UpperEvent> {
    val eventHandler: ComponentEventHandler<E, C, UpperEvent>

    sealed interface Object<E, C: NavigationComponent<E>, GraphEvent>:
        NavigationComposable<E, C, GraphEvent> {
        val component: C
    }
}