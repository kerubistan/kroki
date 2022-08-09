package io.github.kerubistan.kroki.io

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CountingOutputStreamTest {

	@Test
	fun write() {
		val output = CountingOutputStream(NullOutputStream)
		RandomInputStream(length = 1024).copyTo(output)
		assertEquals(1024, output.counter)
	}
}