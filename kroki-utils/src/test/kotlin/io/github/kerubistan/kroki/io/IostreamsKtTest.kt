package io.github.kerubistan.kroki.io

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.InputStream
import java.nio.charset.StandardCharsets.UTF_8

internal class IostreamsKtTest {

	@Test
	fun peekSample() {
		abstract class InputProcessor() {
			abstract fun process(stream : InputStream)
		}
		val jsonProcessor = mock<InputProcessor>()
		val xmlProcessor = mock<InputProcessor>()
		listOf(
			"""<text>
					hello world!
				</text>
			""".trimIndent(),
			"""{
					"text": "hello world!"
				}
			""".trimIndent()
		).map { it.byteInputStream(UTF_8) }.forEach {
			stream ->
			var isJson = false
			stream.peek {
				try {
					ObjectMapper().createParser(stream).nextToken()
					isJson = true
				} catch (e : JsonParseException) {
					println("beee, not json!")
				}
			}.use {
				input ->
				if(isJson) {
					jsonProcessor.process(input)
				} else {
					xmlProcessor.process(input)
				}
			}
		}
		verify(jsonProcessor, times(1)).process(any())
		verify(xmlProcessor, times(1)).process(any())
	}

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