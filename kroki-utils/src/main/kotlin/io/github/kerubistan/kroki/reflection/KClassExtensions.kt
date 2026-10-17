package io.github.kerubistan.kroki.reflection

import kotlin.reflect.KClass

/**
 * Checks if a class is an enum class.
 */
val KClass<*>.isEnum
	get() : Boolean = this.supertypes.any {
				it.classifier == Enum::class
			}

/**
 * Checks if a class is comparable.
 */
val KClass<*>.isComparable
	get() : Boolean = this.supertypes.any {
		it.classifier == Comparable::class
	}
