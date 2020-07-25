package io.github.kerubistan.kroki.io

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue


class NullInputStreamTest {

	@Test
	fun read() {
		assertTrue(NullInputStream.reader().readLines().isEmpty())
		assertTrue(NullInputStream.reader().readText().isEmpty())
	}

}