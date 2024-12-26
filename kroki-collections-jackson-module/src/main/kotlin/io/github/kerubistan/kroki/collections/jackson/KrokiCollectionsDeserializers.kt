package io.github.kerubistan.kroki.collections.jackson

import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.type.CollectionType

class KrokiCollectionsDeserializers : SimpleDeserializers() {
	override fun findCollectionDeserializer(
		type: CollectionType?,
		config: DeserializationConfig?,
		beanDesc: BeanDescription?,
		elementTypeDeserializer: TypeDeserializer?,
		elementDeserializer: JsonDeserializer<*>?
	): JsonDeserializer<*> {
		return KrokiListDeserializer()
	}
}