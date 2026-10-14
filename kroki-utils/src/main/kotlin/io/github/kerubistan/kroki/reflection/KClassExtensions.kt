package io.github.kerubistan.kroki.reflection

import kotlin.reflect.KClass

val KClass<*>.isEnum get() : Boolean {
	// quick check if a class is an enum type
	this.isFinal
			&& !this.isOpen
			&& !this.isData
	// the more expensive check
	return this.supertypes.any {
		it.classifier == Enum::class
	}
}