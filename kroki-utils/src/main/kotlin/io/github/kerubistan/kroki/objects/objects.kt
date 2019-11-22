package io.github.kerubistan.kroki.objects

import io.github.kerubistan.kroki.collections.concat

/**
 * Find objects in an object graph.
 * @param selector selects objects to navigate
 * @param filter filters objects from the result
 */
fun <T : Any> T.find(selector: (T) -> Iterable<T>?, filter: (T) -> Boolean = { true }): Iterable<T> =
    selector(this)?.filter(filter)?.map { listOf(it) + it.find(selector, filter) }?.concat() ?: listOf()
