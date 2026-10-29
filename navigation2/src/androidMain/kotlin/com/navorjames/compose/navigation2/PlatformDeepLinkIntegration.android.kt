package com.navorjames.compose.navigation2

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.core.util.Consumer
import androidx.navigation.NavController

@Composable
actual fun PlatformDeepLinkIntegration(controller: NavController) {
    val activity = LocalActivity.current
    DisposableEffect(controller, activity) {
        val componentActivity = activity as? ComponentActivity
            ?: return@DisposableEffect onDispose {  }
        // Intent is already handled by NavController on launch
//        componentActivity.intent?.let { intent ->
//            controller.handleDeepLink(intent).also {
//                Logger.Debug.log("DeepLink ${intent.data} handled: $it")
//            }
//        }
        val consumer = Consumer<Intent> { intent ->
            controller.handleDeepLink(intent)
        }
        componentActivity.addOnNewIntentListener(consumer)
        onDispose {
            componentActivity.removeOnNewIntentListener(consumer)
        }
    }
}