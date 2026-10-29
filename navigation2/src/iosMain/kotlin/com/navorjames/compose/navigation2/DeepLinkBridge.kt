package com.navorjames.compose.navigation2

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel

object DeepLinkBridge {
    internal val uriChannel = Channel<String>(onBufferOverflow = BufferOverflow.DROP_OLDEST)

    /**
     * ```swift
     * ContentView()
     *     .onOpenURL(perform: { url in
     *             DeepLinkBridge.shared.onNewUri(uri: url.absoluteString)
     *     })
     * ```
     */
    fun onNewUri(uri: String) {
        uriChannel.trySend(uri)
    }
}