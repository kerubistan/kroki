package io.github.kerubistan.kroki.collections

class ImmutableMapEntry<K, V>(override val key: K, override val value: V) : Map.Entry<K, V> {

	private val hashCode = key.hashCode()

	override fun hashCode() = hashCode

	override fun equals(other: Any?): Boolean {
		if (this === other) return true

		if(other !is Map.Entry<*, *>) {
			return false
		}

		if (key != other.key) return false
		if (value != other.value) return false

		return true
	}
}