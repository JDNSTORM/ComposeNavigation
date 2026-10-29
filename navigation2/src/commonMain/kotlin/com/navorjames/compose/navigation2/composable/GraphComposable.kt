package com.navorjames.compose.navigation2.composable

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.navorjames.compose.navigation2.ComponentEventHandler
import com.navorjames.compose.navigation2.NavigationEvent
import com.navorjames.compose.navigation2.component.NavigationGraph

/**
 * `GraphComposable` is a specialized [Object] that represents a group of composables
 * defined within a [NavigationGraph]. It handles the setup and management of a navigation graph's composable
 * destinations using Jetpack Compose Navigation.
 *
 * This class acts as a container for a [NavigationGraph], allowing you to define a set of composable destinations
 * that are logically grouped together. It leverages the Jetpack Compose Navigation library to manage the
 * navigation between these destinations.
 *
 * Key Features:
 * - **Graph Representation:** Represents a single [NavigationGraph] within a larger navigation structure.
 * - **Composable Management:**  Manages the creation and setup of composable destinations defined in the associated [NavigationGraph].
 * - **Event Handling:** Provides a mechanism for handling events that occur within the graph through a [ComponentEventHandler].
 * - **Navigation Integration:** Integrates with Jetpack Compose Navigation's [NavGraphBuilder] to define navigation routes and transitions.
 * - **Start Destination:** Uses the [NavigationGraph.startDestination] property to determine the initial screen displayed when navigating to this graph.
 * - **Route Definition:** Uses the [NavigationGraph] class name as the route for this graph.
 *
 * @param E The type of event that can be triggered within the [NavigationGraph].
 * @param G The type of the [NavigationGraph] this composable represents. Must extend [NavigationGraph].
 * @param NavEvent The type of event that is propagated to another [NavigationGraph] or `ComposeNavigation`.
 * @property component The [NavigationGraph] that this `GraphComposable` represents. This graph defines the
 *                  structure and destinations within this composable group.
 * @property eventHandler The [ComponentEventHandler] that handles events within the graph.
 *                  It receives events of type `E` that are triggered within the graph's composables.
 *                  By default, it throws an error when an event is not handled, indicating a need
 *                  for custom event handling logic.
 */
data class GraphComposable<E, G: NavigationGraph<E>, NavEvent>(
    override val component: G,
    override val eventHandler: ComponentEventHandler<E, G, NavEvent> = ComponentEventHandler { graph, event, _, _ ->
        error(
            buildString {
                append("Unhandled Graph: ")
                append(graph)
                appendLine()
                append("Event: ")
                append(event)
            }
        )
    }
): NavigationComposable.Object<E, G, NavEvent> {

    fun NavGraphBuilder.setGraph(
        onNavigationEvent: (NavigationEvent) -> Unit,
        onEvent: (NavEvent) -> Unit
    ){
        navigation(
            startDestination = component.startDestination,
            route = component::class
        ){
            component.composables.forEach { destinationComposable ->
                destinationComposable.run {
                    composable(
                        onNavigationEvent = onNavigationEvent,
                        onGraphEvent = {
                            eventHandler(
                                component = component,
                                event = it,
                                onNavigationEvent = onNavigationEvent,
                                onEvent = onEvent
                            )
                        }
                    )
                }
            }
        }
    }
}