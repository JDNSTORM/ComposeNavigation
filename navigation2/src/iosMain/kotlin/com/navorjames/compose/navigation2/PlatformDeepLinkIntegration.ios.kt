package com.navorjames.compose.navigation2

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavUri
import androidx.navigation.navOptions
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@Composable
actual fun PlatformDeepLinkIntegration(controller: NavController) {
    LifecycleStartEffect(controller) {
        val job = lifecycleScope.launch {
            DeepLinkBridge.uriChannel.receiveAsFlow().collectLatest { uri ->
                runCatching {
                    controller.navigate(
                        deepLink = NavUri(uri),
                        navOptions = navOptions {
                            launchSingleTop = true
                        }
                    )
                }.onFailure {
                    it.printStackTrace()
                }
            }
        }
        onStopOrDispose {
            job.cancel()
        }
    }
}