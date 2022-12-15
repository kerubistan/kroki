package io.github.kerubistan.kroki.flyweight

import java.util.*
import kotlin.reflect.KClass

/**
 * A jvm-global instance cache used as default by flyWeight
 * @see flyWeight
 */
object GlobalInstanceCache : InstanceCache {

	private val caches: MutableMap<KClass<*>, MutableMap<*, *>> = WeakHashMap()

	@Suppress("UNCHECKED_CAST")
	override fun <T : Any> cacheForClass(clazz: KClass<T>): MutableMap<T, T> {
		val cache =
			caches[clazz] ?: WeakHashMap<T, T>().apply {
				synchronized(this@GlobalInstanceCache) {
					caches[clazz] = this
				}
			}
		return cache as MutableMap<T, T>
	}
}