package com.navorjames.compose.navigation2.component

import com.navorjames.compose.navigation2.composable.DestinationComposable

/**
 * Represents a navigation graph within a larger navigation structure.
 *
 * A NavigationGraph is a collection of [DestinationComposable]s that define the navigable destinations
 * within a specific part of the application's UI. It acts as a self-contained unit of navigation,
 * with a defined [startDestination] that serves as the entry point to this subgraph.
 *
 * A navigation graph can be nested within another graph, enabling complex hierarchical navigation structures.
 *
 * @param GraphEvent The type of events that can be triggered from destinations within this graph.
 *                   These events are typically used for navigation actions or state changes.
 */
interface NavigationGraph<GraphEvent>: NavigationComponent<GraphEvent> {
    val startDestination: NavigationDestination<*>
    val composables: List<DestinationComposable<*, *, GraphEvent>>
}