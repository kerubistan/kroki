package io.github.kerubistan.kroki.xml

import java.io.Closeable
import java.io.OutputStream
import java.io.Reader
import javax.xml.stream.XMLOutputFactory
import javax.xml.stream.XMLStreamWriter

/**
 * @suppress
 */
class StaxXmlBuilder(outputStream: OutputStream, formatMode: FormatMode = FormatMode.COMPACT) : XmlBuilder, Closeable {

	private val xml: XMLStreamWriter = when (formatMode) {
		FormatMode.COMPACT -> xmlOutputFactory.createXMLStreamWriter(outputStream)
		FormatMode.PRETTY_TABS -> PrettyXmlStreamWriter(xmlOutputFactory.createXMLStreamWriter(outputStream))
		FormatMode.PRETTY_BIG_SPACE_NAZI -> {
			PrettyXmlStreamWriter(
				xmlOutputFactory.createXMLStreamWriter(outputStream),
				FOUR_SPACES
			)
		}

		FormatMode.PRETTY_SMALL_SPACE_NAZI -> PrettyXmlStreamWriter(
			xmlOutputFactory.createXMLStreamWriter(outputStream),
			TWO_SPACES
		)
	}

	companion object {
		private val xmlOutputFactory = XMLOutputFactory.newInstance()
		const val FOUR_SPACES = "    "
		const val TWO_SPACES = "  "
	}

	override fun tag(name: String, vararg atts: Pair<String, Any>, builder: XmlBuilder.() -> Unit) {
		xml.writeStartElement(name)
		writeAttributes(atts)
		this.builder()
		xml.writeEndElement()
	}

	private fun writeAttributes(atts: Array<out Pair<String, Any>>) {
		atts.forEach {
			xml.writeAttribute(it.first, it.second.toString())
		}
	}

	override fun tag(name: String, vararg atts: Pair<String, Any>) {
		xml.writeEmptyElement(name)
		writeAttributes(atts)
	}

	override fun cdata(data: String) {
		xml.writeCData(data)
	}

	override fun text(reader: Reader) {
		val buffer = CharArray(4096)
		var characters = reader.read(buffer)
		while (characters != -1) {
			xml.writeCharacters(buffer, 0, characters)
			characters = reader.read(buffer)
		}
	}

	override fun text(builder: StringBuilder.() -> Unit) {
		xml.writeCharacters(buildString(builder))
	}

	override fun text(value: String) {
		xml.writeCharacters(value)
	}

	override fun comment(value: String) {
		xml.writeComment(value)
	}

	override fun close() {
		xml.close()
	}

}