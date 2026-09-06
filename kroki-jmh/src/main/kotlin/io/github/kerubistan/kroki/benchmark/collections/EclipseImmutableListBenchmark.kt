package io.github.kerubistan.kroki.benchmark.collections

import org.eclipse.collections.api.factory.Lists
import org.eclipse.collections.api.list.ImmutableList
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import java.util.ArrayList

@State(Scope.Benchmark)
open class EclipseImmutableListBenchmark {
	@Param("1", "16", "1024", "4096")
	var size: Int = 0

	lateinit var list: ImmutableList<String>

	@Setup
	fun setup() {
		val rawList: MutableList<String> = ArrayList(size)
		for (i in 0..size) {
			rawList.add(i.toString())
		}
		rawList.shuffle()

		list = Lists.immutable.ofAll(rawList)
	}

	@Benchmark
	fun firstUsingIterable(blackhole: Blackhole) {
		blackhole.consume(list.first())
	}

	@Benchmark
	fun firstUsingIndex(blackhole: Blackhole) {
		blackhole.consume(list.get(0))
	}

	@Benchmark
	fun iterateWithForEach(blackhole: Blackhole) {
		list.forEach(blackhole::consume)
	}

	@Benchmark
	fun iterateWithForLoop(blackhole: Blackhole) {
		for (item in list) {
			blackhole.consume(item)
		}
	}

}