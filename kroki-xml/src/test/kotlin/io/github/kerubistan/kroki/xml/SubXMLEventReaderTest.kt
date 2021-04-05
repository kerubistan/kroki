package io.github.kerubistan.kroki.xml

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test

import org.junit.Assert.*
import javax.xml.namespace.QName
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
		val qName = mock<QName>().apply {
			whenever(this.localPart).thenReturn("start")
		}
		val startElement = mock<StartElement>().apply {
			whenever(this.name).thenReturn(qName)
		}
		val subEventReader = SubXMLEventReader(eventReader, startElement)

		val result = buildString {
			while (subEventReader.hasNext()) {
				val subEvent = subEventReader.next()
				when (subEvent) {
					is StartElement -> appendLine("-> ${subEvent.name.localPart}")
					is EndElement -> appendLine("<- ${subEvent.name.localPart}")
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