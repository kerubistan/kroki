package io.github.kerubistan.kroki.xml

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.events.StartElement
import kotlin.reflect.KFunction0

fun XmlBuilder.nothing() {
	// intentionally blank, used as default
}

interface XmlBuilder {
	fun tag(name: String, vararg atts: Pair<String, Any>, builder: XmlBuilder.() -> Unit)
	fun tag(name: String, vararg atts: Pair<String, Any>)
	fun cdata(data: String)
	fun text(builder: StringBuilder.() -> Unit)
	fun text(value: String)
	fun text(value: Any?) = text(value?.toString() ?: "null")
	fun comment(value: String)
	operator fun Any?.unaryMinus() = text(this?.toString() ?: "null")
	operator fun String.not() = comment(this)
	operator fun String.invoke(vararg atts: Pair<String, Any>): String {
		tag(this, *atts)
		return this
	}

	operator fun String.invoke(vararg atts: Pair<String, Any>, builder: XmlBuilder.() -> Unit): String {
		tag(this, *atts, builder = builder)
		return this
	}
}

fun xml(
	formatMode: FormatMode = FormatMode.COMPACT,
	root: String,
	vararg atts: Pair<String, Any>,
	builder: XmlBuilder.() -> Unit = XmlBuilder::nothing
): InputStream = ByteArrayOutputStream().use {
	StaxXmlBuilder(it, formatMode).use { xmlBuilder ->
		xmlBuilder.tag(root, *atts) { builder() }
	}
	ByteArrayInputStream(it.toByteArray())
}

fun xml(
	formatMode: FormatMode = FormatMode.COMPACT,
	root: String,
	vararg atts: Pair<String, Any>,
	out: OutputStream,
	builder: XmlBuilder.() -> Unit = XmlBuilder::nothing
) {
	StaxXmlBuilder(out, formatMode).use { xmlBuilder ->
		xmlBuilder.tag(root, *atts) { builder() }
	}
}

interface XmlParserBuilder<T> {
	/**
	 * Registers a tag
	 * @param name the name of the tag
	 * @param selector
	 */
	fun tag(
		name: String,
		selector: (name: String, atts: List<Pair<String, String>>) -> Boolean = { _, _ -> true },
		processor: XmlParserBuilder<T>.() -> Unit
	)

	fun text(): String
	fun int(): Int = text().toInt()
	fun <T> yield(result: T): T
	operator fun String.div(s: String): Any {
		TODO("not implemented")
	}

	operator fun Any.div(ref: KFunction0<T>): String {
		TODO("not implemented")
	}

	operator fun String.invoke(function: () -> T): T {
		tag(this@invoke) {
			function()
		}
		TODO("not implemented")
	}

	operator fun String.invoke(vararg atts: Pair<String, Any>): String {
		TODO("not implemented")
	}

	operator fun String.div(kFunction0: KFunction0<Int>): Int {
		TODO()
	}

	operator fun div(s: String): String {
		TODO("not implemented")
	}

	operator fun String.div(function: () -> T): String {
		TODO("not implemented")
	}

	operator fun <E, C : Collection<E>> String.times(function: () -> E): C {
		TODO("not implemented")
	}

	operator fun String.unaryMinus(): String {
		TODO("not implemented")
	}
}

inline fun <T> String.parseAsXml(crossinline builder: XmlParserBuilder<T>.() -> Unit): T =
	ByteArrayInputStream(this.toByteArray()).parseAsXml(builder)

/**
 * Parses the stream as XML and returns the data extracted.
 * @param builder builds the data extractor
 */
inline fun <T> InputStream.parseAsXml(builder: XmlParserBuilder<T>.() -> Unit): T =
	StaxXmlParserBuilder<T>(this).let {
		it.builder()
		it.read()
	}

interface XmlEventStreamParserBuilder {
	fun build() : XmlEventStreamReader
}

interface XmlEventStreamTagParserBuilder: XmlEventStreamParserBuilder {
	fun tag(name: String, builder: XmlEventStreamTagParserBuilder.() -> Unit)
	operator fun String.invoke(builder: XmlEventStreamTagParserBuilder.() -> Unit) {
		tag(name = this, builder = builder)
	}
	fun use(name : String, eventStream: XMLEventReader.(StartElement) -> Unit)
	operator fun String.minus(fn : (XMLEventReader.(StartElement) -> Unit)) {
		use(name = this, eventStream = fn)
	}
}

interface XmlEventStreamReader {
	fun read(events: XMLEventReader)
}

