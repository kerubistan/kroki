package io.github.kerubistan.kroki.objects

import kotlin.reflect.KClass

fun <T> T.isGreaterThan(other: T, comparator: Comparator<T>) =
	comparator.compare(this, other) > 0

fun <T> T.isLessThan(other: T, comparator: Comparator<T>) =
	comparator.compare(this, other) < 0


/**
 * Creates a comparator for any comparable class.
 */
@Suppress("unused")  // of course it is used
inline fun <reified T : Comparable<T>> KClass<T>.comparator() =
	Comparator<T> { first: T, second: T -> first.compareTo(second) }
