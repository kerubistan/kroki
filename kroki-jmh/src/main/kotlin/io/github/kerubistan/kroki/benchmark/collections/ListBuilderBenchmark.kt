package io.github.kerubistan.kroki.benchmark.collections

import com.google.common.collect.ImmutableList
import io.github.kerubistan.kroki.collections.buildList
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
			buildKrokiList()
		)
	}

	private fun buildKrokiList(): List<Int> = buildList {
		repeat(size) {
			add(it)
		}
	}

	@Benchmark
	fun buildGuavaImmutableList(blackhole: Blackhole) {
		blackhole.consume(
			buildGuavaList()
		)
	}

	private fun buildGuavaList(): ImmutableList<Int?> = ImmutableList.builder<Int>().apply {
		repeat(size) {
			add(it)
		}
	}.build()

	@Benchmark
	fun buildKotlinList(blackhole: Blackhole) {
		blackhole.consume(
			buildKotlinList()
		)
	}

	private fun buildKotlinList(): List<Int> = buildList {
		repeat(size) {
			add(it)
		}
	}

}