package io.github.kerubistan.kroki.io

import java.io.ByteArrayOutputStream
import java.io.InputStream

class ForkInputStream(private val inputStream: InputStream) : InputStream() {

	internal val buffer = ByteArrayOutputStream(128)

	override fun read(): Int =
		inputStream.read().apply { buffer.write(this) }

	override fun read(target: ByteArray): Int =
		inputStream.read(target).apply { buffer.write(target) }

	override fun read(target: ByteArray, offset: Int, length: Int): Int = inputStream.read(target, offset, length)
		.apply { buffer.write(target, offset, this) }

	override fun close() {
		inputStream.close()
		buffer.close()
	}

}