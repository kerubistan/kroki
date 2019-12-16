package io.github.kerubistan.kroki.parallel

import io.github.kerubistan.kroki.numbers.sumBy
import kotlinx.coroutines.asCoroutineDispatcher
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.math.BigInteger
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

@State(Scope.Benchmark)
open class ParallelSumComparisonBenchmark {

    @Param("1", "2", "3", "4")
    var threadPoolSize = 1

    @Param("10", "100", "1000", "10000", "100000", "1000000", "10000000", "100000000")
    var size = 10
    lateinit var list: List<BigInteger>

    lateinit var context: CoroutineContext
    lateinit var exec: ExecutorService

    @Setup
    fun setup() {
        list = (0 until size).map { (it % 2).toBigInteger() }
        exec = Executors.newFixedThreadPool(threadPoolSize)
        context = exec.asCoroutineDispatcher()
    }

    @TearDown
    fun tearDown() {
        exec.shutdown()
    }

    @Benchmark
    fun parallel(hole : Blackhole) {
        hole.consume(list.pSum(context))
    }

    @Benchmark
    fun threaded(hole : Blackhole) {
        hole.consume(list.tSum(exec))
    }

    @Benchmark
    fun singleThreaded(hole : Blackhole) {
        hole.consume(list.sumBy { it })
    }

}