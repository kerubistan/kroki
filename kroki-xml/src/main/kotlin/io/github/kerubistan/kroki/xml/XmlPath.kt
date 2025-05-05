package io.github.kerubistan.kroki.xml

import java.io.InputStream
import java.io.Reader

class XmlPath(private val reader: XmlEventStreamReader) {

	companion object {
		fun of(expression : String) : XmlPath {
			XmlPath(buildXmlEventStreamReader {
				expression.split("/+".toRegex()).filterNot { it.isBlank() }.forEach {

				}
			})
			TODO()
		}
	}
	fun evaluate(input : InputStream) : String {
		input.useAsXmlEventStream(reader)
		TODO()
	}
	fun evaluate(input : String) : String = evaluate(input.reader())
	fun evaluate(input : ByteArray) : String  = evaluate(input.inputStream())
	fun evaluate(input : Reader) : String {

		TODO()
	}

}