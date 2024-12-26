package io.github.kerubistan.kroki.collections

import java.util.Collections

class ImmutableHashMapBuilder<K, V : Any>(private var increment: Int = 128) {

	private var size = 0
	private var items: Array<ImmutableHashMap.ImmutableHashMapEntry<K, V>?> = arrayOfNulls(increment)

	private fun resize(size: Int) {
		if (items.size < size) {
			val newItems = arrayOfNulls<ImmutableHashMap.ImmutableHashMapEntry<K, V>>(items.size + increment)
			items.copyInto(newItems)
		}
	}

	fun put(key: K, value: V) {
		resize(size + 1)
		items[size] = ImmutableHashMap.ImmutableHashMapEntry(key, value)
		size++
	}

	fun put(pair: Pair<K, V>) {
		put(pair.first, pair.second)
	}

	fun put(vararg pairs: Pair<K, V>) {
		resize(size + pairs.size)
		pairs.forEachIndexed { index, pair ->
			items[size + index] = ImmutableHashMap.ImmutableHashMapEntry(pair.first, pair.second)
		}
		size += pairs.size
	}

	fun preAllocate(size: Int) {
		increment = size
	}

	fun build(): Map<K, V> {
		return when (size) {
			0 -> emptyMap()
			1 -> {
				val first = requireNotNull(items.first())
				ImmutableSingletonMap(first.key, first.value)
//				Collections.singletonMap(first.key, first.value)
			}
			2 -> {
				val first = requireNotNull(items.first())
				val second = requireNotNull(items[1])
				ImmutableTinyMap(first.key, first.value, second.key, second.value)
			}
			else ->
				if (items.size == size) {
					@Suppress("UNCHECKED_CAST")
					ImmutableHashMap(
						items as Array<ImmutableHashMap.ImmutableHashMapEntry<K, V>>
					)
				} else {
					@Suppress("UNCHECKED_CAST")
					ImmutableHashMap(
						items.copyOf(size) as Array<ImmutableHashMap.ImmutableHashMapEntry<K, V>>
					)
				}
		}
	}
}