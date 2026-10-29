package com.navorjames.compose.navigation

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform