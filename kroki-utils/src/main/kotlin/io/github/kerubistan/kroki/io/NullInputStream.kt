package io.github.kerubistan.kroki.io

import java.io.InputStream

object NullInputStream : InputStream() {
	override fun read(): Int = -1
	override fun available(): Int = 0
	override fun close() {
		// intentionally blank
	}

	override fun read(p0: ByteArray) = 0
	override fun read(p0: ByteArray, p1: Int, p2: Int) = 0
}