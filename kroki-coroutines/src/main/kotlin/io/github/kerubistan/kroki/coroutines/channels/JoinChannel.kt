package io.github.kerubistan.kroki.coroutines.channels

import io.github.kerubistan.kroki.objects.isGreaterThan
import io.github.kerubistan.kroki.objects.isLessThan
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce


suspend fun <T> CoroutineScope.joinChannels(
	inputChannels: List<ReceiveChannel<T>>,
	outChannelCapacity: Int = 128,
	comparator: Comparator<T>
): ReceiveChannel<T> = produce<T>(capacity = outChannelCapacity) {
	val inputIterators = inputChannels
		.map { it.iterator() }
		.filter { it.hasNext() }
		.map {
			it.next() to it
		}
		.toMutableList()

	inputIterators.sortWith { a, b -> comparator.compare(a.first, b.first) }

	while (inputIterators.isNotEmpty()) {
		val (item, iterator) = inputIterators.removeFirst()
		send(item)
		if (iterator.hasNext()) {
			val newItem = iterator.next()
			if(inputIterators.isEmpty()) {
				// was the last channel, now we just dump that all to the out channel
				send(newItem)
				while(iterator.hasNext()) {
					send(iterator.next())
				}
			} else {
				//put the channel back to its proper place
				val index = inputIterators.indexOfFirst { it.first.isGreaterThan(newItem, comparator)}
				if(index >= 0) {
					inputIterators.add(index, newItem to iterator)
				} else {
					inputIterators.add(newItem to iterator)
					inputIterators.sortWith { a, b -> comparator.compare(a.first, b.first) }
				}
			}
		}
	}

}

