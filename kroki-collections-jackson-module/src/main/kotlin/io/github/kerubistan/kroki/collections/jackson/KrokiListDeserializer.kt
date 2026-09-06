package io.github.kerubistan.kroki.collections.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

class KrokiListDeserializer : JsonDeserializer<List<*>>() {
	override fun deserialize(p: JsonParser, ctxt: DeserializationContext): List<*> {
		ctxt.contextualType
		TODO("not implemented")
	}
}