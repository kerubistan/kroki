package io.github.kerubistan.kroki.collections

class ImmutableSingletonMap<K, V>(private val key: K, private val value: V) : Map<K, V> {
	override val entries: Set<Map.Entry<K, V>>
		get() = TODO("not implemented")
	override val keys: Set<K>
		get() = TODO("not implemented")
	override val size: Int
		get() = TODO("not implemented")
	override val values: Collection<V>
		get() = TODO("not implemented")

	override fun isEmpty(): Boolean = false

	override fun get(key: K): V? = if (key == this.key) value else null

	override fun containsValue(value: V): Boolean = value == this.value

	override fun containsKey(key: K): Boolean = key == this.key

}