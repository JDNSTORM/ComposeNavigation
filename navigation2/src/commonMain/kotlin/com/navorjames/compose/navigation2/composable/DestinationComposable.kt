package com.navorjames.compose.navigation2.composable

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.navorjames.compose.navigation2.ComponentEventHandler
import com.navorjames.compose.navigation2.NavigationEvent
import com.navorjames.compose.navigation2.component.NavigationDestination
import kotlinx.serialization.KSerializer
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * Represents a composable destination within a navigation graph.
 *
 * This class encapsulates the necessary information to define a screen (composable)
 * in a navigation graph, including its route (destination class), how to serialize
 * and deserialize its arguments, custom transitions, and deep links. It also handles
 * events triggered within the composable via an [ComponentEventHandler].
 *
 * @param E The type of events that can be emitted by the composable.
 * @param D The type of the destination, which must implement [NavigationDestination]. This type represents a screen or feature in the app.
 * @param GraphEvent The type of events that can be propagated to the navigation graph. These are events that affect the overall navigation flow, like opening a menu.
 * @property destinationClass The Kotlin class representing the destination. This is used as the route key. This class should implement [NavigationDestination].
 * @property serializer The [KSerializer] responsible for serializing and deserializing the destination's data. It handles converting the destination object to and from a data format.
 * @property typeMap A map of [KType] to [NavType] for specifying how to handle complex argument types. This allows the navigation system to understand how to pass complex objects as arguments.
 * @property deepLinks A list of [NavDeepLink] that can be used to navigate directly to this destination. These links are used to launch the app directly into a specific screen.
 * @property enterTransition An optional [EntryTransitionScopeBuilder] to define the enter transition for this destination. This defines how the screen animates when it's navigated to.
 */
data class DestinationComposable<E, D: NavigationDestination<E>, GraphEvent>(
    val destinationClass: KClass<D>,
    val serializer: KSerializer<D>,
    val typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
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
): NavigationComposable<E, D, GraphEvent> {

    fun NavGraphBuilder.composable(
        onNavigationEvent: (event: NavigationEvent) -> Unit,
        onGraphEvent: (GraphEvent) -> Unit
    ){
        composable(
            route = destinationClass,
            typeMap = typeMap,
            deepLinks = deepLinks,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
        ){ entry ->
            val destination = entry.toRoute<D>(destinationClass)
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