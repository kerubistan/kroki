package io.github.kerubistan.kroki.benchmark.collections.jvm

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import java.util.LinkedList

@State(Scope.Benchmark)
open class ResortBenchmark {

	@Param("10", "100", "1000")
	var listSize = 10

	@Param("array", "linked")
	var implementation = "array"

	var sortedList = listOf<Int>()

	var shuffledList = listOf<Int>()

	var singleItemChanged = listOf<Int>()

	@Setup
	fun setup() {
		sortedList = (0..listSize).toList()
		shuffledList = sortedList.shuffled()
		singleItemChanged = sortedList.map {
			if (it == 0) {
				2
			} else it
		}
		when (implementation) {
			"array" -> {
				sortedList = ArrayList(sortedList)
				shuffledList = ArrayList(shuffledList)
				singleItemChanged = ArrayList(singleItemChanged)
			}

			"linked" -> {
				sortedList = LinkedList(sortedList)
				shuffledList = LinkedList(shuffledList)
				singleItemChanged = LinkedList(singleItemChanged)
			}
		}
	}

	@Benchmark
	fun resortOrderedList(hole: Blackhole) {
		hole.consume(sortedList.sorted())
	}

	@Benchmark
	fun resortShuffledList(hole: Blackhole) {
		hole.consume(shuffledList.sorted())
	}

	@Benchmark
	fun resortSingleItem(hole: Blackhole) {
		hole.consume(singleItemChanged.sorted())
	}
}