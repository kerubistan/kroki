package io.github.kerubistan.kroki.io

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import kotlin.text.Charsets.UTF_8

class ForkInputStreamTest {

	private val text = """shalalla-hallala """.repeat(4096).trimIndent()

	@Test
	fun read() {
		ForkInputStream(text.byteInputStream(UTF_8)).apply {
			val bytes = ByteArray(128) { read().toByte() }
			assertArrayEquals(bytes, text.toByteArray(UTF_8).copyOfRange(0, 128))
		}
	}

	@Test
	fun readByteArray() {
		ForkInputStream(text.byteInputStream(UTF_8)).apply {
			val bytes = ByteArray(128)
			read(bytes)
			assertArrayEquals(bytes, text.toByteArray(UTF_8).copyOfRange(0, 128))
		}
	}

	@Test
	fun readByteArrayWithOffset() {
		ForkInputStream(text.byteInputStream(UTF_8)).apply {
			val bytes = ByteArray(128)
			read(bytes, 0, 128)
			assertArrayEquals(bytes, text.toByteArray(UTF_8).copyOfRange(0, 128))
		}
	}

	@Test
	fun close() {
		ForkInputStream(text.byteInputStream(UTF_8)).use {
			read()
		}
	}
}