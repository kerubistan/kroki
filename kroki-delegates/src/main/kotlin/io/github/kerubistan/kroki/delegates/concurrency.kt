package io.github.kerubistan.kroki.delegates

import kotlin.reflect.KProperty

private class FunctionLiteralThreadLocal<T>(private val initializer: () -> T) : ThreadLocal<T>() {
	override fun initialValue(): T = initializer()
}

class ThreadLocalDelegate<T>(initializer: () -> T) {

	private val threadLocal = FunctionLiteralThreadLocal(initializer)

	operator fun getValue(obj: Any?, property: KProperty<*>): T = threadLocal.get()
}

/**
 * Allows having one instance created by thread.
 * This can be used to make efficient use of non-threadsafe classes, such as SimpleDateFormat or old XML parser API.
 * @param initializer a function literal that initializes the instance for the thread
 */
fun <T> threadLocal(initializer: () -> T) = ThreadLocalDelegate(initializer)

