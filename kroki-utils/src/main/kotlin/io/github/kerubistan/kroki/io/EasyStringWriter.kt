package io.github.kerubistan.kroki.io

import java.io.Writer

/**
 * An implementation of Writer that uses StringBuilder instead of StringBuffer.
 * Therefore it is not thread-safe, but on a single thread it outperforms the StringWriter.
 */
class EasyStringWriter(preAllocatedSize: Int = 64) : Writer() {

	private val builder = StringBuilder(preAllocatedSize)

	override fun append(ch: Char): Writer {
		builder.append(ch)
		return this
	}

	override fun write(str: String?) {
		builder.append(str)
	}

	override fun write(str: String?, from: Int, to: Int) {
		builder.append(str, from, to)
	}

	override fun write(data: CharArray, from: Int, to: Int) {
		builder.append(data, from, to)
	}

	override fun flush() {
		// like StringWriter, it does nothing
	}

	override fun close() {
		// like StringWriter, it does nothing
	}

	override fun toString() = builder.toString()
}