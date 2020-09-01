package io.github.kerubistan.kroki.delegates

import java.io.Serializable
import java.lang.ref.Reference
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

/**
 * Delegate interface for all kind of reference (weak, soft) delegates.
 */
interface ReferenceDelegate<T> : Serializable {
	val value: T
	operator fun getValue(obj: Any?, property: KProperty<*>): T = value
}

private abstract class AbstractReferenceDelegateImpl<T, R : Reference<Lazy<T>>>(
	val mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
	val initializer: () -> T
) : ReferenceDelegate<T> {
	@Transient
	private var weakRef: Reference<Lazy<T>>? = this.initRef()

	private val safeWeak: Reference<Lazy<T>>
		get() = if (weakRef == null) {
			val temp = WeakReference(lazy(mode, initializer))
			weakRef = temp
			temp
		} else weakRef!!

	abstract fun initRef(): R

	override val value
		get() = safeWeak.get()?.value ?: initRef().let {
			val lazy = lazy(mode, initializer)
			weakRef = WeakReference(lazy)
			lazy.value
		}

	override operator fun getValue(obj: Any?, property: KProperty<*>): T = value

}

private class WeakDelegateImpl<T>(
	mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
	initializer: () -> T
) : AbstractReferenceDelegateImpl<T, WeakReference<Lazy<T>>>(mode, initializer) {

	override fun initRef() = WeakReference(lazy(mode, initializer))

}

private class SoftDelegateImpl<T>(
	mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
	initializer: () -> T
) : AbstractReferenceDelegateImpl<T, SoftReference<Lazy<T>>>(mode, initializer) {

	override fun initRef() = SoftReference(lazy(mode, initializer))

}

/**
 * Delegate to weak references.
 *
 * a weak delegate is not only evaluated on demand (lazy) but also since
 * it is tracked with a weak reference, may loose its value and therefore
 * may be recalculated again on demand.
 *
 * @param mode the initialization mode of the lazy delegate
 * @param initializer the function literal that initializes the value referenced
 */
fun <T> weak(
	mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
	initializer: () -> T
): ReferenceDelegate<T> =
	WeakDelegateImpl(mode = mode, initializer = initializer)

/**
 * Delegate to soft references.
 *
 * @param mode the initialization mode of the lazy delegate
 * @param initializer the function literal that initializes the value referenced
 */
fun <T> soft(
	mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED,
	initializer: () -> T
): ReferenceDelegate<T> =
	SoftDelegateImpl(mode = mode, initializer = initializer)
