package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import java.util.LinkedList
import kotlin.random.Random

/**
 * Creates a channel with the messages from the input channel in a randomized order.
 */
fun <T> CoroutineScope.shuffle(
	channel: ReceiveChannel<T>,
	capacity: Int,
	outCapacity: Int = 4,
	random: Random = Random.Default
): ReceiveChannel<T> = produce(capacity = outCapacity) {
	val buffer = LinkedList<T>()
	for (message in channel) {
		if (buffer.size >= capacity) {
			val index = random.nextInt(buffer.size)
			send(buffer.removeAt(index))
		}
		buffer.add(message)
	}
	while (buffer.isNotEmpty()) {
		val index = random.nextInt(buffer.size)
		send(buffer.removeAt(index))
	}
}