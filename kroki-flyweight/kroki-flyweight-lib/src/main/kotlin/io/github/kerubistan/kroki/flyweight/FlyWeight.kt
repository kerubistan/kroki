package io.github.kerubistan.kroki.flyweight

import kotlin.reflect.KFunction

fun <T : Any> T.flyWeight(instanceCache: InstanceCache = GlobalInstanceCache) : T =
	if(this.javaClass.kotlin.isData) {
		var workInstance = this
		val memberFunctions = this.javaClass.kotlin.members.filterIsInstance<KFunction<*>>()
		val copy = memberFunctions.single { it.name == "copy" }
		val deDuplicatedFields = memberFunctions.filter { it.name.startsWith("component") }.sortedBy { it.name }.mapIndexed {
			index, component ->
			var componentValue = component.call(workInstance)
			copy.parameters[index + 1] to if(componentValue != null) {
				if(componentValue.javaClass.isEnum) {
					componentValue
				} else {
					val componentCache = instanceCache.cacheForClass(componentValue.javaClass.kotlin)
					if(componentCache.containsKey(componentValue)) {
						componentCache[componentValue]
					} else {
						componentValue = componentValue.flyWeight(instanceCache)
						componentCache.put(componentValue, componentValue)
						componentValue
					}
				}
			} else null
		}.toMap() + (copy.parameters[0] to workInstance)
		workInstance = copy.callBy(deDuplicatedFields) as T
		workInstance
	} else this