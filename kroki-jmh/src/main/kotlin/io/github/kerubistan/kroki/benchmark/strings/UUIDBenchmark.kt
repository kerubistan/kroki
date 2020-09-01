package io.github.kerubistan.kroki.benchmark.strings

import io.github.kerubistan.kroki.strings.isUUID
import io.github.kerubistan.kroki.strings.toUUID
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import java.util.UUID.randomUUID

@State(Scope.Benchmark)
open class UUIDBenchmark {

	val id = randomUUID().toString()

	@Benchmark
	fun toUUID(hole: Blackhole) {
		hole.consume(id.toUUID())
	}

	@Benchmark
	fun isUUID(hole: Blackhole) {
		hole.consume(id.isUUID())
	}

	@Benchmark
	fun isUUIDNotMatch(hole: Blackhole) {
		hole.consume("".isUUID())
	}
}