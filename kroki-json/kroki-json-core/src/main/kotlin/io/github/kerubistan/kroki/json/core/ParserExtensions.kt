package io.github.kerubistan.kroki.json.core

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken

internal class JsonTokenIterator(private val parser : JsonParser) : Iterator<JsonToken> {
	override fun hasNext(): Boolean = parser.nextToken()

	override fun next(): JsonToken {
		TODO("not implemented")
	}

}

fun JsonParser.iterator() = JsonTokenIterator(this)