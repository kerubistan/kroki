package io.github.kerubistan.kroki.xml

import java.io.InputStream
import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.events.*

class StaxXmlParserBuilder<T>(input: InputStream) : XmlParserBuilder<T> {

	companion object {
		private val factory = XMLInputFactory.newFactory()
	}

	private val xml : XMLEventReader = factory.createXMLEventReader(input)

	override fun tag(
		name: String,
		selector: (name: String, atts: List<Pair<String, String>>) -> Boolean,
		processor: XmlParserBuilder<T>.() -> Unit
	) {
		TODO("not implemented")
	}

	override fun text(): String {
		TODO("not implemented")
	}

	override fun <T> yield(result: T) : T {
		TODO("not implemented")
	}

	fun read() : T {
		while(xml.hasNext()) {
			xml.next()
			val event = xml.nextEvent()
			when(event) {
				is StartElement -> {
					TODO()
				}
				is EndElement -> TODO()
				is StartDocument -> TODO()
				is EndDocument -> TODO()
				is Characters -> TODO()
				is Comment -> TODO()
				else -> {
					// ignored events, such as DTD, EntityReference, EntityDeclaration
				}
			}
		}
		TODO()
	}

}