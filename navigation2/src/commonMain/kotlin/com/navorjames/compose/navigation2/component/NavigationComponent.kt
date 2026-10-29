package com.navorjames.compose.navigation2.component

/**
 * `NavigationComponent` is a sealed interface representing a component that participates in navigation.
 *
 * It defines a contract for components that can emit navigation-related events, allowing a navigation
 * controller or similar entity to react to these events and manage the application's navigation flow.
 *
 * @param ComponentEvent The type of events that this component can emit, representing actions that
 *                       trigger navigation changes. This is typically a sealed class or sealed interface
 *                       defining the possible navigation events.
 *
 * Example:
 * ```kotlin
 * sealed class MyNavigationEvent {
 *     data object GoToScreenA : MyNavigationEvent()
 *     data object GoToScreenB : MyNavigationEvent()
 *     data class GoToScreenC(val data: String) : MyNavigationEvent()
 *     data object GoBack : MyNavigationEvent()
 * }
 *
 * class MyScreenA : NavigationComponent<MyNavigationEvent> {
 *   // ...
 *   fun onButtonClicked() {
 *       // Trigger navigation to another screen.
 *       val event: MyNavigationEvent = MyNavigationEvent.GoToScreenB
 *       // ... emit event
 *   }
 * }
 * ```
 */
sealed interface NavigationComponent<ComponentEvent>