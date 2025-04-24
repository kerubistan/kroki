package io.github.kerubistan.kroki.collections

internal class ArrayTransformIterator<FROM, TO>(private val items: Array<FROM>, private val transform: (FROM) -> TO) :
	ListIterator<TO> {

	private var index = 0
	override fun hasNext(): Boolean = index <= items.lastIndex
	override fun hasPrevious(): Boolean = index > 0

	override fun next(): TO =
		try {
			transform(items[index++])
		} catch (aiob: ArrayIndexOutOfBoundsException) {
			throw IllegalStateException("iterator has no next element", aiob)
		}

	override fun nextIndex(): Int = index + 1

	override fun previous(): TO = try {
		transform(items[--index])
	} catch (aiob: ArrayIndexOutOfBoundsException) {
		throw IllegalStateException("iterator has no next element", aiob)
	}

	override fun previousIndex(): Int = index - 1

}