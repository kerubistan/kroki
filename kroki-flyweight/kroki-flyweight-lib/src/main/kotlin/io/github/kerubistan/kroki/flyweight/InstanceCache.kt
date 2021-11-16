package io.github.kerubistan.kroki.flyweight

import kotlin.reflect.KClass

/**
 * Instance cache plug-in interface.
 */
interface InstanceCache {
	fun <T : Any> cacheForClass(clazz: KClass<T>): MutableMap<T, T>
}