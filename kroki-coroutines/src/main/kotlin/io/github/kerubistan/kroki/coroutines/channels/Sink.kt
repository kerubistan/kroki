package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

suspend fun <T> CoroutineScope.sink(channel: ReceiveChannel<T>) {
	launch {
		val iterator = channel.iterator()
		while (iterator.hasNext()) {
			iterator.next()
		}
    }
}