package io.github.kerubistan.kroki.benchmark.strings

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class StringReaderVsByteArrayReadBenchmark {

	@Param("64", "1024", "4096", "8192")
	var size : Int = 10

	lateinit var input : String

	@Setup
	fun setup() {
		input = ".".repeat(size)
	}

	@Benchmark
	fun byteArray(hole: Blackhole) {
		hole.consume(input.reader().readText())
	}

	@Benchmark
	fun reader(hole: Blackhole) {
		hole.consume(input.byteInputStream().readAllBytes())
	}

}