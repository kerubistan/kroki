package io.github.kerubistan.kroki.collections

import io.github.kerubistan.kroki.delegates.weak

/**
 * An immutable hashmap implementation on a flat array of items sorted by their key hashCode.
 * It is expected that the array is already ordered. The implementation doesn't take a copy of the array.
 */
internal class ImmutableHashMap<K, V>(private val hashTable: Array<ImmutableMapEntry<K, V>>) : Map<K, V> {

	init {
		require(hashTable.isNotEmpty()) { "For empty maps, use simply emptyMap" }
	}

	companion object {

		internal fun <K, V> Array<ImmutableMapEntry<K, V>>.findInRange(range: Pair<Int, Int>, key : K) : ImmutableMapEntry<K, V>? {
			for (index in range.first .. range.second) {
				if(this[index].key == key) {
					return this[index]
				}
			}
			return null
		}

		/**
		 * Find the start and end of a hashcode. If the hashCode is not found, then it will return null.
		 */
		internal fun <K, V> indexesForHash(
			hashCode: Int,
			hashTable: Array<ImmutableMapEntry<K, V>>
		): Pair<Int, Int>? {
			// TODO: this is slow, ignores the fact that the array is sorted
			val first = hashTable.indexOfFirst { it.key.hashCode() == hashCode }
			return if (first > -1) {
				val last = hashTable.indexOfLast { it.key.hashCode() == hashCode }
				first to last
			} else null
		}

	}

	private class KeySetIterator<K, V>(private val hashTable: Array<ImmutableMapEntry<K, V>>) : Iterator<K> {
		var index = 0;
		override fun hasNext(): Boolean = index < hashTable.size

		override fun next(): K {
			val key = hashTable[index].key
			index++
			return key
		}
	}

	private class KeySet<K, V>(private val hashTable: Array<ImmutableMapEntry<K, V>>) : Set<K> {
		override val size: Int = hashTable.size

		override fun isEmpty(): Boolean = false

		override fun iterator(): Iterator<K> = KeySetIterator(hashTable)

		override fun containsAll(elements: Collection<K>): Boolean = elements.all { this.contains(it) }

		override fun contains(element: K): Boolean {
			// TODO this is kind of low performing solution
			return hashTable.any { it.key == element }
		}

	}

	override val entries: Set<Map.Entry<K, V>>
		get() = ImmutableMapEntrySet(hashTable)
	override val keys: Set<K>
		get() = KeySet(hashTable)
	override val size: Int
		get() = hashTable.size
	override val values: Collection<V>
		get() = ArrayTransformList(hashTable) { it.value }

	override fun isEmpty(): Boolean = hashTable.isEmpty()

	override fun get(key: K): V? {
		val keyHash = key.hashCode()

		val range = indexesForHash(keyHash, hashTable)
		return if(range == null) {
			null
		} else {
			hashTable.findInRange(range, key)?.value
		}
	}


	// this is slow and that's fine
	override fun containsValue(value: V): Boolean = hashTable.any {
		it.value == value
	}

	private val weakString by weak {
		buildString {
			append('{')
			var first = true
			forEach { key, value ->
				if(!first) {
					append(',')
					append(' ')
				} else {
					first = false
				}
				append(key)
				append('=')
				append(value)
			}
			append('}')
		}
	}

	override fun toString(): String = weakString

	// this has to be quick
	override fun containsKey(key: K): Boolean {
		val range = indexesForHash(key.hashCode(), hashTable)
		return if(range == null) {
			false
		} else {
			hashTable.findInRange(range, key) != null
		}
	}

	override fun equals(other: Any?): Boolean {
		return when {
			other == null -> false
			other === this -> true
			other is Map<*, *> -> this.size == other.size && all { (key, value) -> other[key] == value }
			else -> false
		}
	}
}
