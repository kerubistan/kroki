package io.github.kerubistan.kroki.io

import java.io.OutputStream

class CountingOutputStream(private val outputStream: OutputStream) : OutputStream() {
	var counter = 0
	override fun write(data: Int) {
		outputStream.write(data)
		counter++
	}

	override fun write(data: ByteArray) {
		outputStream.write(data)
		counter += data.size
	}

	override fun write(data: ByteArray, off: Int, len: Int) {
		outputStream.write(data, off, len)
		counter += len
	}

	override fun flush() = outputStream.flush()

	override fun close() = outputStream.close()

}