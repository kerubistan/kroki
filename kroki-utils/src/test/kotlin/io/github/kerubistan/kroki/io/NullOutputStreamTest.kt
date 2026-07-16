package io.github.kerubistan.kroki.io

import io.github.kerubistan.kroki.size.GB
import io.github.kerubistan.kroki.size.MB
import org.junit.jupiter.api.Test

class NullOutputStreamTest {

	@Test
	fun write() {
		NullOutputStream.writer().use {
			it.write("blah")
			it.write("x")
			it.appendLine("")
			it.flush()
		}
		NullOutputStream.use { output ->
			val byteArray = ByteArray(size = 1.MB.toInt(), init = { index -> (index % 2).toByte() })
			repeat(1024) {
				output.write(byteArray)
			}
		}
		NullOutputStream.use { output ->
			repeat(1.GB.toInt()) {
				output.write(it)
			}
		}
	}
}