package io.github.kerubistan.kroki.benchmark.strings

import io.github.kerubistan.kroki.strings.remove
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class StringRemoveBenchmark {

	private val spaces = "\\s+".toRegex()

	@Benchmark
	fun remove(hole: Blackhole) {
		hole.consume("blah bla    blaaah".remove(spaces))
	}
}