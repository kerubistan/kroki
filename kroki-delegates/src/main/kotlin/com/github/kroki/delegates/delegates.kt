package com.github.kroki.delegates

import kotlin.reflect.KProperty

private class FunctionLiteralThreadLocal<T>(private val initializer: () -> T) : ThreadLocal<T>() {
    override fun initialValue(): T = initializer()
}

class ThreadLocalDelegate<T>(initializer: () -> T) {

    private val threadLocal = FunctionLiteralThreadLocal(initializer)

    operator fun getValue(obj: Any?, property: KProperty<*>): T = threadLocal.get()
}

fun <T> threadLocal(initializer: () -> T) = ThreadLocalDelegate(initializer)