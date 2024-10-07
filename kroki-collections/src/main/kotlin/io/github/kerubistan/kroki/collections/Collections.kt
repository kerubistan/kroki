package io.github.kerubistan.kroki.collections

fun <T : Any> immutableListOf(vararg items: T): List<T> =
	when (items.size) {
		0 -> emptyList()
		1 -> listOf(items.single())
		else -> ImmutableArrayList(items)
	}

inline fun <reified T : Any> List<T>.toImmutableList(): List<T> = when {
	this.isEmpty() -> emptyList()
	this.size == 1 -> listOf(first())
	else -> immutableListOf(*(this.toTypedArray()))
}

internal fun <T> listHashCode(list: List<T>): Int {
	var hashCode = 1
	list.forEach { hashCode = (hashCode * 31) + it.hashCode() }
	return hashCode
}

fun <T : Comparable<T>> List<T>.immutableSorted(): List<T> =
	when (this) {
		is ImmutableArrayList -> this.sortedToImmutable()
		else -> this.sorted()
	}
