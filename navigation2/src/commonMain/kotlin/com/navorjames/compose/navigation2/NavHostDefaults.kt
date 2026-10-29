package com.navorjames.compose.navigation2

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import com.navorjames.compose.navigation2.composable.EntryTransitionScopeBuilder

object NavHostDefaults {
    val enterTransition: EntryTransitionScopeBuilder<EnterTransition> = {
        fadeIn(tween(700))
    }
    val exitTransition: EntryTransitionScopeBuilder<ExitTransition> = {
        fadeOut(tween(700))
    }
}