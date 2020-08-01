package io.github.kerubistan.kroki.io

import org.junit.jupiter.api.Test

internal class NullOutputStreamTest {

	@Test
	fun write() {
		NullOutputStream.writer().use {
			it.write("blah")
			it.write("x")
			it.appendln("")
			it.flush()
		}
	}
}