package io.github.kerubistan.kroki.benchmark.collections

import io.github.kerubistan.kroki.collections.update
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class MapUpdateBenchmark {

	@Param("0", "1", "2", "4", "8", "16", "32", "64", "128", "256", "512", "1024")
	var size = 0

	lateinit var map: Map<String, String>

	@Setup
	fun setup() {
		map = (0..size).map { index -> "key-$index" to "value-$index" }.toMap()
	}

	@Benchmark
	fun plus(hole: Blackhole) {
		hole.consume(map + ("key-0" to "${map["key-0"]} updated"))
	}

	@Benchmark
	fun hit(hole: Blackhole) {
		hole.consume(map.update("key-0") { value -> "$value updated" })
	}

	@Benchmark
	fun miss(hole: Blackhole) {
		hole.consume(map.update("key-MISS") { value -> "$value updated" })
	}

}