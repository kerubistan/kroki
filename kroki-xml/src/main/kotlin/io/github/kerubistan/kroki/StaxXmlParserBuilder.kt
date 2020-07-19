package io.github.kerubistan.kroki

import java.io.InputStream
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamReader

class StaxXmlParserBuilder<T>(input: InputStream) : XmlParserBuilder<T> {


	companion object {
		private val factory = XMLInputFactory.newFactory()
	}

	private val xml : XMLStreamReader = factory.createXMLStreamReader(input)

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
		TODO()
	}

}