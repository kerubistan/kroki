package io.github.kerubistan.kroki.flyweight

import io.github.kerubistan.kroki.flyweight.annotations.IgnoreFlyWeight
import kotlin.reflect.KFunction

/**
 * Creates a de-duplicated copy of the object, which will be equal (by value, as == in kotlin) to the original.
 */
@Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
fun <T : Any> T.flyWeight(instanceCache: InstanceCache = GlobalInstanceCache): T =
	when {
		this is String ->
			this.intern()
		this.javaClass.kotlin.isData ->
			flyWeightDataInstance(instanceCache)
		this is Set<*> ->
			this.map { it?.flyWeight(instanceCache) }.toSet()
		this is List<*> -> {
			this.map { it?.flyWeight(instanceCache) }
		}
		this is Map<*, *> -> {
			this.map { (key, value) ->
				key?.flyWeight(instanceCache) to value?.flyWeight(instanceCache)
			}.toMap()
		}
		else -> this
	} as T

/**
 * Makes a copy of an instance of kotlin data class, de-duplicating each property.
 */
@Suppress("UNCHECKED_CAST")
private fun <T : Any> T.flyWeightDataInstance(instanceCache: InstanceCache): T {
	val memberFunctions = this.javaClass.kotlin.members.filterIsInstance<KFunction<*>>()
	val copy = memberFunctions.single { it.name == "copy" }
	val deDuplicatedFields = memberFunctions
		.filter { it.name.startsWith("component") }
		.sortedBy { it.name.substringAfter("component").toInt() }
		.mapIndexed { index, component ->
			var componentValue = component.call(this)
			copy.parameters[index + 1] to if (componentValue != null) {
				if (noFlyWeightType(componentValue)) {
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
	return copy.callBy(deDuplicatedFields) as T
}

/**
 * Classes that should not be considered to be de-duplicated, either because they have too few possible values
 * or they are de-duplicated by the runtime (such as Integer.valueOf() caches a number of instances for the first some).
 */
private val noFlyWeightTypes = setOf(
	Boolean::class,
	Short::class,
	Byte::class,
	Char::class,
	Int::class,
	Long::class
)

/**
 * Checks if the value should be ignored by de-duplication logic.
 */
private fun noFlyWeightType(componentValue: Any) =
	componentValue.javaClass.isEnum
			|| componentValue.javaClass.kotlin in noFlyWeightTypes
			|| componentValue.javaClass.annotations.any { it is IgnoreFlyWeight }
