package io.github.kerubistan.kroki.io

import io.github.kerubistan.kroki.size.KB
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class NullInputStreamTest {

	@Test
	fun read() {
		assertTrue(NullInputStream.reader().readLines().isEmpty())
		assertTrue(NullInputStream.reader().readText().isEmpty())
		assertEquals(0, NullInputStream.available())
		assertEquals(-1, NullInputStream.read())
		assertEquals(-1, NullInputStream.read(ByteArray(size = 1. KB.toInt())))
	}

}