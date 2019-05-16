package com.github.kroki.benchmark.time

import com.github.kroki.time.now
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.infra.Blackhole

open class NowBenchmark {
    @Benchmark
    fun benchmarkNow(hole: Blackhole) {
        hole.consume(now())
    }
}