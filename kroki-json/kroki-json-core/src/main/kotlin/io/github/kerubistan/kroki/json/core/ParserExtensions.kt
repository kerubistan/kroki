package io.github.kerubistan.kroki.json.core

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken

internal class JsonTokenIterator(private val parser: JsonParser) : Iterator<JsonToken> {

	private var nextToken: JsonToken? = null

	init {
		nextToken = parser.nextToken()
	}

	override fun hasNext(): Boolean = nextToken != null

	override fun next(): JsonToken {
		if (nextToken == null) {
			throw IllegalStateException("there is no next element")
		} else {
			val temp: JsonToken = nextToken!!
			nextToken = parser.nextToken()
			return temp
		}
	}

}

internal fun JsonParser.iterator() = JsonTokenIterator(this)