package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

fun <T> CoroutineScope.toFlow(channel: ReceiveChannel<T>): Flow<T> =
	flow {
		for (item in channel) {
			emit(item)
		}
	}

fun <T> CoroutineScope.toChannel(flow: Flow<T>): Channel<T> {
	val outChannel = Channel<T>()
	launch {
		flow.collect { outChannel.send(it) }
		outChannel.close()
	}
	return outChannel
}
