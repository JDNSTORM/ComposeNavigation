package com.navorjames.compose.navigation2

import com.navorjames.compose.navigation2.component.NavigationComponent

/**
 * `ComponentEventHandler` is a functional interface responsible for handling events
 * within a [NavigationComponent]. It provides a way to react to events emitted by a
 * specific component and trigger navigation or component-specific actions.
 *
 * This interface defines a single `invoke` method that takes the emitting component,
 * the event that occurred, and two callback functions: `onNavigationEvent` and `onEvent`.
 *
 *  **Key Responsibilities:**
 *
 *  - **Event Handling:**  Receives events of type `E` emitted by a [NavigationComponent] of type `D`.
 *  - **Navigation Control:** Uses the `onNavigationEvent` callback to trigger navigation actions within the system.
 *  - **Component-Specific Actions:** Uses the `onEvent` callback to emit custom events of type `ComponentEvent` to the caller, allowing for actions beyond navigation.
 *  - **Component Awareness:** Receives the emitting component `D` as a parameter, enabling access to the component's internal state and methods.
 *
 * **Parameters:**
 *
 *  - `E`: The type of event emitted by the component (e.g., ButtonClickEvent, ItemSelectedEvent).
 *  - `D`: The type of the [NavigationComponent] that is emitting the event. This component
 *        will be passed to the handler so it can access its state and functions. It must
 *        implement the [NavigationComponent] interface.
 *  - `ComponentEvent`: The type of custom event that can be emitted by this handler
 *        to the caller. This allows handlers to trigger actions outside of
 *        navigation.
 *
 * **`invoke` Function Parameters:**
 *
 *  - `component`: An instance of type `D`, representing the component that emitted the event.
 *  - `event`: An instance of type `E`, representing the event that occurred.
 *  - `onNavigationEvent`: A lambda function that takes a [NavigationEvent] as input and is used to trigger navigation.
 *  - `onEvent`: A lambda function that takes a `ComponentEvent` as input and is used to emit custom events to the caller.
 */
fun interface ComponentEventHandler<E, D: NavigationComponent<E>, ComponentEvent>{
    operator fun invoke(
        component: D,
        event: E,
        onNavigationEvent: (event: NavigationEvent) -> Unit,
        onEvent: (ComponentEvent) -> Unit
    )
}