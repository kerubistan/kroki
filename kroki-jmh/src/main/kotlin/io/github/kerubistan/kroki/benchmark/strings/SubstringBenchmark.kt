package io.github.kerubistan.kroki.benchmark.strings

import io.github.kerubistan.kroki.strings.substringBetween
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class SubstringBenchmark {

    private val str = """Some text "quotes" and some more text."""

    @Benchmark
    fun substringBetween(hole: Blackhole) {
        str.substringBetween("\"", "\"")
    }
}