package io.github.kerubistan.kroki.collections

fun <K, V> Map<K, V>.upsert(key: K, mapper: (V) -> V, init: () -> V): Map<K, V> =
	this[key]?.let { this + (key to mapper(it)) } ?: this + (key to init())

/**
 * Update one key in the map using function literal. If the key is not found in the map, the resulting map will be
 * unchanged.
 */
fun <K : Any, V : Any> Map<K, V>.update(key: K, mapper: (V) -> V): Map<K, V> =
	this.mapValues {
		if (it.key == key) {
			mapper(it.value)
		} else {
			it.value
		}
	}

@SuppressWarnings("unchecked")
fun <K: Any, V : Any> Map<K, V?>.filterNotNullValues() : Map<K, V> =
	this.filter { it.value != null } as Map<K, V>
