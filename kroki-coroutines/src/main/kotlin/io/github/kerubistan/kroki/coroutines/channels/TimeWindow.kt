package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration

internal sealed interface Token

internal object Tick : Token

internal data class Event<T>(val value: T) : Token

fun <T> CoroutineScope.timeWindow(
	inputs: ReceiveChannel<T>,
	window: Duration,
): ReceiveChannel<Collection<T>> {
	val aggregationChannel = Channel<Collection<T>>()
	val channel = Channel<Token>()
	// send time ticks
	launch {
		while (!channel.isClosedForSend) {
			delay(window)
			if (!channel.isClosedForSend) {
				channel.trySend(Tick)
			}
		}
	}
	// process time-signals and inputs
	launch {
		val list = mutableListOf<T>()
		channel.consumeEach { token ->
			when (token) {
				is Tick -> {
					aggregationChannel.send(list.toList())
					list.clear()
				}

				is Event<*> -> {
					list.add(token.value as T)
				}
			}
		}
		aggregationChannel.close()
	}
	launch {
		inputs.consumeEach {
			channel.send(Event(it))
		}
		channel.close()
	}

	return aggregationChannel
}