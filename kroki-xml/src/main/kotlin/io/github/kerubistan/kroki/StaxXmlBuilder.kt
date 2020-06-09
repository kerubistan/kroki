package io.github.kerubistan.kroki

import java.io.OutputStream
import java.lang.StringBuilder

import javax.xml.stream.XMLOutputFactory
import javax.xml.stream.XMLStreamWriter

class StaxXmlBuilder(outputStream: OutputStream) : XmlBuilder {

    private val xml : XMLStreamWriter

    companion object {
        private val xmlOutputFactory = XMLOutputFactory.newInstance()
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

    override fun cdata(data : String) {
        xml.writeCData(data)
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

    init {
        xml = xmlOutputFactory.createXMLStreamWriter(outputStream)
    }

}