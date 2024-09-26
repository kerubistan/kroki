package io.github.kerubistan.kroki.collections

import io.github.kerubistan.kroki.delegates.weak

internal class ImmutableArrayList<T : Any>() : List<T> {

	private lateinit var items: Array<out T>

	internal constructor(array: Array<out T>) : this() {
		this.items = array
	}

	companion object {
		fun <T : Any> of(vararg items: T): List<T> = ImmutableArrayList(items)
	}

	override val size: Int
		get() = items.size

	override fun get(index: Int): T = items[index]

	override fun isEmpty(): Boolean = size == 0

	private class ImmutableArrayListIterator<out T : Any>(private val items: Array<T>) : ListIterator<T> {
		private var index = 0

		constructor(items: Array<T>, index: Int) : this(items) {
			check(index > 0) { "Index must be greater than zero" }
			check(index < items.size) { "Index must be less than the size of the array" }
			this.index = index
		}

		override fun hasNext(): Boolean = items.size > index
		override fun hasPrevious(): Boolean = index > 0

		override fun next(): T = items[index++]

		override fun nextIndex(): Int = index + 1

		override fun previous(): T = items[--index]

		override fun previousIndex(): Int = index - 1
	}

	override fun iterator(): Iterator<T> = ImmutableArrayListIterator(this.items)

	override fun listIterator(): ListIterator<T> = ImmutableArrayListIterator(this.items)

	override fun listIterator(index: Int): ListIterator<T> = ImmutableArrayListIterator(this.items, index)

	override fun subList(fromIndex: Int, toIndex: Int): List<T> =
		when {
			toIndex == fromIndex -> {
				emptyList()
			}

			fromIndex == 0 && toIndex == size + 1 -> this
			else -> ImmutableSubArrayList(fromIndex, toIndex, this.items)
		}

	override fun lastIndexOf(element: T): Int = items.lastIndexOf(element)

	override fun indexOf(element: T): Int = items.indexOf(element)

	override fun containsAll(elements: Collection<T>): Boolean = elements.all(items::contains)

	override fun contains(element: T): Boolean = items.contains(element)

	override fun equals(other: Any?): Boolean {
		when (other) {
			is ImmutableArrayList<*> -> return this.items.contentEquals(other.items)
			is List<*> -> {
				if (this.size != other.size)
					return false
				this.items.forEachIndexed { index, item ->
					if (item != other[index]) {
						return false
					}
				}
				return true
			}

			else ->
				return false
		}
	}

	private val hashCode by lazy {
		var hashCode = 1
		this.forEach { hashCode = (hashCode * 31) + it.hashCode() }
		hashCode
	}

	override fun hashCode(): Int = hashCode

	private val stringValue by weak {
		buildString {
			append('[')
			this@ImmutableArrayList.items.forEachIndexed { index, it ->
				if (index != 0) {
					append(',')
				}
				append(it)
			}
			append(']')
		}
	}

	override fun toString(): String = stringValue
}
