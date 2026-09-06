package io.github.kerubistan.kroki.benchmark.collections

import com.google.common.collect.ImmutableList
import io.github.kerubistan.kroki.collections.immutableListOf
import io.github.kerubistan.kroki.collections.immutableSorted
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import java.util.*

@State(Scope.Benchmark)
open class ImmutableArrayListBenchmark {

	@Param("1", "16", "1024", "4096")
	var size: Int = 0

	@Param("arraylist", "unmodifiableList", "immutablearraylist", "guava")
	var type: String = "arraylist"

	lateinit var list: List<String>

	@Setup
	fun setup() {
		val rawList: MutableList<String> = ArrayList(size)
		for (i in 0..size) {
			rawList.add(i.toString())
		}
		rawList.shuffle()
		when (type) {
			"arraylist" -> {
				list = ArrayList(rawList)
			}
			"unmodifiableList" -> {
				list = Collections.unmodifiableList(rawList)
			}
			"immutablearraylist" -> {
				list = immutableListOf(*rawList.toTypedArray())
			}
			"guava" -> {
				list = ImmutableList.builder<String>().addAll(rawList).build()
			}
		}
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

	@Benchmark
	fun iterateWithCountingFor(blackhole: Blackhole) {
		for (index in (0 .. list.lastIndex)) {
			blackhole.consume(list[index])
		}
	}

	/**
	 * An example of what a faster list could do.
	 */
	@Benchmark
	fun map(blackhole: Blackhole) {
		blackhole.consume(list.map { it.lowercase() })
	}

	@Benchmark
	fun callToString(blackhole: Blackhole) {
		blackhole.consume(list.toString())
	}

	@Benchmark
	fun first(blackhole: Blackhole) {
		blackhole.consume(list.first())
	}

	@Benchmark
	fun last(blackhole: Blackhole) {
		blackhole.consume(list.last())
	}

	@Benchmark
	fun indexOf(blackhole: Blackhole) {
		blackhole.consume(list.indexOf("1"))
	}

	@Benchmark
	fun isEmpty(blackhole: Blackhole) {
		blackhole.consume(list.isEmpty())
	}

	@Benchmark
	fun sorted(blackhole: Blackhole) {
		blackhole.consume(list.sorted())
	}

	@Benchmark
	fun immutableSorted(blackhole: Blackhole) {
		blackhole.consume(list.immutableSorted())
	}

	@Benchmark
	fun max(blackhole: Blackhole) {
		blackhole.consume(list.max())
	}

	@Benchmark
	fun callHashCode(blackhole: Blackhole) {
		blackhole.consume(list.hashCode())
	}
}