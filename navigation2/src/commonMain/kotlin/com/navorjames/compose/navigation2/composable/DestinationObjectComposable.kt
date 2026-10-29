package com.navorjames.compose.navigation2.composable

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.navorjames.compose.navigation2.ComponentEventHandler
import com.navorjames.compose.navigation2.NavigationEvent
import com.navorjames.compose.navigation2.component.NavigationDestination
import kotlinx.serialization.KSerializer

/**
 * Represents a composable destination within a navigation graph, where the destination is a specific object.
 *
 * This class defines a navigation destination that corresponds to a concrete instance of a [NavigationDestination]
 * rather than a route string. It encapsulates the necessary information to render a composable for that specific
 * destination, handle navigation events, and configure transitions.
 *
 * @param E The type of navigation event that can be triggered from this destination.
 * @param D The type of the navigation destination, which must implement [NavigationDestination].
 * @param GraphEvent The type of event that can be emitted to the parent navigation graph.
 * @property component The specific [NavigationDestination] instance that this composable represents.
 * @property serializer The [KSerializer] used to (de)serialize the destination.
 * @property deepLinks A list of [NavDeepLink] that can be used to navigate directly to this destination. Defaults to an empty list.
 * @property enterTransition A function that defines the enter transition for this composable. If null, no specific transition is applied.
 * @property exitTransition A function that defines the exit transition for this composable. If null, no specific transition is applied.
 * @property popEnterTransition A function that defines the enter transition when returning to this composable from a back stack.
 */
data class DestinationObjectComposable<E, D: NavigationDestination<E>, GraphEvent>(
    override val component: D,
    val serializer: KSerializer<D>,
    val deepLinks: List<NavDeepLink> = emptyList(),
    val enterTransition: EntryTransitionScopeBuilder<EnterTransition>? = null,
    val exitTransition: EntryTransitionScopeBuilder<ExitTransition>? = null,
    val popEnterTransition: EntryTransitionScopeBuilder<EnterTransition>? = enterTransition,
    val popExitTransition: EntryTransitionScopeBuilder<ExitTransition>? = exitTransition,
    override val eventHandler: ComponentEventHandler<E, D, GraphEvent> = ComponentEventHandler { destination, event, _, _ ->
        error(
            buildString {
                append("Unhandled Destination: ")
                append(destination)
                appendLine()
                append("Event: ")
                append(event)
            }
        )
    }
): NavigationComposable.Object<E, D, GraphEvent> {

    fun NavGraphBuilder.composable(
        onNavigationEvent: (event: NavigationEvent) -> Unit,
        onGraphEvent: (GraphEvent) -> Unit
    ) {
        composable(
            route = component::class,
            typeMap = emptyMap(),
            deepLinks = deepLinks,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
        ) { entry ->
            val destination = entry.toRoute<D>(component::class)
            destination.run {
                Content(
                    entry = entry,
                    onEvent = {
                        eventHandler(
                            component = destination,
                            event = it,
                            onNavigationEvent = onNavigationEvent,
                            onEvent = onGraphEvent
                        )
                    }
                )
            }
        }
    }
}