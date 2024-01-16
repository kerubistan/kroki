package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

suspend fun <T> CoroutineScope.fork(
	channel: ReceiveChannel<T>,
	outCapacity: Int = 4
): Pair<ReceiveChannel<T>, ReceiveChannel<T>> {
	val left = Channel<T>(capacity = outCapacity)
	val right = Channel<T>(capacity = outCapacity)

	launch {
		for (message in channel) {
			left.send(message)
			right.send(message)
		}
		left.close()
		right.close()
	}

	return left to right
}