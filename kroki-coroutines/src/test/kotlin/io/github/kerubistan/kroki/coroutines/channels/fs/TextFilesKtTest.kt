package io.github.kerubistan.kroki.coroutines.channels.fs

import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

class TextFilesKtTest {

	@Test
	fun readLines() {
		val tempFile = File.createTempFile("test", "txt")
		try {
			tempFile.writeText(
				"""
				Foo
				Bar
				Baz
			""".trimIndent()
			)

			runBlocking {
				assertEquals(listOf("Foo", "Bar", "Baz"), readLines(tempFile).toList())
			}

		} finally {
			assertTrue(tempFile.delete())
		}

	}
}