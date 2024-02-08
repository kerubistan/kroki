package io.github.kerubistan.kroki.coroutines.channels

import io.github.kerubistan.kroki.objects.isGreaterThan
import io.github.kerubistan.kroki.objects.isLessThan
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import java.util.*

internal data class Node<T>(val item: T, var next: Node<T>?)

internal class SortedList<T>(private val comparator: Comparator<T>) {

	private var first: Node<T>? = null

	constructor(items: Collection<T>, comparator: Comparator<T>) : this(comparator) {
		items.sortedWith(this.comparator).forEach {
			first = Node(it, first)
		}
	}

	fun isNotEmpty() : Boolean = first != null

	fun isEmpty() : Boolean = first == null
	fun removeFirst(): T {
		return first?.let {
			val item = it.item
			first = it.next
			item
		} ?: throw IllegalStateException("Empty list")
	}

	fun add(element: T) {
		when {
			first == null ->
				first = Node(element, null)
			first!!.item.isLessThan(element, comparator) ->
				first = Node(element, first)
			else -> {
				var seek = first!!
				while (seek.next != null && seek!!.next!!.item.isGreaterThan(element, comparator)) {
					seek = seek.next!!
				}
				seek.next = Node(element, seek.next)
			}
		}
	}

}

suspend fun <T> CoroutineScope.joinChannels(
	inputChannels: List<ReceiveChannel<T>>,
	outChannelCapacity: Int = 128,
	comparator: Comparator<T>
): ReceiveChannel<T> = produce<T>(capacity = outChannelCapacity) {
	val inputIterators = SortedList(inputChannels
		.map { it.iterator() }
		.filter { it.hasNext() }
		.map {
			it.next() to it
		}, { a, b -> comparator.compare(b.first, a.first) })

	while (inputIterators.isNotEmpty()) {
		val (item, iterator) = inputIterators.removeFirst()
		send(item)
		if (iterator.hasNext()) {
			val newItem = iterator.next()
			if (inputIterators.isNotEmpty()) {
				inputIterators.add(newItem to iterator)
			} else {
				// was the last channel, now we just dump that all to the out channel
				send(newItem)
				while (iterator.hasNext()) {
					send(iterator.next())
				}
			}
		}
	}

}

