package io.github.kerubistan.kroki.benchmark.io

import io.github.kerubistan.kroki.io.NullOutputStream
import io.github.kerubistan.kroki.io.peek
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import java.io.InputStream
import java.nio.charset.StandardCharsets.UTF_8

@State(Scope.Benchmark)
open class InputStreamPeekBenchmark {

	class NoMarkInputStream(private val input : InputStream) : InputStream() {
		override fun read(): Int = input.read()
		override fun read(p0: ByteArray) = input.read(p0)
		override fun read(p0: ByteArray, p1: Int, p2: Int) = input.read(p0, p1, p2)
		override fun markSupported() = false
		override fun close() {
			input.close()
		}
	}

	@Param("1024", "2048", "4096")
	var size = 1024

	@Param("128", "256", "512")
	var peek = 128

	lateinit var text : String

	@Setup
	fun setup() {
		text = ".".repeat(size)
	}

	@Benchmark
	fun peekAndCopy(hole : Blackhole) {
		text.byteInputStream(UTF_8).peek {
			it.read(ByteArray(peek))
		}.use {
			hole.consume(it.copyTo(NullOutputStream))
		}
	}

	@Benchmark
	fun peekAndCopyNoMark(hole : Blackhole) {
		NoMarkInputStream(text.byteInputStream(UTF_8)).peek {
			it.read(ByteArray(peek))
		}.use {
			hole.consume(it.copyTo(NullOutputStream))
		}
	}

	@Benchmark
	fun peekAndReadByteByByte(hole : Blackhole) {
		text.byteInputStream(UTF_8).peek {
			it.read(ByteArray(peek))
		}.use {
			var data = it.read()
			while (data != -1) {
				hole.consume(data)
				data = it.read()
			}
		}
	}

}