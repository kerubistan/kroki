package io.github.kerubistan.kroki.objects

import kotlin.reflect.KClass

/**
 * Creates a comparator for any comparable class.
 */
@Suppress("unused")  // of course it is used
inline fun <reified T : Comparable<T>> KClass<T>.comparator() =
	Comparator<T> { first: T, second: T -> first.compareTo(second) }
