package io.github.kerubistan.kroki.benchmark.coroutines.channels

import io.github.kerubistan.kroki.coroutines.channels.batch
import io.github.kerubistan.kroki.coroutines.channels.sink
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State

@State(Scope.Benchmark)
open class BatchBenchmark {

	@Param("1", "4", "16", "32", "64", "128", "256", "512", "1024")
	var size: Int = 1

	@Param("256", "512", "1024")
	var count: Int = 1

	@OptIn(ExperimentalCoroutinesApi::class)
	@Benchmark
	fun run() = runBlocking {
		val input = produce {
			repeat(count) {
				send(it)
			}
		}
		val batches = batch(input, size)
		sink(batches)
	}
}