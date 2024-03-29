package io.github.kerubistan.kroki.benchmark.kotlin

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class RunVsLetComparisonBenchmark {

	private data class Person(
		val firstname: String,
		val lastName: String
	)

	private val person = Person(
		firstname = "John",
		lastName = "Brown"
	)

	@Benchmark
	fun let(hole: Blackhole) {
		hole.consume(person.let { "${it.firstname} ${it.lastName}" })
	}

	@Benchmark
	fun run(hole: Blackhole) {
		hole.consume(person.run { "$firstname $lastName" })
	}

}