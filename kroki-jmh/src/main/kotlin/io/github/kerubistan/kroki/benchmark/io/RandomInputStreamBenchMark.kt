package io.github.kerubistan.kroki.benchmark.io

import io.github.kerubistan.kroki.io.NullOutputStream
import io.github.kerubistan.kroki.io.RandomInputStream
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class RandomInputStreamBenchMark {

	@Param("0", "1", "64", "1024", "8192")
	var length = 0

	lateinit var input : RandomInputStream

	@Setup
	fun setup() {
		input = RandomInputStream(length = length)
	}

	@Benchmark
	fun copyTo(hole : Blackhole) {
		hole.consume(input.copyTo(NullOutputStream))
	}

}