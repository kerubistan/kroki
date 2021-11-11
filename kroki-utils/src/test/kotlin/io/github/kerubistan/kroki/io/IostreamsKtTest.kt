package io.github.kerubistan.kroki.io

import io.github.kerubistan.kroki.strings.substringBetween
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.nio.charset.StandardCharsets.UTF_8

internal class IostreamsKtTest {

	@Test
	fun peek() {

		val message = """Dear Liza,
			|
			|Test message, please check it.
			|
			|Best regards,
			|Unit Test
		""".trimMargin()
		message.byteInputStream(UTF_8).peek {
			val firstLine = it.reader(UTF_8).buffered().readLine()
			assertEquals("Dear Liza,", firstLine)
		}.use {
			assertEquals(message, it.reader().readText())
		}
	}
}