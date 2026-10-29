package com.navorjames.compose.navigation2.composable

import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.navorjames.compose.navigation2.ComponentEventHandler
import com.navorjames.compose.navigation2.NavigationEvent
import com.navorjames.compose.navigation2.component.NavigationDialog
import kotlinx.serialization.KSerializer
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * [DialogComposable] is a data class that encapsulates the configuration and behavior
 * of a composable dialog within a navigation graph. It provides a way to define
 * a dialog destination, its associated serializer, type mapping for arguments,
 * deep links, and custom dialog properties. It also allows for handling events
 * triggered within the dialog.
 *
 * @param E The type of event emitted by the composable within the dialog.
 * @param D The type of the dialog destination, which must implement [NavigationDialog].
 * @param GraphEvent The type of event emitted to the navigation graph from this dialog.
 * @property destinationClass The KClass of the dialog destination ([D]).
 * @property serializer The [KSerializer] used to serialize and deserialize the dialog's destination data.
 * @property typeMap A map of [KType] to [NavType] that defines the type mapping for arguments
 *                   passed to the dialog. This is used when navigating to the dialog with arguments.
 * @property deepLinks A list of [NavDeepLink]s associated with the dialog. This allows the dialog
 *                     to be launched via deep links.
 * @property properties The [DialogProperties] to customize the appearance and behavior of the dialog,
 *                    such as its dimensions, dismiss behavior, and secure policy.
 * @property eventHandler The [ComponentEventHandler] that handles events ([E]) emitted from the dialog.
 *                       The default implementation will throw an error if an event is received, indicating
 *                       that it should be overridden with custom logic.
 */
data class DialogComposable<E, D: NavigationDialog<E>, GraphEvent>(
    val destinationClass: KClass<D>,
    val serializer: KSerializer<D>,
    val typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    val deepLinks: List<NavDeepLink> = emptyList(),
    val properties: DialogProperties = DialogProperties(),
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

    fun NavGraphBuilder.dialog(
        onNavigationEvent: (event: NavigationEvent) -> Unit,
        onGraphEvent: (GraphEvent) -> Unit
    ){
        dialog(
            route = destinationClass,
            typeMap = typeMap,
            dialogProperties = properties,
            deepLinks = deepLinks,
        ){ entry ->
            val destination = entry.toRoute<D>(destinationClass)
            destination.Content(
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