package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

fun <T, K> CoroutineScope.mergeChannel(
	channel: ReceiveChannel<T>,
	key: (T) -> K,
	outChannelCapacity : Int = 1024,
	merge: (T, T) -> T): ReceiveChannel<T> {
    val output = Channel<T>(capacity = outChannelCapacity)

    launch {
        var last: T? = null
        for (message in channel) {
			last = if (last == null) {
				message
			} else {
				if (key(last) == key(message)) {
					merge(last, message)
				} else {
					output.send(last)
					message
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
