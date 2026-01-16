package io.github.kerubistan.kroki.benchmark.numbers

import io.github.kerubistan.kroki.numbers.rangeByOrNull
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class RangeByBenchmark {

	@Param("2", "4", "8", "16", "32", "64", "128", "256", "512", "1024")
	var size = "2"
	lateinit var list: List<Int>

	@Setup
	fun setup() {
		list = (0..size.toInt()).map { it }
	}

	@Benchmark
	fun minAndMax(hole: Blackhole) {
		hole.consume(list.minBy { it })
		hole.consume(list.maxBy { it })
	}

	@Benchmark
	fun rangeByOrNull(hole: Blackhole) {
		hole.consume(list.rangeByOrNull { it })
	}
}