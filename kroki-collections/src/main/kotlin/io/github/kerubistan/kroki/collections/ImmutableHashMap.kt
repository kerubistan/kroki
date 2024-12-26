package io.github.kerubistan.kroki.collections

internal class ImmutableHashMap<K, V>(private val hashTable : Array<ImmutableHashMapEntry<K, V>>) : Map<K, V> {

	init {
		require(hashTable.isNotEmpty()) { "For empty maps, use simply emptyMap" }
	}

	companion object {

		internal fun <K, V> findStartAndEnd(hashCode : Int, position: Int, hashTable : Array<ImmutableHashMapEntry<K, V>>) : Pair<Int, Int> {
			TODO()
		}

		internal fun <K, V> indexesForHash(hashCode : Int, hashTable : Array<ImmutableHashMapEntry<K, V>>) : Pair<Int, Int>? {
			var start = 0
			var end = hashTable.size
			while (start != end - 1) {
				var middleIndex = start + ((end - start)  /2)
				val middle = hashTable[middleIndex]
				when {
					middle.hashCode == hashCode -> {

					}
				}
			}

			return start to end

		}

	}

	class ImmutableHashMapEntry<K, V>(override val key: K, override val value: V) : Map.Entry<K, V> {

		val hashCode = key.hashCode()

		override fun hashCode() = hashCode

		override fun equals(other: Any?): Boolean {
			if (this === other) return true

			other as ImmutableHashMapEntry<*, *>

			if (key != other.key) return false
			if (value != other.value) return false

			return true
		}
	}

	private class KeySetIterator<K, V>(private val hashTable : Array<ImmutableHashMapEntry<K, V>>) : Iterator<K> {
		var index = 0;
		override fun hasNext(): Boolean = index < hashTable.size

		override fun next(): K {
			val key = hashTable[index].key
			index++
			return key
		}
	}

	private class KeySet<K, V>(private val hashTable : Array<ImmutableHashMapEntry<K, V>>) : Set<K> {
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
		get() = TODO("not implemented")
	override val keys: Set<K>
		get() = KeySet(hashTable)
	override val size: Int
		get() = hashTable.size
	override val values: Collection<V>
		get() = TODO("not implemented")

	override fun isEmpty(): Boolean  = hashTable.isEmpty()

	override fun get(key: K): V? {
		val keyHash = key.hashCode()

		indexesForHash(keyHash, hashTable)

		TODO("not implemented")
	}


	// this is slow and that's fine
	override fun containsValue(value: V): Boolean = hashTable.any {
		it.value == value
	}

	// this has to be quick
	override fun containsKey(key: K): Boolean {
		TODO("not implemented")
	}

}
