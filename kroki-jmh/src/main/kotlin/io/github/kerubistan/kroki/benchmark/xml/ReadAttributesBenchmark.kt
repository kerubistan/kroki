package io.github.kerubistan.kroki.benchmark.xml

import io.github.kerubistan.kroki.xml.readAttributes
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import java.io.Writer
import javax.xml.namespace.NamespaceContext
import javax.xml.namespace.QName
import javax.xml.stream.Location
import javax.xml.stream.events.Attribute
import javax.xml.stream.events.Characters
import javax.xml.stream.events.EndElement
import javax.xml.stream.events.Namespace
import javax.xml.stream.events.StartElement

@State(Scope.Benchmark)
open class ReadAttributesBenchmark {

	@Param("0", "1", "2", "4", "8", "16", "32", "64", "128")
	lateinit var attributes: String

	lateinit var element: StartElement

	@Setup
	fun setup() {
		class TestStartElementImpl(private val attributes: List<Attribute>) : StartElement {
			override fun getEventType(): Int {
				TODO("wont be implemented")
			}

			override fun getLocation(): Location {
				TODO("wont be implemented")
			}

			override fun isStartElement(): Boolean {
				TODO("wont be implemented")
			}

			override fun isAttribute(): Boolean {
				TODO("wont be implemented")
			}

			override fun isNamespace(): Boolean {
				TODO("wont be implemented")
			}

			override fun isEndElement(): Boolean {
				TODO("wont be implemented")
			}

			override fun isEntityReference(): Boolean {
				TODO("wont be implemented")
			}

			override fun isProcessingInstruction(): Boolean {
				TODO("wont be implemented")
			}

			override fun isCharacters(): Boolean {
				TODO("wont be implemented")
			}

			override fun isStartDocument(): Boolean {
				TODO("wont be implemented")
			}

			override fun isEndDocument(): Boolean {
				TODO("wont be implemented")
			}

			override fun asStartElement(): StartElement {
				TODO("wont be implemented")
			}

			override fun asEndElement(): EndElement {
				TODO("wont be implemented")
			}

			override fun asCharacters(): Characters {
				TODO("wont be implemented")
			}

			override fun getSchemaType(): QName {
				TODO("wont be implemented")
			}

			override fun writeAsEncodedUnicode(p0: Writer?) {
				TODO("wont be implemented")
			}

			override fun getName(): QName {
				TODO("wont be implemented")
			}

			override fun getAttributes(): Iterator<Attribute> = attributes.iterator()

			override fun getNamespaces(): Iterator<Namespace> {
				TODO("wont be implemented")
			}

			override fun getAttributeByName(p0: QName?): Attribute {
				TODO("wont be implemented")
			}

			override fun getNamespaceContext(): NamespaceContext {
				TODO("wont be implemented")
			}

			override fun getNamespaceURI(p0: String?): String {
				TODO("wont be implemented")
			}

		}

		class AttributeImpl(private val name: QName, private val value: String) : Attribute {
			override fun getEventType(): Int {
				TODO("not implemented")
			}

			override fun getLocation(): Location {
				TODO("not implemented")
			}

			override fun isStartElement(): Boolean {
				TODO("not implemented")
			}

			override fun isAttribute(): Boolean {
				TODO("not implemented")
			}

			override fun isNamespace(): Boolean {
				TODO("not implemented")
			}

			override fun isEndElement(): Boolean {
				TODO("not implemented")
			}

			override fun isEntityReference(): Boolean {
				TODO("not implemented")
			}

			override fun isProcessingInstruction(): Boolean {
				TODO("not implemented")
			}

			override fun isCharacters(): Boolean {
				TODO("not implemented")
			}

			override fun isStartDocument(): Boolean {
				TODO("not implemented")
			}

			override fun isEndDocument(): Boolean {
				TODO("not implemented")
			}

			override fun asStartElement(): StartElement {
				TODO("not implemented")
			}

			override fun asEndElement(): EndElement {
				TODO("not implemented")
			}

			override fun asCharacters(): Characters {
				TODO("not implemented")
			}

			override fun getSchemaType(): QName {
				TODO("not implemented")
			}

			override fun writeAsEncodedUnicode(p0: Writer?) {
				TODO("not implemented")
			}

			override fun getName(): QName = name

			override fun getValue(): String = value

			override fun getDTDType(): String {
				TODO("not implemented")
			}

			override fun isSpecified(): Boolean {
				TODO("not implemented")
			}
		}

		element = TestStartElementImpl((0..attributes.toInt()).map { nr ->
			AttributeImpl(
				QName.valueOf("att-$nr"),
				"value-$nr"
			)
		})
	}

	@Benchmark
	fun readAttributes(hole: Blackhole) {
		hole.consume(element.readAttributes())
	}

}