package io.github.kerubistan.kroki.collections

import java.util.function.Consumer

internal class Iterator2<T>(val list: ImmutableList2<T>) : ListIterator<T> {
	var idx: Byte = 0
	override fun next(): T = when (idx++) {
		0.toByte() -> list.first
		1.toByte() -> list.second
		else -> throw IllegalArgumentException()
	}

	override fun hasNext(): Boolean = idx <= 1

	override fun hasPrevious(): Boolean = idx > 0

	override fun previous(): T = when (idx) {
		1.toByte() -> list.first
		else -> throw IllegalArgumentException()
	}

	override fun nextIndex(): Int = idx + 1

	override fun previousIndex(): Int = idx - 1

}

internal class ImmutableList2<T>(internal val first: T, internal val second: T) : List<T> {

	override val size: Int = 2

	override fun isEmpty(): Boolean = false

	override fun contains(element: T) = first == element || second == element

	override fun iterator(): Iterator<T> = Iterator2(this)

	override fun containsAll(elements: Collection<T>): Boolean = elements.all { this.contains(it) }

	override fun get(index: Int): T = when (index) {
		0 -> first
		1 -> second
		else -> throw IllegalArgumentException("index is out of bounds")
	}

	override fun indexOf(element: T): Int = when {
		first == element -> 0
		second == element -> 1
		else -> -1
	}

	override fun lastIndexOf(element: T): Int = when {
		second == element -> 1
		first == element -> 0
		else -> -1
	}

	override fun listIterator(): ListIterator<T> = Iterator2(this)

	override fun listIterator(index: Int): ListIterator<T> = when (index) {
		0 -> Iterator2(this)
		1 -> listOf(this.second).listIterator()
		else -> throw IndexOutOfBoundsException()
	}

	override fun subList(fromIndex: Int, toIndex: Int): List<T> = when {
		fromIndex < 0 -> throw IndexOutOfBoundsException()
		toIndex > 2 -> throw IndexOutOfBoundsException()
		fromIndex == 0 -> when (toIndex) {
			0 -> emptyList()
			1 -> listOf(first)
			2 -> this
			else -> throw IndexOutOfBoundsException("fromIndex is out of bounds")
		}
		fromIndex == 1 -> when (toIndex) {
			1 -> emptyList()
			2 -> listOf(second)
			else -> throw IndexOutOfBoundsException("fromIndex is out of bounds")
		}

		else -> throw IndexOutOfBoundsException("fromIndex is out of bounds")
	}

	override fun toString(): String = "[$first,$second]"

	override fun forEach(action: Consumer<in T>) {
		action.accept(first)
		action.accept(second)
	}

	override fun equals(other: Any?): Boolean {
		return (other is List<*>) && this.first == other[0] && this.second == other[1]
	}

	private val hashCode by lazy { listHashCode(this) }

	override fun hashCode(): Int = hashCode
}