package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

fun <T, K> CoroutineScope.mergeChannel(
	channel: Channel<T>,
	key: (T) -> K,
	outChannelCapacity : Int = 1024,
	merge: (T, T) -> T): Channel<T> {
    val output = Channel<T>(capacity = outChannelCapacity)

    launch {
        var last: T? = null
        for (message in channel) {
            if (last == null) {
                last = message
            } else {
                if (key(last) == key(message)) {
                    last = merge(last, message)
                } else {
                    output.send(last)
                    last = message
                }
            }
        }
        if (last != null) {
            output.send(last)
        }
        output.close()
    }

    return output
}
