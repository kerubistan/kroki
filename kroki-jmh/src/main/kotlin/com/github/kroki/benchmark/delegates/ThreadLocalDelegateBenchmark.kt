package com.github.kroki.benchmark.delegates

import com.github.kroki.delegates.threadLocal
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class ThreadLocalDelegateBenchmark {

    private val testValue by threadLocal { 1 }

    @Benchmark
    fun useThreadLocal(hole: Blackhole) {
        hole.consume(testValue)
    }

}