package com.navorjames.compose.navigation2

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
expect fun PlatformDeepLinkIntegration(controller: NavController)