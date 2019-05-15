package com.github.kroki.collections

fun <K, V> Map<K, V>.upsert(key: K, mapper: (V) -> V, init: () -> V): Map<K, V> =
    this[key]?.let { this + (key to mapper(it)) } ?: this+(key to init())
