package com.github.kroki.benchmark.collections

import com.github.kroki.collections.upsert
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class MapUpsertBenchmark {

    @Param("4", "8", "16", "32", "64", "128", "256", "512", "1024")
    var mapSize = 4

    lateinit var map: Map<String, String>

    @Setup
    fun setup() {
        map = (1..mapSize).map { "key-$it" to "value-$it" }.toMap()
    }

    @Benchmark
    fun upsertExisting(hole: Blackhole) {
        hole.consume(
            map.upsert(key = "key-1", mapper = { it.plus(1) }, init = { "value-1" })
        )
    }

    @Benchmark
    fun upsertNotexisting(hole: Blackhole) {
        hole.consume(
            map.upsert(key = "new_key", mapper = { it.plus(1) }, init = { "new value" })
        )
    }
}