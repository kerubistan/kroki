package io.github.kerubistan.kroki.collections

import io.github.kerubistan.kroki.delegates.weak

internal class ImmutableSubArrayList<T : Any>(
	private val offset: Int,
	private val limit: Int,
	private val items: Array<T>
) : List<T> {

	init {
		require(offset >= 0) { "offset ($offset) must be greater than 0" }
		require(limit > offset) { "limit must be bigger than the offset" }
		require(limit <= items.size + 1) { "limit ($limit) must be less than or equal to the size of the array (${items.size})" }
	}

	override val size: Int = limit - offset

	override fun get(index: Int): T {
		require(index + offset < limit) { "index $index outside of boundaries" }
		return items[offset + index]
	}

	override fun isEmpty(): Boolean = false // because limit > offset, for empty list use emptyList

	internal class SubListIterator<T>(
		private val items: Array<T>,
		private val offset: Int,
		private val limit: Int,
		private var position: Int = offset
	) : ListIterator<T> {
		override fun hasNext(): Boolean = position < limit
		override fun hasPrevious(): Boolean = position > offset

		override fun next(): T {
			require(position < limit) { "reached end of the list" }
			return items[position++]
		}

		override fun nextIndex(): Int = position + 1

		override fun previous(): T {
			require(position > offset)
			return items[position--]
		}

		override fun previousIndex(): Int = position - 1
	}

	override fun iterator(): Iterator<T> = SubListIterator(items, offset, limit)

	override fun listIterator(): ListIterator<T> = SubListIterator(items, offset, limit)

	override fun listIterator(index: Int): ListIterator<T> = SubListIterator(items, offset, limit, index)

	override fun subList(fromIndex: Int, toIndex: Int): List<T> =
		if (fromIndex == toIndex)
			emptyList()
		else
			ImmutableSubArrayList(offset + fromIndex, limit - toIndex, items)

	override fun lastIndexOf(element: T): Int = (offset until limit).last { this[it] == element }

	override fun indexOf(element: T): Int = (offset until limit).first { this[it] == element }

	override fun containsAll(elements: Collection<T>): Boolean = elements.isEmpty() || elements.all { it in this }

	override fun contains(element: T): Boolean = (offset until limit).any { items[it] == element }

	private val stringValue by weak {
		buildString {
			append('[')
			this@ImmutableSubArrayList.forEachIndexed { index, item ->
				if (index > 0) {
					append(',')
				}
				append(item)
			}
			append(']')
		}
	}

	override fun toString(): String {
		return stringValue
	}

	override fun equals(other: Any?): Boolean {
		return other != null
			&& other is List<*>
			&& other.size == size
			&& (0 until this.lastIndex).all { index -> this[index] == other[index] }
	}

	override fun hashCode(): Int {
		var hashCode = 1
		items.forEach { hashCode = (hashCode * 31) + it.hashCode() }
		return hashCode
	}
}