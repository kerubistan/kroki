package io.github.kerubistan.kroki

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

interface XmlBuilder {
    fun tag(name : String, vararg atts : Pair<String, Any>, builder : XmlBuilder.() -> Unit = {})
    fun cdata(data : String)
    fun text(builder : StringBuilder.() -> Unit)
    fun text(value: String)
    fun comment(value: String)
    operator fun String.unaryMinus() = text(this)
    operator fun String.unaryPlus() = tag(this)
    operator fun String.not() = comment(this)
}

fun xml(root : String, builder : XmlBuilder.() -> Unit) : InputStream = ByteArrayOutputStream().use {
    StaxXmlBuilder(it).apply {
        tag(root) { builder() }
    }
    ByteArrayInputStream(it.toByteArray())
}