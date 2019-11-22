package io.github.kerubistan.kroki.benchmark.collections

import io.github.kerubistan.kroki.collections.concat
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class ListConcatBenchmark {

    private val list = listOf(
        listOf("A", "B"),
        listOf("C"),
        listOf("D", "E", "F")
    )

    @Benchmark
    fun concat(hole: Blackhole) {
        hole.consume(list.concat())
    }
}