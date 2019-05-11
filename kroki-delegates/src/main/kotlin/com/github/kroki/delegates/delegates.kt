package com.github.kroki.delegates

import kotlin.reflect.KProperty

class ThreadLocalDelegate<T>(initializer : () -> T) {
	operator fun getValue(obj : Any?, property : KProperty<*>) {

	}
}

fun <T> threadLocal(initializer : () -> T) = ThreadLocalDelegate(initializer)