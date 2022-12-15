package io.github.kerubistan.kroki.delegates

import kotlin.reflect.KProperty

/**
 * @suppress
 */
private class FunctionLiteralThreadLocal<T>(private val initializer: () -> T) : ThreadLocal<T>() {
	override fun initialValue(): T = initializer()
}

/**
 * Delegate class of the threadLocal - you do not need to use this directly.
 * @suppress
 */
class ThreadLocalDelegate<T>(initializer: () -> T) {

	private val threadLocal = FunctionLiteralThreadLocal(initializer)

	operator fun getValue(obj: Any?, property: KProperty<*>): T = threadLocal.get()
}

/**
 * Delegate class of the mutable threadLocal - you do not need to use this directly.
 * @suppress
 */
class MutableThreadLocalDelegate<T>(initialValue: T?) {

	constructor() : this(null)

	private val threadLocal = FunctionLiteralThreadLocal(initializer = { initialValue })

	operator fun getValue(obj: Any?, property: KProperty<*>): T? = threadLocal.get()

	operator fun setValue(obj: Any?, property: KProperty<*>, newValue: T?) {
		threadLocal.set(newValue)
	}
}

/**
 * Allows having one instance created by thread.
 * This can be used to make efficient use of non-thread-safe classes, such as SimpleDateFormat or old XML parser API.
 * @param initializer a function literal that initializes the instance for the thread - note that the threadLocal will
 * 		only be settable by the initializer here
 * @sample io.github.kerubistan.kroki.delegates.ConcurrencyKtTest.threadLocalTest
 */
fun <T> threadLocal(initializer: () -> T) = ThreadLocalDelegate(initializer)

/**
 * Mutable thread-local.
 * Delegates the value to a ThreadLocal variable, which is initially null
 * and the value can be set later.
 * @sample io.github.kerubistan.kroki.delegates.ConcurrencyKtTest.assignableThreadLocal
 */
fun <T> threadLocal() = MutableThreadLocalDelegate<T>()

/**
 * Delegates the value to a ThreadLocal variable with initial value, but can later be updated.
 * @param initialValue the initial value of the threadLocal - please remember that this will be on each new thread
 */
fun <T> threadLocal(initialValue: T) = MutableThreadLocalDelegate(initialValue)
