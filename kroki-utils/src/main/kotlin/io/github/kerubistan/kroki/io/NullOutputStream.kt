package io.github.kerubistan.kroki.io

import java.io.OutputStream

/**
 * An OutputStream that writes nowhere, a kotlin equivalent of /dev/null
 */
object NullOutputStream : OutputStream() {
	override fun write(p0: ByteArray, p1: Int, p2: Int) {
		// intentionally blank
	}
	override fun flush() {
		// intentionally blank
	}
	override fun write(p0: ByteArray) {
		// intentionally blank
	}
	override fun close() {
		// intentionally blank
	}
	override fun write(data: Int) {
		// intentionally blank
	}
}