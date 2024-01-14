package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

inline fun <I, O> CoroutineScope.transform(
    inputChannel: ReceiveChannel<I>,
    crossinline transformation: (I) -> O
): ReceiveChannel<O> {
    val outChannel = Channel<O>(capacity = 64)
    launch {
        for (message in inputChannel) {
            outChannel.send(transformation(message))
        }
        outChannel.close()
    }
    return outChannel
}

inline fun <I, O> CoroutineScope.transformNotNull(
    inputChannel: ReceiveChannel<I>,
    crossinline transformation: (I) -> O?
): ReceiveChannel<O> {
    val outChannel = Channel<O>(capacity = 64)
    launch {
        for (message in inputChannel) {
            transformation(message)?.let { outChannel.send(it) }
        }
        outChannel.close()
    }
    return outChannel
}