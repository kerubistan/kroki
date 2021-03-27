package io.github.kerubistan.kroki.json.stream

import java.io.InputStream

interface JsonEventStreamReader {
	fun read(events: Any)
}

internal class JsonEventStreamReaderImpl :JsonEventStreamReader {
	override fun read(events: Any) {
		TODO("not implemented")
	}
}

interface JsonEventStreamTagParserBuilder {
	val root : JsonEventStreamTagParserBuilder
	val `$`
			get() = root
	operator fun JsonEventStreamTagParserBuilder.invoke(builder: JsonEventStreamTagParserBuilder.() -> Unit) : Unit = TODO()
	operator fun (() -> Unit).unaryMinus() : Unit = TODO()
	operator fun String.invoke(builder: JsonEventStreamTagParserBuilder.() -> Unit ) : Unit = TODO()
	fun build(): JsonEventStreamReader
}

class JsonEventStreamTagParserBuilderImpl : JsonEventStreamTagParserBuilder {
	override val root: JsonEventStreamTagParserBuilder
		get() = TODO("not implemented")

	override fun build(): JsonEventStreamReader {
		TODO("not implemented")
	}
}

/**
 * Build a reader for XML input.
 * @param builder the reader configuration
 */
inline fun buildJsonEventStreamReader(builder: JsonEventStreamTagParserBuilder.() -> Unit) =
	JsonEventStreamTagParserBuilderImpl().apply(builder).build()

/**
 * Read the input as XML.
 * @param builder the reader configuration
 */
inline fun InputStream.readAsJsonEventStream(crossinline builder: JsonEventStreamTagParserBuilder.() -> Unit) {
	buildJsonEventStreamReader(builder).read(this)
}
