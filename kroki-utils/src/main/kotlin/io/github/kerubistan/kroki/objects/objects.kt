package io.github.kerubistan.kroki.objects

import io.github.kerubistan.kroki.collections.join

fun <T : Any> T.find(selector: (T) -> Iterable<T>?, filter: (T) -> Boolean = { true }): Iterable<T> =
    selector(this)?.filter(filter)?.map { listOf(it) + it.find(selector, filter) }?.join() ?: listOf()
