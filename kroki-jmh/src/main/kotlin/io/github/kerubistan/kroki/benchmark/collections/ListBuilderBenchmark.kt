package io.github.kerubistan.kroki.benchmark.collections

import com.google.common.collect.ImmutableList
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class ListBuilderBenchmark {

	@Param("0", "1", "2", "16", "1024", "4096")
	var size = 0

	@Benchmark
	fun buildImmutableArrayList(blackhole: Blackhole) {
		blackhole.consume(
			io.github.kerubistan.kroki.collections.buildList {
				repeat(size) {
					add(it)
				}
			}
		)
	}

	@Benchmark
	fun buildGuavaImmutableList(blackhole: Blackhole) {
		blackhole.consume(
			ImmutableList.builder<Int>().apply {
				repeat(size) {
					add(it)
				}
			}.build()
		)
	}

}