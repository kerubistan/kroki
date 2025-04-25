package io.github.kerubistan.kroki.collections

internal class ArrayTransformList<FROM, TO>(
	private val items: Array<FROM>,
	private val transform: (FROM) -> TO
) : List<TO> {
	override val size: Int = items.size

	override fun get(index: Int): TO = try {
		transform(items[index])
	} catch (iab: ArrayIndexOutOfBoundsException) {
		throw IndexOutOfBoundsException(index)
	}

	override fun isEmpty(): Boolean = items.isEmpty()

	override fun iterator(): Iterator<TO> = ArrayTransformIterator(items, transform)

	override fun listIterator(): ListIterator<TO> = ArrayTransformIterator(items, transform)

	override fun listIterator(index: Int): ListIterator<TO> = ArrayTransformIterator<FROM, TO>(items, transform)

	override fun subList(fromIndex: Int, toIndex: Int): List<TO> {
		// TODO we could give the array to another array transform list
		return ArrayTransformList(items.sliceArray(fromIndex until toIndex), transform)
	}

	override fun lastIndexOf(element: TO): Int = items.indexOfLast { transform(it) == element }

	override fun indexOf(element: TO): Int = items.indexOfFirst { transform(it) == element }

	override fun containsAll(elements: Collection<TO>): Boolean = elements.all { this.contains(it) }

	override fun contains(element: TO): Boolean = items.any { transform(it) == element }

}