package com.github.kroki.benchmark.numbers

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.infra.Blackhole

import com.github.kroki.numbers.times
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State

@State(Scope.Benchmark)
open class BigDecimalTimesBigIntegerBenchmark {

    private val decimal = "0.123".toBigDecimal()
    private val integer = 2.toBigInteger()

    @Benchmark
    fun calculate(hole: Blackhole) {
        hole.consume(decimal * integer)
    }
}