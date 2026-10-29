package com.navorjames.compose.navigation2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.navigation.NavBackStackEntry
import kotlinx.coroutines.flow.collectLatest

@Composable
fun <T> SavedStatePropagationEffect(
    key: String?,
    initialValue: T,
    parentBackStackEntry: NavBackStackEntry,
    nestedBackStackEntry: NavBackStackEntry?,
    onPropagated: (T) -> Unit = {}
) {
    val sendPropagated by rememberUpdatedState(onPropagated)
    LaunchedEffect(key, parentBackStackEntry, nestedBackStackEntry) {
        key ?: return@LaunchedEffect
        nestedBackStackEntry ?: return@LaunchedEffect
        parentBackStackEntry
            .savedStateHandle
            .getStateFlow(key, initialValue)
            .collectLatest {
                nestedBackStackEntry.savedStateHandle[key] = it
                sendPropagated(it)
            }
    }
}