package io.github.kerubistan.kroki.collections

internal class ImmutableTinyMap<K, V : Any>(
	private val key1: K,
	private val value1: V,
	private val key2: K,
	private val value2: V
) : Map<K, V> {

	override val entries: Set<Map.Entry<K, V>> by lazy {
		setOf(
			ImmutableHashMap.ImmutableHashMapEntry(key1, value1),
			ImmutableHashMap.ImmutableHashMapEntry(key2, value2)
		)
	}
	override val keys: Set<K> by lazy { setOf(key1, key2) }
	override val size: Int = 2
	override val values: Collection<V> by lazy { immutableListOf(value1, value2) }

	override fun isEmpty(): Boolean = false

	override fun get(key: K): V? =
		when (key) {
			key1 -> value1
			key2 -> value2
			else -> null
		}

	override fun containsValue(value: V): Boolean = value1 == value || value2 == value

	override fun containsKey(key: K): Boolean = key1 == key || key2 == key

}
