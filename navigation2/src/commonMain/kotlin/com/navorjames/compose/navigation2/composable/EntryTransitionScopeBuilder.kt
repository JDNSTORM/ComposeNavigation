package com.navorjames.compose.navigation2.composable

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavBackStackEntry

typealias EntryTransitionScopeBuilder<T> = AnimatedContentTransitionScope<NavBackStackEntry>.() -> T

