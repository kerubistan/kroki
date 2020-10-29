package io.github.kerubistan.kroki.dedup

import kotlin.reflect.KClass

object GlobalInstanceCache : InstanceCache {

	private val caches: MutableMap<KClass<*>, MutableMap<*, *>> = mutableMapOf()

	override fun <T : Any> cacheForClass(clazz: KClass<T>): MutableMap<T, T> {
		val cache =
			caches[clazz] ?: mutableMapOf<T, T>().apply { synchronized(this@GlobalInstanceCache) { caches[clazz] = this } }
		return cache as MutableMap<T, T>
	}
}