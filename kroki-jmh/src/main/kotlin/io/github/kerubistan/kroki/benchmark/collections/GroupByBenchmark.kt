package io.github.kerubistan.kroki.benchmark.collections

import io.github.kerubistan.kroki.collections.groupsBy
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import kotlin.random.Random

@State(Scope.Benchmark)
open class GroupByBenchmark {

	@Param("0", "1", "2", "4", "8", "16", "32", "64", "128", "256")
	var listSize = 0

	@Param("0", "1", "2", "4", "8", "16", "32", "64")
	var nrOfGroups = 0

	@Param("0", "1", "2", "4", "8")
	var groupsPerItem = 0

	data class Product(val name: String, val categories: Set<String>)

	lateinit var list: List<Product>

	@Setup
	fun setup() {
		val random = Random
		list = (0 until listSize).map { index ->
			Product(
				name = "Product $index",
				categories = (0 until groupsPerItem).map {
					"category-${random.nextInt(nrOfGroups)})"
				}.toSet()
			)
		}
	}

	@Benchmark
	fun run(hole: Blackhole) {
		hole.consume(list.groupsBy(Product::categories))
	}
}