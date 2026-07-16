package io.github.kerubistan.kroki.io

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EasyStringWriterTest {

	@Test
	fun write() {
		EasyStringWriter().use {
			it.write("foo")
			assertEquals(
				"foo",
				it.toString()
			)
		}
		EasyStringWriter().use {
			it.append('f').append('o').append('o')
			assertEquals(
				"foo",
				it.toString()
			)
		}
	}
}