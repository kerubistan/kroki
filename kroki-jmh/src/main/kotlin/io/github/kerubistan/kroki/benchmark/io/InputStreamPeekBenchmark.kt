package io.github.kerubistan.kroki.benchmark.io

import io.github.kerubistan.kroki.io.NullOutputStream
import io.github.kerubistan.kroki.io.peek
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import java.nio.charset.StandardCharsets.UTF_8

@State(Scope.Benchmark)
open class InputStreamPeekBenchmark {

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