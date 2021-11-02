package io.github.kerubistan.kroki.benchmark.collections

import io.github.kerubistan.kroki.collections.replace
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class ReplaceBenchmark {

	@Param("0", "1", "2", "4", "8", "16", "32")
	var listSize = 0

	@Param("2", "4")
	var replaceItemsPer = 0

	lateinit var list: List<Boolean>

	@Setup
	fun setup() {
		list = (0 until listSize).map { index ->
			index % replaceItemsPer == 0
		}
	}

	@Benchmark
	fun run(hole: Blackhole) {
		hole.consume(list.replace(filter = { !it }, replacer = { !it }))
	}
}