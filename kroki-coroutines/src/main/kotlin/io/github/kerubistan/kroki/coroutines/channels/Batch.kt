package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> CoroutineScope.batch(channel: ReceiveChannel<T>, chunkSize: Int): ReceiveChannel<List<T>> =
	produce {
		var list = ArrayList<T>(chunkSize)
		for (message in channel) {
			list.add(message)
			if (list.size >= chunkSize) {
				send(list)
				list = ArrayList(chunkSize)
			}
		}
		if (list.isNotEmpty()) {
			send(list)
		}
	}
