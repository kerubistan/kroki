package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce

suspend fun <T> CoroutineScope.batch(channel: ReceiveChannel<T>, chunkSize: Int): ReceiveChannel<List<T>> =
	produce {
		var list = mutableListOf<T>()
		for (message in channel) {
			list.add(message)
			if (list.size >= chunkSize) {
				send(list)
				list = mutableListOf()
			}
		}
		if (list.isNotEmpty()) {
			send(list)
		}
	}
