package io.github.kerubistan.kroki.io

import java.io.Reader
import java.nio.charset.Charset

fun Reader.readText(): String = EasyStringWriter(1024).use {
    copyTo(it)
    it.toString()
}

fun resource(resource: String) =
    requireNotNull(
        Thread.currentThread()
            .contextClassLoader
            .getResource(resource)
    ) { "$resource not found" }
        .openStream()!!

fun resourceToString(resource: String, charset: Charset = Charsets.UTF_8) =
    resource(resource)
        .use { it.reader(charset).readText() }