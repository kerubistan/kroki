package io.github.kerubistan.kroki.flyweight

import kotlin.reflect.KFunction

fun <T : Any> T.flyWeight(instanceCache: InstanceCache = GlobalInstanceCache): T =
	when {
		this is String ->
			this.intern() as T
		this.javaClass.kotlin.isData -> {
			val memberFunctions = this.javaClass.kotlin.members.filterIsInstance<KFunction<*>>()
			val copy = memberFunctions.single { it.name == "copy" }
			val deDuplicatedFields = memberFunctions
				.filter { it.name.startsWith("component") }
				.sortedBy { it.name.substringAfter("component").toInt() }
				.mapIndexed { index, component ->
					var componentValue = component.call(this)
					copy.parameters[index + 1] to if (componentValue != null) {
						if (noFlyWeightType(componentValue, component)) {
							componentValue
						} else {
							val componentCache = instanceCache.cacheForClass(componentValue.javaClass.kotlin)
							if (componentCache.containsKey(componentValue)) {
								componentCache[componentValue]
							} else {
								componentValue = componentValue.flyWeight(instanceCache)
								componentCache.put(componentValue, componentValue)
								componentValue
							}
						}
					} else null
				}.toMap() + (copy.parameters[0] to this)
			copy.callBy(deDuplicatedFields) as T
		}
		this is Set<*> ->
			this.map { it?.flyWeight(instanceCache) }.toSet() as T
		this is List<*> -> {
			this.map { it?.flyWeight(instanceCache) } as T
		}
		this is Map<*, *> -> {
			this.map { (key, value) ->
				key?.flyWeight(instanceCache) to value?.flyWeight(instanceCache)
			}.toMap() as T
		}
		else -> this
	}

private val noFlyWeightTypes = setOf(
	Boolean::class,
	Short::class,
	Byte::class,
	Char::class,
	Int::class,
	Long::class
)

private fun noFlyWeightType(componentValue: Any, component: KFunction<*>) =
	componentValue.javaClass.isEnum || component.javaClass.kotlin in noFlyWeightTypes