package io.github.kerubistan.kroki.benchmark.collections

import io.github.kerubistan.kroki.collections.immutableListOf
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class ImmutableSubArrayListBenchmark {

	@Param("immutable", "list")
	var type = "list"

	lateinit var list : List<String>

	@Setup
	fun setup() {
		list = when(type) {
			"immutable" -> immutableListOf( *((1..100).map { it.toString() }.toList().toTypedArray()) )
			"list" -> listOf( *((1..100).map { it.toString() }.toList().toTypedArray()) )
			else -> throw IllegalArgumentException("immutable or list")
		}
	}

	@Benchmark
	fun subList(blackhole: Blackhole) {
		blackhole.consume(list.subList(1, 10))
	}

}