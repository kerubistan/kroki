package io.github.kerubistan.kroki.flyweight

import kotlin.reflect.KClass

interface InstanceCache {
	fun <T : Any> cacheForClass(clazz: KClass<T>): MutableMap<T, T>
}