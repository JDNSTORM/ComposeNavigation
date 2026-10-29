package com.navorjames.compose.navigation2

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.navorjames.compose.navigation2.component.NavigationComponent
import com.navorjames.compose.navigation2.component.NavigationDestination
import kotlin.reflect.KClass

sealed interface NavigationEvent {
    val fallbackEvent: NavigationEvent?
        get() = null

    data class Navigate<T: NavigationComponent<*>>(
        val destination: T,
        val builder: NavOptionsBuilder.() -> Unit = {}
    ): NavigationEvent

    data class NavigateUp(
        override val fallbackEvent: NavigationEvent? = null
    ): NavigationEvent

    sealed interface PopBackStack: NavigationEvent {
        data class PopRoute<T: Any>(
            val destinationClass: KClass<T>,
            val inclusive: Boolean,
            val saveState: Boolean = false,
            override val fallbackEvent: NavigationEvent? = null,
        ) : PopBackStack

        data class PopDestination<T : Any>(
            val destination: T,
            val inclusive: Boolean,
            val saveState: Boolean = false,
            override val fallbackEvent: NavigationEvent? = null,
        ) : PopBackStack
    }

    /**
     * [NavigationEvent] used for propagating Nested Events sequentially passed down through
     * [NavigationDestination] with [NavHost]
     *
     * [event] should be consumed by Primary [NavController] and
     * [nestedEvent] should be consumed by Nested [NavController] inside the Primary NavControllers NavHost
     */
    data class NestedNavigationEvent(
        val event: NavigationEvent,
        val nestedEvent: NavigationEvent
    ): NavigationEvent {
        init {
            require(event !is NestedNavigationEvent){
                "$event should not be a NestedNavigationEvent to avoid recursive calls"
            }
        }
    }

    companion object{
        /**
         * Processes navigation events within a [NavController].
         *
         * This function handles various types of navigation events, including:
         * - [Navigate]: Navigating to a specific destination.
         * - [NavigateUp]: Navigating up the back stack.
         * - [PopBackStack.PopDestination]: Popping back to a specific destination.
         * - [PopBackStack.PopRoute]: Popping back to a specific route.
         * - [NestedNavigationEvent]: Handling nested navigation events.
         *
         * It also includes robust error handling with fallback mechanisms. If a navigation
         * operation fails, it will attempt to execute a fallback event (if provided) before
         * throwing an exception or recording it.
         *
         * @param event The [NavigationEvent] to be processed.
         *
         * @throws Throwable if a navigation operation fails and no fallback event is provided,
         * or if the fallback event itself fails.
         *
         * @see NavigationEvent
         * @see Navigate
         * @see NavigateUp
         * @see PopBackStack
         * @see NestedNavigationEvent
         * @see Logger
         */
        fun NavController.onEvent(event: NavigationEvent){
            runCatching {
                when(event){
                    is Navigate<*> -> navigate(event.destination, event.builder)
                    is NavigateUp -> {
                        if (navigateUp()) return@runCatching
                        event.fallbackEvent?.let {
                            onEvent(it)
                        } ?: error("Failed to navigate up")
                    }
                    is PopBackStack.PopDestination<*> -> {
                        if (
                            popBackStack(
                                route = event.destination,
                                inclusive = event.inclusive,
                                saveState = event.saveState
                            )
                        ) return@runCatching
                        event.fallbackEvent?.let {
                            onEvent(it)
                        } ?: error("Navigate Failed")
                    }
                    is PopBackStack.PopRoute<*> -> {
                        if (
                            popBackStack(
                                route = event.destinationClass,
                                inclusive = event.inclusive,
                                saveState = event.saveState
                            )
                        ) return@runCatching
                        event.fallbackEvent?.let {
                            onEvent(it)
                        } ?: error("Navigate Failed")
                    }
                    is NestedNavigationEvent -> onEvent(event.event)
                }
            }.onFailure { throwable ->
                event.fallbackEvent?.let {
                    onEvent(it)
                }?: throw throwable
            }
        }
    }
}