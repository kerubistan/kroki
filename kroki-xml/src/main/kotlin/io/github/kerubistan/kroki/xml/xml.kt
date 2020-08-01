package io.github.kerubistan.kroki.xml

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import kotlin.reflect.KFunction0

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

interface XmlParserBuilder<T> {
	/**
	 * Registers a tag
	 * @param name the name of the tag
	 * @param selector
	 */
	fun tag(name: String,
			selector : (name: String, atts : List<Pair<String, String>>) -> Boolean = { _, _ -> true },
			processor : XmlParserBuilder<T>.() -> Unit
	)
	fun text() : String
	fun int() : Int = text().toInt()
	fun <T> yield(result : T) : T
	operator fun String.div(s: String): Any {
		TODO("not implemented")
	}
	operator fun Any.div(ref: KFunction0<T>): String {
		TODO("not implemented")
	}
	operator fun String.invoke(function: () -> T) : T {
		tag(this@invoke) {
			function()
		}
		TODO("not implemented")
	}
	operator fun String.invoke(vararg atts: Pair<String, Any>) : String {
		TODO("not implemented")
	}
	operator fun String.div(kFunction0: KFunction0<Int>): Int {
		TODO()
	}
	operator fun div(s: String): String {
		TODO("not implemented")
	}
	operator fun String.div(function: () -> T): String {
		TODO("not implemented")
	}
	operator fun <E, C : Collection<E>> String.times(function: () -> E): C {
		TODO("not implemented")
	}
	operator fun String.unaryMinus(): String {
		TODO("not implemented")
	}
}

inline fun <T> String.parseAsXml(crossinline builder: XmlParserBuilder<T>.() -> Unit) : T =
	ByteArrayInputStream(this.toByteArray()).parseAsXml(builder)

/**
 * Parses the stream as XML and returns the data extracted.
 * @param builder builds the data extractor
 */
inline fun <T> InputStream.parseAsXml(builder: XmlParserBuilder<T>.() -> Unit) : T =
	StaxXmlParserBuilder<T>(this).let {
		it.builder()
		it.read()
	}
