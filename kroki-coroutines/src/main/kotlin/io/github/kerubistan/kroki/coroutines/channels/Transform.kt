package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

internal inline fun <I, O> CoroutineScope.transform(
    inputChannel: Channel<I>,
    crossinline transformation: (I) -> O
): Channel<O> {
    val outChannel = Channel<O>(capacity = 64)
    launch {
        for (message in inputChannel) {
            outChannel.send(transformation(message))
        }
        outChannel.close()
    }
    return outChannel
}

internal inline fun <I, O> CoroutineScope.transformNotNull(
    inputChannel: Channel<I>,
    crossinline transformation: (I) -> O?
): Channel<O> {
    val outChannel = Channel<O>(capacity = 64)
    launch {
        for (message in inputChannel) {
            transformation(message)?.let { outChannel.send(it) }
        }
        outChannel.close()
    }
    return outChannel
}