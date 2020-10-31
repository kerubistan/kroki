package io.github.kerubistan.kroki.benchmark.flyweight

import io.github.kerubistan.kroki.flyweight.flyWeight
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class FlyWeightCostBenchmark {

	data class DataClass1(
		val id : String
	)

	data class DataClass10(
		val id : String,
		val prop1 : String,
		val prop2 : String,
		val prop3 : String,
		val prop4 : String,
		val prop5 : String,
		val prop6 : String,
		val prop7 : String,
		val prop8 : String,
		val prop9 : String
	)

	private val instance1 = DataClass1(id = "test")
	private val instance10 = DataClass10(
		id = "test",
		prop1 = "test-1",
		prop2 = "test-2",
		prop3 = "test-3",
		prop4 = "test-4",
		prop5 = "test-5",
		prop6 = "test-6",
		prop7 = "test-7",
		prop8 = "test-8",
		prop9 = "test-9"
	)

	@Benchmark
	fun smallDataClass1(hole : Blackhole) {
		hole.consume(instance1.flyWeight())
	}

	@Benchmark
	fun smallDataClass10(hole : Blackhole) {
		hole.consume(instance10.flyWeight())
	}

}