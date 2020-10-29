package io.github.kerubistan.kroki.flyweight

import kotlin.reflect.KFunction

fun <T : Any> T.deduplicate(instanceCache: InstanceCache = GlobalInstanceCache) : T =
	if(this.javaClass.kotlin.isData) {
		var workInstance = this
		val memberFunctions = this.javaClass.kotlin.members.filterIsInstance<KFunction<*>>()
		val copy = memberFunctions.single { it.name == "copy" }
		val deduplicatedFeilds = memberFunctions.filter { it.name.startsWith("component") }.sortedBy { it.name }.mapIndexed {
			index, component ->
			var componentValue = component.call(workInstance)
			copy.parameters[index + 1] to if(componentValue != null) {
				val componentCache = instanceCache.cacheForClass(componentValue.javaClass.kotlin)
				if(componentCache.containsKey(componentValue)) {
					componentCache[componentValue]
				} else {
					componentValue = componentValue.deduplicate(instanceCache)
					componentCache.put(componentValue, componentValue)
					componentValue
				}
			} else null
		}.toMap() + (copy.parameters[0] to workInstance)
		workInstance = copy.callBy(deduplicatedFeilds) as T
		workInstance
	} else this