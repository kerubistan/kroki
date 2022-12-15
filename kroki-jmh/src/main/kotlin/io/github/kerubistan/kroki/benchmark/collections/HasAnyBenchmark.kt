package io.github.kerubistan.kroki.benchmark.collections

import io.github.kerubistan.kroki.collections.hasAny
import io.github.kerubistan.kroki.collections.hasNone
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class HasAnyBenchmark {

	@Param("10", "100")
	lateinit var listSize: String

	@Param("1", "2", "3")
	lateinit var itemPosition: String

	lateinit var list: List<Number>

	@Setup
	fun setup() {
		list =
			(0..listSize.toInt()).toList().mapIndexed { index, item -> if (index != itemPosition.toInt()) item else -1 }
	}

	@Benchmark
	fun hasAnyHit(hole: Blackhole) {
		hole.consume(list.hasAny<Int> { it < 0 })
	}

	@Benchmark
	fun hasAnyMiss(hole: Blackhole) {
		hole.consume(list.hasAny<Byte> { it < 0 })
	}

	@Benchmark
	fun hasNoneHit(hole: Blackhole) {
		hole.consume(list.hasNone<Int> { it < 0 })
	}

	@Benchmark
	fun hasNoneMiss(hole: Blackhole) {
		hole.consume(list.hasNone<Byte> { it < 0 })
	}

}