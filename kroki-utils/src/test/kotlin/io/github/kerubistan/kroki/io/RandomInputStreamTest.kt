package io.github.kerubistan.kroki.io

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class RandomInputStreamTest {
	@Test
	fun read() {
		val out = CountingOutputStream(NullOutputStream)
		RandomInputStream(length = 4096).copyTo(out)
		assertEquals(4096, out.counter)
	}
}