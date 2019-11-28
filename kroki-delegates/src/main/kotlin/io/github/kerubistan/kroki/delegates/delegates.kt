package io.github.kerubistan.kroki.delegates

import java.io.Serializable
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

private class FunctionLiteralThreadLocal<T>(private val initializer: () -> T) : ThreadLocal<T>() {
    override fun initialValue(): T = initializer()
}

class ThreadLocalDelegate<T>(initializer: () -> T) {

    private val threadLocal = FunctionLiteralThreadLocal(initializer)

    operator fun getValue(obj: Any?, property: KProperty<*>): T = threadLocal.get()
}

fun <T> threadLocal(initializer: () -> T) = ThreadLocalDelegate(initializer)

interface WeakDelegate<T> : Serializable {
    val value: T
    operator fun getValue(obj: Any?, property: KProperty<*>): T = value
}

private class WeakDelegateImpl<T>(
    val mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
    val initializer: () -> T
) : WeakDelegate<T> {

    @Transient
    private var weakRef: WeakReference<Lazy<T>>? = WeakReference(lazy(mode, initializer))

    private val safeWeak: WeakReference<Lazy<T>>
        get() = if (weakRef == null) {
            val temp = WeakReference(lazy(mode, initializer))
            weakRef = temp
            temp
        } else weakRef!!


    override val value
        get() = safeWeak.get()?.value ?: WeakReference(
            lazy(
                mode,
                initializer
            )
        ).let {
            val lazy = lazy(mode, initializer)
            weakRef = WeakReference(lazy)
            lazy.value
        }

    override operator fun getValue(obj: Any?, property: KProperty<*>): T = value

}

/**
 * Delegate to weak references.
 *
 * a weak delegate is not only evaluated on demand (lazy) but also since
 * it is tracked with a weak reference, may loose its value and therefore
 * may be recalculated again on demand.
 *
 */
fun <T> weak(mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED, initializer: () -> T): WeakDelegate<T> =
    WeakDelegateImpl(mode = mode, initializer = initializer)

