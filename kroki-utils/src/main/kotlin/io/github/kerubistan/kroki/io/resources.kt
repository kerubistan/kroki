package io.github.kerubistan.kroki.io

import java.io.Reader
import java.nio.charset.Charset

fun Reader.readText(): String = EasyStringWriter(1024).use {
	copyTo(it)
	it.toString()
}

/**
 * Opens a resource file located on the classpath.
 * @param resource the location of the resource file
 */
fun resource(resource: String) =
	requireNotNull(
		Thread.currentThread()
			.contextClassLoader
			.getResource(resource)
	) { "$resource not found" }
		.openStream()!!

/**
 * Reads a resource file into a string
 * @param resource the path to the resource file
 * @param charset the character set to use for reading the resource file - default us UTF-8
 */
fun resourceToString(resource: String, charset: Charset = Charsets.UTF_8) =
	resource(resource)
		.use { it.reader(charset).readText() }