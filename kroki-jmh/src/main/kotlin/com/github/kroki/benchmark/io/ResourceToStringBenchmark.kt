package com.github.kroki.benchmark.io

import com.github.kroki.io.resourceToString
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.infra.Blackhole

open class ResourceToStringBenchmark {

    @Benchmark
    fun resourceToStringBenchmark(hole : Blackhole) {
        hole.consume(resourceToString("com/github/kroki/benchmark/io/1k.txt"))
    }

}