class UseTagXmlEventStreamReader(private val fn : XMLEventReader.(StartElement) -> Unit) : XmlEventStreamReader {
	override fun read(events: XMLEventReader) {
		events.fn((events as SubXMLEventReader).startElement)
	}
}

/**
 * Only skips through the events until the end.
 */
object NoOperationStreamReader : XmlEventStreamReader {
	override fun read(events: XMLEventReader) {
		while (events.hasNext()) {
			events.nextEvent()
		}
	}
}

/**
 * Delegates control to a single configured event parser, ignores all other.
 * Separate from MultipleTagsEventStreamParser for performance reason.
 */
class SingleTagEventStreamReader(private val tag : String, private val reader : XmlEventStreamReader) : XmlEventStreamReader {
	override fun read(events: XMLEventReader) {
		while (events.hasNext()) {
			val event = events.nextEvent()
			if(event is StartElement && event.name.localPart == tag) {
				reader.read(SubXMLEventReader(events, event))
			}
		}
	}
}

/**
 * Delegates control to XmlEventStreamParser objects based on the tag name.
 * If there is only one, use SingleTagEventStreamParser.
 */
class MultipleTagsEventStreamReader(private val tags : Map<String, XmlEventStreamReader>) : XmlEventStreamReader {
	override fun read(events: XMLEventReader) {
		while (events.hasNext()) {
			val event = events.nextEvent()
			if(event is StartElement && event.name.localPart in tags.keys) {
				tags.getValue(event.name.localPart)
					.read(SubXMLEventReader(events, event))
			}
		}
	}
}

class XmlEventStreamUseTagBuilderImpl(private val eventStream: XMLEventReader.(StartElement) -> Unit) : XmlEventStreamParserBuilder {
	override fun build(): XmlEventStreamReader = UseTagXmlEventStreamReader(eventStream)
}

class XmlEventStreamTagParserBuilderImpl : XmlEventStreamTagParserBuilder {

	private val tagMap = mutableMapOf<String, XmlEventStreamParserBuilder>()

	override fun tag(name: String, builder: XmlEventStreamTagParserBuilder.() -> Unit) {
		require(!tagMap.containsKey(name)) {
			"tag $name already defined"
		}
		val sub = XmlEventStreamTagParserBuilderImpl()
		sub.builder()
		tagMap[name] = sub
	}

	override fun use(name: String, eventStream: XMLEventReader.(StartElement) -> Unit) {
		require(!tagMap.containsKey(name)) {
			"tag $name already defined"
		}
		tagMap[name] = XmlEventStreamUseTagBuilderImpl(eventStream)
	}

	override fun build(): XmlEventStreamReader =
		when(tagMap.size) {
			0 -> NoOperationStreamReader
			1 -> {
				val tagName = tagMap.keys.single()
				SingleTagEventStreamReader(tagName, tagMap.getValue(tagName).build())
			}
			else -> MultipleTagsEventStreamReader( tagMap.mapValues { it.value.build() } )
		}
}

val xmlInputFactory: XMLInputFactory = XMLInputFactory.newInstance()

/**
 * Read the input as XML and close when done.
 * @param builder the reader configuration
 */
inline fun InputStream.useAsXmlEventStream(crossinline builder: XmlEventStreamTagParserBuilder.() -> Unit) {
	this.use {
		it.readAsXmlEventStream(builder)
	}
}

/**
 * Read the input as XML.
 * @param builder the reader configuration
 */
inline fun InputStream.readAsXmlEventStream(crossinline builder: XmlEventStreamTagParserBuilder.() -> Unit) {
	buildXmlEventStreamReader(builder).read(xmlInputFactory.createXMLEventReader(this))
}

/**
 * Read the input as XML and close when done.
 * @param reader the reader
 */
fun InputStream.useAsXmlEventStream(reader : XmlEventStreamReader) {
	this.use { it.readAsXmlEventStream(reader) }
}

/**
 * Read the input as XML with a pre-build reader.
 * @param reader the reader
 */
fun InputStream.readAsXmlEventStream(reader : XmlEventStreamReader) {
	reader.read(xmlInputFactory.createXMLEventReader(this))
}

/**
 * Build a reader for XML input.
 * @param builder the reader configuration
 */
inline fun buildXmlEventStreamReader(crossinline builder: XmlEventStreamTagParserBuilder.() -> Unit) =
	XmlEventStreamTagParserBuilderImpl().apply(builder).build()

fun XMLEventReader.readAsXmlEventStream(reader : XmlEventStreamReader) {
	reader.read(this)
}

inline fun XMLEventReader.readAsXmlEventStream(crossinline builder: XmlEventStreamTagParserBuilder.() -> Unit) {
	buildXmlEventStreamReader(builder).read(this)
}