package io.github.kerubistan.kroki.collections

internal class ImmutableMapEntrySet<K, V>(private val hashTable: Array<ImmutableMapEntry<K, V>>) :
	Set<Map.Entry<K, V>> {
	override val size: Int = hashTable.size

	override fun isEmpty(): Boolean = hashTable.isEmpty()

	override fun iterator(): Iterator<Map.Entry<K, V>> = hashTable.iterator()

	override fun containsAll(elements: Collection<Map.Entry<K, V>>): Boolean {
		// TODO on can easily imagine something more performant than this, but for now
		//  let's make the simple solution we iterate on the items and check that all are in the set
		//  However a better solution could be that we calculate the hash for each key
		return elements.all { this.contains(it) }
	}

	override fun contains(element: Map.Entry<K, V>): Boolean {
		TODO("not implemented")
	}

}
