package io.github.kerubistan.kroki.collections

fun <T : Any> immutableListOf(vararg items: T): List<T> =
	when (items.size) {
		0 -> emptyList()
		1 -> listOf(items.first())
		else -> ImmutableArrayList(items)
	}
