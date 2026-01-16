package io.github.kerubistan.kroki.xml

import io.github.kerubistan.kroki.iteration.toMap
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.io.Reader
import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.events.Attribute
import javax.xml.stream.events.EndElement
import javax.xml.stream.events.StartElement

/**
 * @suppress
 */
fun XmlBuilder.nothing() {
	// intentionally blank, used as default
}

interface XmlBuilder {
	fun tag(name: String, vararg atts: Pair<String, Any>, builder: XmlBuilder.() -> Unit)
	fun tag(name: String, vararg atts: Pair<String, Any>)
	fun cdata(data: String)
	fun text(reader: Reader)
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

/**
 * Build XML output using builders.
 * @sample io.github.kerubistan.kroki.xml.XmlTest.generation
 * @return an InputStream containing the XML
 */
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

interface XmlEventStreamParserBuilder {
	fun build(): XmlEventStreamReader
}

interface XmlEventStreamTagParserBuilder : XmlEventStreamParserBuilder {
	fun tag(name: String, builder: XmlEventStreamTagParserBuilder.() -> Unit)
	operator fun String.invoke(builder: XmlEventStreamTagParserBuilder.() -> Unit) {
		tag(name = this, builder = builder)
	}

	fun use(name: String, eventStream: XMLEventReader.(StartElement) -> Unit)
	operator fun String.minus(fn: (XMLEventReader.(StartElement) -> Unit)) {
		use(name = this, eventStream = fn)
	}
}

interface XmlEventStreamReader {
	fun read(events: XMLEventReader)
}

class UseTagXmlEventStreamReader(private val fn: XMLEventReader.(StartElement) -> Unit) : XmlEventStreamReader {
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
class SingleTagEventStreamReader(
	private val tag: String,
	private val reader: XmlEventStreamReader
) : XmlEventStreamReader {
	override fun read(events: XMLEventReader) {
		var depth = 1
		while (events.hasNext()) {
			when (val event = events.nextEvent()) {
				is StartElement -> {
					depth++
					if (event.name.localPart == tag) {
						reader.read(SubXMLEventReader(events, event))
					}
				}

				is EndElement -> {
					depth--
					if (depth == 0)
						return
				}
			}
		}
	}
}

/**
 * Delegates control to XmlEventStreamParser objects based on the tag name.
 * If there is only one, use SingleTagEventStreamParser.
 * @suppress
 */
class MultipleTagsEventStreamReader(private val tags: Map<String, XmlEventStreamReader>) : XmlEventStreamReader {
	override fun read(events: XMLEventReader) {
		var depth = 1
		while (events.hasNext()) {
			when (val event = events.nextEvent()) {
				is StartElement -> {
					val tagName = event.name.localPart
					depth++
					if (tagName in tags.keys) {
						tags.getValue(tagName)
							.read(SubXMLEventReader(events, event))
					}
				}

				is EndElement -> {
					depth--
					if (depth == 0)
						return
				}
			}
		}
	}
}

/**
 * @suppress
 */
internal class XmlEventStreamUseTagBuilderImpl(
	private val eventStream: XMLEventReader.(StartElement) -> Unit
) : XmlEventStreamParserBuilder {
	override fun build(): XmlEventStreamReader = UseTagXmlEventStreamReader(eventStream)
}

/**
 * @suppress
 */
internal class XmlEventStreamTagParserBuilderImpl : XmlEventStreamTagParserBuilder {

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
		when (tagMap.size) {
			0 -> NoOperationStreamReader
			1 -> {
				val tagName = tagMap.keys.single()
				SingleTagEventStreamReader(tagName, tagMap.getValue(tagName).build())
			}

			else -> MultipleTagsEventStreamReader(tagMap.mapValues { it.value.build() })
		}
}

val xmlInputFactory: XMLInputFactory = XMLInputFactory.newInstance()

fun OutputStream.xml(
	formatMode: FormatMode = FormatMode.COMPACT,
	root: String,
	vararg atts: Pair<String, Any>,
	builder: XmlBuilder.() -> Unit = XmlBuilder::nothing
): Unit = this.use {
	xml(
		formatMode = formatMode,
		root = root,
		atts = atts,
		out = this,
		builder = builder
	)
}

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
 * Read the input as XML and close when done.
 * @param builder the reader configuration
 */
inline fun Reader.useAsXmlEventStream(crossinline builder: XmlEventStreamTagParserBuilder.() -> Unit) {
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
 * Read the input as XML.
 * @param builder the reader configuration
 */
inline fun Reader.readAsXmlEventStream(crossinline builder: XmlEventStreamTagParserBuilder.() -> Unit) {
	buildXmlEventStreamReader(builder).read(xmlInputFactory.createXMLEventReader(this))
}

/**
 * Read the string as XML.
 * @param builder the reader configuration
 */
inline fun String.readAsXmlEventStream(crossinline builder: XmlEventStreamTagParserBuilder.() -> Unit) {
	buildXmlEventStreamReader(builder).read(xmlInputFactory.createXMLEventReader(this.reader()))
}

/**
 * Read the input as XML and close when done.
 * @param reader the reader
 */
fun InputStream.useAsXmlEventStream(reader: XmlEventStreamReader) {
	this.use { it.readAsXmlEventStream(reader) }
}

/**
 * Read the input as XML with a pre-build reader.
 * @param reader the reader
 */
fun InputStream.readAsXmlEventStream(reader: XmlEventStreamReader) {
	reader.read(xmlInputFactory.createXMLEventReader(this))
}

/**
 * Create a new XmlEventStreamTagParserBuilder.
 * This is only to hide the implementation.
 */
fun parserBuilder(): XmlEventStreamTagParserBuilder = XmlEventStreamTagParserBuilderImpl()

/**
 * Build a reader for XML input.
 * @param builder the reader configuration
 */
inline fun buildXmlEventStreamReader(crossinline builder: XmlEventStreamTagParserBuilder.() -> Unit) =
	parserBuilder().apply(builder).build()

fun XMLEventReader.readAsXmlEventStream(reader: XmlEventStreamReader) {
	reader.read(this)
}

inline fun XMLEventReader.readAsXmlEventStream(crossinline builder: XmlEventStreamTagParserBuilder.() -> Unit) {
	buildXmlEventStreamReader(builder).read(this)
}

/**
 * Reads all attributes of the StartElement into a map.
 * Once this method is called, it will only return empty
 * results for any subsequent calls.
 */
fun StartElement.readAttributes(): Map<String, String> =
	this.attributes.toMap { (this as Attribute).run { name.localPart to value } }
