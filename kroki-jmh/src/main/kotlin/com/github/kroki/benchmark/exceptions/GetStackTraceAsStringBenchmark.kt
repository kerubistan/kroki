package com.github.kroki.benchmark.exceptions

import com.github.kroki.exceptions.getStackTraceAsString
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class GetStackTraceAsStringBenchmark {

    private val exception = IllegalArgumentException("no problem")

    @Benchmark
    fun getStackTraceAsString(hole: Blackhole) {
        hole.consume(exception.getStackTraceAsString())
    }

}