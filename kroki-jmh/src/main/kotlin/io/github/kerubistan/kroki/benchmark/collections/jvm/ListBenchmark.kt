package io.github.kerubistan.kroki.benchmark.collections.jvm

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import java.util.LinkedList

@State(Scope.Benchmark)
open class ListBenchmark {

	@Param("1", "2", "4", "16", "32", "64", "128", "256")
	var arraySize = 1

	@Param("array","linked")
	var implementation = "array"

	var insertIndex : Int = 0

	lateinit var list : MutableList<String>

	@Setup
	fun setup() {
		val data = (0..arraySize).map { "item-$it" }.toList()
		list = when(implementation) {
			"array" -> ArrayList(data)
			"linked" -> LinkedList(data)
			else -> throw IllegalArgumentException("no-no")
		}
		insertIndex = arraySize / 2
	}

	@Benchmark
	fun addAndRemove() {
		list.add("new")
		list.removeFirst()
	}

	@Benchmark
	fun addAndRemoveIndex() {
		list.add(insertIndex, "new")
		list.removeAt(insertIndex)
	}

	@Benchmark
	fun iterate(hole : Blackhole) {
		list.forEach {
			hole.consume(it)
		}
	}

}