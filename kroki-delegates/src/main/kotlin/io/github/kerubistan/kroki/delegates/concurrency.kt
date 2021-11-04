package io.github.kerubistan.kroki.delegates

import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KProperty

private class FunctionLiteralThreadLocal<T>(private val initializer: () -> T) : ThreadLocal<T>() {
	override fun initialValue(): T = initializer()
}

class ThreadLocalDelegate<T>(initializer: () -> T) {

	private val threadLocal = FunctionLiteralThreadLocal(initializer)

	operator fun getValue(obj: Any?, property: KProperty<*>): T = threadLocal.get()
}

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
 * This can be used to make efficient use of non-threadsafe classes, such as SimpleDateFormat or old XML parser API.
 * @param initializer a function literal that initializes the instance for the thread
 */
fun <T> threadLocal(initializer: () -> T) = ThreadLocalDelegate(initializer)

/**
 * Delegates the value to a ThreadLocal variable, which is initially null
 * and the value can be set later.
 */
fun <T> threadLocal() = MutableThreadLocalDelegate<T>()

/**
 * Delegates the value to a ThreadLocal variable with initial value, but can later be updated.
 * @param initialValue the initial value of the threadLocal - please remember that this will be on each new thread
 */
fun <T> threadLocal(initialValue: T) = MutableThreadLocalDelegate(initialValue)

class AtomicReferenceDelegate<T>(private val reference: AtomicReference<T>) {

	operator fun getValue(obj: Any?, property: KProperty<*>): T? = reference.get()

	operator fun setValue(obj: Any?, property: KProperty<*>, newValue: T?) {
		reference.set(newValue)
	}

}

/**
 * Gives a simplified access to an atomic reference value.
 * @param reference the atomic reference
 */
fun <T> atomic(reference: AtomicReference<T>) = AtomicReferenceDelegate(reference)
