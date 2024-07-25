package io.github.kerubistan.kroki.benchmark.coroutines.channels

import io.github.kerubistan.kroki.coroutines.channels.sink
import io.github.kerubistan.kroki.coroutines.channels.transform
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State

@State(Scope.Benchmark)
open class ChannelOverheadBenchmark {

	@Param("0", "1", "2", "3", "4")
	var stepCount: Int = 0

	@Benchmark
	fun run() = runBlocking {
		val channel = produce(capacity = 16) {
			repeat(1000_000) {
				send(it)
			}
		}
		var transform = channel
		repeat(stepCount) {
			transform = transform(transform) { it }
		}
		sink(transform)
	}

}