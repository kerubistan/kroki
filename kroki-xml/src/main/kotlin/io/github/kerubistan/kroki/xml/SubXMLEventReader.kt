package io.github.kerubistan.kroki.xml

import javax.xml.stream.XMLEventReader
import javax.xml.stream.events.EndElement
import javax.xml.stream.events.StartElement
import javax.xml.stream.events.XMLEvent

/**
 * Allows the client code to read XML events of XML stream only in a certain tag of the
 * whole XML stream. Once the closing tag is reached, the reader object will respond as
 * if no more events available.
 * @suppress
 */
class SubXMLEventReader(private val original: XMLEventReader, val startElement: StartElement) : XMLEventReader {

	private var open = true
	private var depth = 0
	private val closeTag = startElement.name.localPart

	private fun checkOpen() {
		check(open) { "Event reader closed already" }
	}

	override fun nextEvent(): XMLEvent {
		checkOpen()
		val event = original.nextEvent()
		followEvent(event)
		return event
	}

	private fun followEvent(event: Any?) {
		when (event) {
			is EndElement -> {
				depth--
				if (depth < 0 && event.name.localPart == closeTag) {
					open = false
				}
			}
			is StartElement -> {
				depth++
			}
		}
	}

	override fun remove() {
		checkOpen()
		original.remove()
	}

	override fun getElementText(): String {
		checkOpen()
		return original.elementText
	}

	override fun getProperty(prop: String?): Any {
		checkOpen()
		return original.getProperty(prop)
	}

	override fun next(): Any? {
		checkOpen()
		val next = original.next()
		followEvent(next)
		return next
	}

	override fun peek(): XMLEvent {
		checkOpen()
		return original.peek()
	}

	override fun hasNext(): Boolean = open && original.hasNext()

	override fun close() {
		open = false
	}

	override fun nextTag(): XMLEvent {
		val event = original.nextTag()
		followEvent(event)
		return event
	}
}