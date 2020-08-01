package io.github.kerubistan.kroki.io

import java.io.InputStream

object NullInputStream : InputStream() {

	private const val closedNothingToRead = -1

	override fun read(): Int = closedNothingToRead
	override fun available(): Int = 0
	override fun close() {
		// intentionally blank
	}

	override fun read(p0: ByteArray) = closedNothingToRead
	override fun read(p0: ByteArray, p1: Int, p2: Int) = closedNothingToRead
}