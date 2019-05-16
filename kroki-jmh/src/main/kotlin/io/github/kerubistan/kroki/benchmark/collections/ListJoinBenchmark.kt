package io.github.kerubistan.kroki.benchmark.collections

import io.github.kerubistan.kroki.collections.join
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class ListJoinBenchmark {

    private val list = listOf(
        listOf("A", "B"),
        listOf("C"),
        listOf("D", "E", "F")
    )

    @Benchmark
    fun join(hole: Blackhole) {
        hole.consume(list.join())
    }
}