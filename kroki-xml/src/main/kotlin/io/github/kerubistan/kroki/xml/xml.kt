package io.github.kerubistan.kroki.xml

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

fun XmlBuilder.nothing() {
	// intentionally blank, used as default
}

interface XmlBuilder {
	fun tag(name: String, vararg atts: Pair<String, Any>, builder: XmlBuilder.() -> Unit)
	fun tag(name: String, vararg atts: Pair<String, Any>)
	fun cdata(data: String)
	fun text(builder: StringBuilder.() -> Unit)
	fun text(value: String)
	fun text(value: Any?) = text(value?.toString() ?: "null")
	fun comment(value: String)
	operator fun Any?.unaryMinus() = text(this?.toString() ?: "null")
	operator fun String.not() = comment(this)
	operator fun String.invoke(vararg atts: Pair<String, Any>): String {
		tag(this, *atts)
		return this
	}

	operator fun String.invoke(vararg atts: Pair<String, Any>, builder: XmlBuilder.() -> Unit): String {
		tag(this, *atts, builder = builder)
		return this
	}
}

fun xml(
	formatMode: FormatMode = FormatMode.COMPACT,
	root: String,
	vararg atts: Pair<String, Any>,
	builder: XmlBuilder.() -> Unit = XmlBuilder::nothing
): InputStream = ByteArrayOutputStream().use {
	StaxXmlBuilder(it, formatMode).use { xmlBuilder ->
		xmlBuilder.tag(root, *atts) { builder() }
	}
	ByteArrayInputStream(it.toByteArray())
}

fun xml(
	formatMode: FormatMode = FormatMode.COMPACT,
	root: String,
	vararg atts: Pair<String, Any>,
	out: OutputStream,
	builder: XmlBuilder.() -> Unit = XmlBuilder::nothing
) {
	StaxXmlBuilder(out, formatMode).use { xmlBuilder ->
		xmlBuilder.tag(root, *atts) { builder() }
	}
}