package com.navorjames.compose.navigation2.component

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry

/**
 * `NavigationDialog` represents a dialog-based navigation component in a Compose application.
 *
 * This interface extends `NavigationComponent` and provides a way to display a dialog
 * associated with a specific navigation destination.  It handles the presentation of the
 * dialog's content based on the current navigation back stack entry.
 *
 * **Key Responsibilities:**
 *
 * 1.  **Content Display:**  Defines how the dialog's UI content should be rendered using
 *     the [Content] composable function.
 * 2.  **Navigation Event Handling:** Provides a callback mechanism (`onEvent`) to send
 *     navigation-related events from the dialog back to the navigation system.
 * 3. **Backstack Entry Association**: Links the dialog's UI to the relevant `NavBackStackEntry`, allowing the dialog
 * to access route specific information.
 * 4. **Lambda Short Hand:** Provides a shorthand way to invoke lambda functions with a unit parameter using the operator invoke function.
 *
 * **Generic Parameter:**
 *
 * *   `DestinationEvent`: The type of event that can be triggered by the dialog to
 *     communicate with the navigation system (e.g., navigating to a new route, closing
 *     the dialog).
 *
 * **Usage:**
 *
 * Implement this interface to create a custom dialog component for a specific
 * navigation destination. The `Content` function will be called whenever the
 * associated route is navigated to, allowing you to render the dialog's UI.
 * The `onEvent` lambda in the content function must be called in order to inform the navigation component to
 * perform navigation actions.
 *
 * **Example:**
 *
 * ```kotlin
 * data class MyDialogEvent(val action: String)
 *
 * class MyNavigationDialog : NavigationDialog<MyDialogEvent> {
 *     override val route: String = "my_dialog_route"
 *     @Composable
 *     override fun Content(entry: NavBackStackEntry, onEvent: (MyDialogEvent) -> Unit) {
 *         AlertDialog(
 *             onDismissRequest = { onEvent(MyDialogEvent("dismissed")) },
 *             confirmButton = {
 *                 TextButton(
 *                     onClick = { onEvent(MyDialogEvent("confirmed")) }
 *                 ) { Text("Confirm") }
 *             },
 *             dismissButton = {
 *                 TextButton(
 *                     onClick = { onEvent(MyDialogEvent("dismissed")) }
 *                 ) { Text("Dismiss") }
 *             },
 *             title = { Text("My Dialog") }
 *         )
 *     }
 * }
 * ```
 */
interface NavigationDialog<DestinationEvent>: NavigationComponent<DestinationEvent> {
    @Composable
    fun Content(entry: NavBackStackEntry, onEvent: (DestinationEvent) -> Unit)
    operator fun ((Unit) -> Unit).invoke() = invoke(Unit)
}
