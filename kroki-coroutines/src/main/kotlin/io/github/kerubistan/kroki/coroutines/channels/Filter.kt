package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

/**
 * Creates a channel with only the selected messages from the input channel.
 */
fun <T> CoroutineScope.filter(inputChannel: ReceiveChannel<T>, filterLogic: (T) -> Boolean): Channel<T> {
	val filtered = Channel<T>(64)

	launch {
		for (input in inputChannel) {
			if (filterLogic(input)) {
				filtered.send(input)
			}
		}
		filtered.close()

	}
	return filtered
}