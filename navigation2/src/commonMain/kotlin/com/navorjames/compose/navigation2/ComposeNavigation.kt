package com.navorjames.compose.navigation2

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.navorjames.compose.navigation2.NavigationEvent.Companion.onEvent
import com.navorjames.compose.navigation2.component.NavigationComponent
import com.navorjames.compose.navigation2.composable.DestinationComposable
import com.navorjames.compose.navigation2.composable.DestinationObjectComposable
import com.navorjames.compose.navigation2.composable.DialogComposable
import com.navorjames.compose.navigation2.composable.GraphComposable
import com.navorjames.compose.navigation2.composable.NavigationComposable

/**
 * `ComposeNavigation` is an abstract class that defines a structured way to manage navigation within
 * a Compose application. It provides a centralized configuration for destinations and their associated
 * composables, enabling a declarative approach to navigation definition.
 *
 * @param T The type of `NavigationComponent` that represents a destination or graph within the
 *          navigation structure. Must implement `NavigationComponent`.
 * @param NavEvent The type of events that can be triggered from within the composables of the
 *                 navigation structure. These events are typically used to signal changes or actions
 *                 that should be handled by the navigation framework or the application logic.
 *
 * **Key Features:**
 *
 * - **Start Destination:** Defines the initial screen or graph that the navigation should begin with.
 * - **Composable Definition:** Allows the specification of a list of composables (`NavigationComposable`)
 *   that represent the different destinations and their UI elements.
 * - **`setComponents` Function:** Provides a way to dynamically add the defined composables to a
 *   `NavGraphBuilder`, integrating them into the navigation graph.
 * - **`NavigationContent` Composable:** A ready-to-use composable that encapsulates a `NavHost`,
 *   automatically configuring it with the defined destinations and handling navigation events.
 *
 * **Usage:**
 *
 * 1. **Define Destinations:** Create classes that implement `NavigationComponent` to represent the
 *    different screens or graphs in your application.
 * 2. **Create `NavigationComposable`s:** Define instances of `NavigationComposable` (e.g.,
 *    `DestinationComposable`, `GraphComposable`, `DialogComposable`) that associate a `NavigationComponent`
 *    with its corresponding Composable function.
 * 3. **Extend `ComposeNavigation`:** Create a class that extends `ComposeNavigation`, providing the
 *    start destination and the list of composables.
 * 4. **Use `NavigationContent`:** Include the `NavigationContent` composable in your UI hierarchy,
 *    passing it a `NavHostController` instance and an event handler for `NavEvent`s.
 */
abstract class ComposeNavigation<T: NavigationComponent<*>, NavEvent> {
    abstract val startDestination: T
    abstract val composables: List<NavigationComposable<*, out T, NavEvent>>

    /**
     * Sets up the navigation components within a navigation graph.
     *
     * This function iterates through a list of composables and adds them to the navigation graph based on their type.
     * It handles different types of composables such as:
     * - [DestinationComposable]: A regular composable destination.
     * - [GraphComposable]: A nested navigation graph.
     * - [DestinationObjectComposable]: A composable representing a destination based on an object.
     * - [DialogComposable]: A dialog destination.
     *
     * Each composable is configured with the provided navigation event and graph event callbacks.
     *
     * @param onNavigationEvent A lambda function that handles navigation events triggered within the composables.
     *                          It receives a [NavigationEvent] object containing information about the navigation action.
     * @param onEvent A lambda function that handles graph-level events triggered within the composables.
     *                It receives a [NavEvent] object containing information about the graph event.
     */
    fun NavGraphBuilder.setComponents(
        onNavigationEvent: (NavigationEvent) -> Unit,
        onEvent: (NavEvent) -> Unit
    ){
        composables.forEach { composable ->
            with(composable) {
                when (this) {
                    is DestinationComposable -> composable(
                        onNavigationEvent = onNavigationEvent,
                        onGraphEvent = onEvent,
                    )

                    is GraphComposable -> setGraph(
                        onNavigationEvent = onNavigationEvent,
                        onEvent = onEvent
                    )

                    is DestinationObjectComposable -> composable(
                        onNavigationEvent = onNavigationEvent,
                        onGraphEvent = onEvent,
                    )

                    is DialogComposable -> dialog(
                        onNavigationEvent = onNavigationEvent,
                        onGraphEvent = onEvent
                    )
                }
            }
        }
    }

    /**
     *  Composes the navigation structure using [NavHost] within a [ComposeNavigation] context.
     *
     *  This function provides a simplified way to create a navigation graph using Jetpack Compose's
     *  [NavHost]. It automatically sets up the [NavHost] with the provided [NavHostController]
     *  and the `startDestination` defined in the parent `ComposeNavigation`.
     *
     *  For more granular control over the [NavHost] and its contents, consider using
     *  [setComponents] directly, especially if a [NavHost] already exists within your layout.
     *
     *  @param modifier The modifier to be applied to the [NavHost].
     *  @param controller The [NavHostController] responsible for managing navigation state and transitions.
     *  @param onEvent A callback invoked when a [NavEvent] occurs. This allows handling of custom navigation actions
     *                 or events not directly managed by the [NavHostController]. If not provided, a default
     *                 implementation will throw an error indicating an unhandled navigation event, including details
     *                 about the `ComposeNavigation` instance and the event.
     *                 The event will also be sent to the controller via the internal `onEvent` extension function.
     *
     *  @see NavHost
     *  @see setComponents
     *  @see NavHostController
     *  @see NavEvent
     */
    @Composable
    fun NavigationContent(
        modifier: Modifier = Modifier,
        controller: NavHostController,
        onEvent: (NavEvent) -> Unit = {
            error(
                buildString {
                    append("Unhandled Navigation: ")
                    append(this@ComposeNavigation)
                    appendLine()
                    append("Event: ")
                    append(it)
                }
            )
        }
    ) {
        NavHost(
            navController = controller,
            modifier = modifier,
            startDestination = startDestination,
            enterTransition = NavHostDefaults.enterTransition,
            exitTransition = NavHostDefaults.exitTransition
        ){
            setComponents(
                onNavigationEvent = {
                    controller.onEvent(it)
                },
                onEvent = onEvent
            )
        }
    }
}