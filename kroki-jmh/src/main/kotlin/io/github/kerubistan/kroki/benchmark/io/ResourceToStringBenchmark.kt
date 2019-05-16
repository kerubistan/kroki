package io.github.kerubistan.kroki.benchmark.io

import io.github.kerubistan.kroki.io.resourceToString
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.infra.Blackhole

open class ResourceToStringBenchmark {

    @Benchmark
    fun resourceToStringBenchmark(hole: Blackhole) {
        hole.consume(resourceToString("io/github/kerubistan/kroki/benchmark/io/1k.txt"))
    }

}