package io.github.kerubistan.kroki.xml

import org.junit.Test

import org.junit.Assert.*
import javax.xml.stream.events.EndElement
import javax.xml.stream.events.StartElement

class SubXMLEventReaderTest {

	@Test
	operator fun next() {
		val eventReader = xmlInputFactory.createXMLEventReader(
			"""
			<xml>
				<ignore/>
				<start>
					<tag>text</tag>
				</start>
			</xml>
			""".trimIndent().byteInputStream()
		)
		var event = eventReader.next()
		while (eventReader.hasNext() && !(event is StartElement && event.name.localPart == "start")) {
			event = eventReader.nextEvent()
		}
		val subEventReader = SubXMLEventReader(eventReader, "start")

		val result = buildString {
			while (subEventReader.hasNext()) {
				val subEvent = subEventReader.next()
				when (subEvent) {
					is StartElement -> appendln("-> ${subEvent.name.localPart}")
					is EndElement -> appendln("<- ${subEvent.name.localPart}")
				}
			}
		}
		assertEquals("""
			-> tag
			<- tag
			<- start
		""".trimIndent().trim(), result.trim())

	}

}