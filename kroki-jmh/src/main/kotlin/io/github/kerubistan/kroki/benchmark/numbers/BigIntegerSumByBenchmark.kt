package io.github.kerubistan.kroki.benchmark.numbers

import io.github.kerubistan.kroki.numbers.sumBy
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.math.BigInteger

@State(Scope.Benchmark)
open class BigIntegerSumByBenchmark {

    @Param("2","4","8","16","32","64","128","256","512")
    var size = 2

    @Param("0", "256", "4096", "16384", "268435456")
    var offset = "0"

    var list = listOf<BigInteger>()

    @Setup
    fun setUp() {
        list = (0..size).map { offset.toBigInteger() + it.toBigInteger() }
    }

    @Benchmark
    fun sumBy(hole : Blackhole) {
        hole.consume(
            list.sumBy { it }
        )
    }

}