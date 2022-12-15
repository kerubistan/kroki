package io.github.kerubistan.kroki.benchmark.collections

import io.github.kerubistan.kroki.iteration.toMap
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class IteratorToMapBenchmark {

	@Param("0", "1", "2", "4", "8", "16", "32", "64", "128")
	lateinit var size: String
	lateinit var list: List<String>

	@Setup
	fun setup() {
		list = (0..size.toInt()).map { "item-$it" }
	}

	@Benchmark
	fun iteratorToMap(hole: Blackhole) {
		hole.consume(list.iterator().toMap { this to this })
	}

	@Benchmark
	fun associateWith(hole: Blackhole) {
		hole.consume(list.associateWith { it })
	}

	@Benchmark
	fun listToMap(hole: Blackhole) {
		hole.consume(list.map { it to it }.toMap())
	}

}