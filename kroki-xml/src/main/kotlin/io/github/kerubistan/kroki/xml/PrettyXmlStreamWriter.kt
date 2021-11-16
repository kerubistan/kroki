package io.github.kerubistan.kroki.xml

import javax.xml.namespace.NamespaceContext
import javax.xml.stream.XMLStreamWriter

/**
 * @suppress
 */
class PrettyXmlStreamWriter(
	private val xmlStreamWriter: XMLStreamWriter,
	private val indent: String = "\t"
) : XMLStreamWriter {

	private var depth = 0

	override fun writeCData(data: String) {
		indent()
		xmlStreamWriter.writeCData(data)
	}

	override fun writeEndDocument() {
		depth = 0
		xmlStreamWriter.writeEndDocument()
	}

	override fun writeComment(comment: String) {
		indent()
		xmlStreamWriter.writeComment(comment)
	}

	override fun flush() {
		xmlStreamWriter.flush()
	}

	override fun setNamespaceContext(namespaceContext: NamespaceContext) {
		xmlStreamWriter.namespaceContext = namespaceContext
	}

	override fun writeStartElement(name: String) {
		indent()
		depth++
		xmlStreamWriter.writeStartElement(name)
	}

	override fun writeStartElement(name: String, nameSpace: String?) {
		indent()
		depth++
		xmlStreamWriter.writeStartElement(name, nameSpace)
	}

	override fun writeStartElement(name: String, nameSpace: String, localName: String) {
		indent()
		depth++
		xmlStreamWriter.writeStartElement(name, nameSpace, localName)
	}

	override fun writeDTD(dtd: String) =
		xmlStreamWriter.writeDTD(dtd)

	override fun getProperty(name: String): Any = xmlStreamWriter.getProperty(name)

	override fun setDefaultNamespace(uri: String) {
		xmlStreamWriter.setDefaultNamespace(uri)
	}

	override fun writeNamespace(prefix: String, namespaceURI: String) {
		indent()
		xmlStreamWriter.writeNamespace(prefix, namespaceURI)
	}

	override fun writeEndElement() {
		depth--
		indent()
		xmlStreamWriter.writeEndElement()
	}

	override fun writeAttribute(localName: String, value: String) {
		xmlStreamWriter.writeAttribute(localName, value)
	}

	override fun writeAttribute(prefix: String, namespaceURI: String, localName: String, value: String) {
		xmlStreamWriter.writeAttribute(prefix, namespaceURI, localName, value)
	}

	override fun writeAttribute(namespaceURI: String, localName: String, value: String) {
		xmlStreamWriter.writeAttribute(namespaceURI, localName, value)
	}

	override fun close() {
		xmlStreamWriter.close()
	}

	override fun writeDefaultNamespace(namespaceURI: String) {
		xmlStreamWriter.writeDefaultNamespace(namespaceURI)
	}

	override fun writeEmptyElement(namespaceURI: String, localName: String) {
		indent()
		xmlStreamWriter.writeEmptyElement(namespaceURI, localName)
	}

	override fun writeEmptyElement(prefix: String, localName: String, namespaceURI: String) {
		indent()
		xmlStreamWriter.writeEmptyElement(prefix, localName, namespaceURI)
	}

	override fun writeEmptyElement(localName: String) {
		indent()
		xmlStreamWriter.writeEmptyElement(localName)
	}

	override fun writeCharacters(text: String) {
		xmlStreamWriter.writeCharacters(text)
	}

	override fun writeCharacters(text: CharArray, start: Int, len: Int) {
		xmlStreamWriter.writeCharacters(text, start, len)
	}

	override fun writeStartDocument() {
		xmlStreamWriter.writeStartDocument()
	}

	override fun writeStartDocument(version: String) {
		xmlStreamWriter.writeStartDocument(version)
	}

	override fun writeStartDocument(encoding: String, version: String) {
		xmlStreamWriter.writeStartDocument(encoding, version)
	}

	override fun getNamespaceContext(): NamespaceContext = xmlStreamWriter.namespaceContext

	override fun writeEntityRef(name: String) {
		indent()
		xmlStreamWriter.writeEntityRef(name)
	}

	override fun setPrefix(prefix: String, uri: String) {
		xmlStreamWriter.setPrefix(prefix, uri)
	}

	override fun getPrefix(uri: String): String = xmlStreamWriter.getPrefix(uri)

	override fun writeProcessingInstruction(target: String) {
		xmlStreamWriter.writeProcessingInstruction(target)
	}

	override fun writeProcessingInstruction(target: String, data: String) {
		xmlStreamWriter.writeProcessingInstruction(target, data)
	}

	private fun indent() {
		xmlStreamWriter.apply {
			writeCharacters("\n")
			repeat(depth) { writeCharacters(indent) }
		}
	}

}