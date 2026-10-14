package io.github.kerubistan.kroki.reflection

import kotlin.reflect.KClass

val KClass<*>.isEnum
	get() : Boolean {
		// quick check if a class is an enum type
		return this.isFinal
				&& !this.isOpen
				&& !this.isData
				// the more expensive check
				&& this.supertypes.any {
					it.classifier == Enum::class
				}
	}