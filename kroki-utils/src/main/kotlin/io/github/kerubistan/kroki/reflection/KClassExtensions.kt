package io.github.kerubistan.kroki.reflection

import kotlin.reflect.KClass

/**
 * Checks if a class is an enum class.
 */
val KClass<*>.isEnum
	get() : Boolean {
		// quick check trying to filter out cases that surely arent enums
		// in order to avoid a more expensive operation
		return this.isFinal
				&& !this.isOpen
				&& !this.isData
				// the more expensive check
				&& this.supertypes.any {
					it.classifier == Enum::class
				}
	}