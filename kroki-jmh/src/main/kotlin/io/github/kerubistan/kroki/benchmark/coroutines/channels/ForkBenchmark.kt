package io.github.kerubistan.kroki.benchmark.coroutines.channels

import io.github.kerubistan.kroki.coroutines.channels.fork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State

@State(Scope.Benchmark)
open class ForkBenchmark {

	@Param("4096")
	var nrOfMessages = 4096

	@Param("0", "1", "2", "4")
	var outCapacity = 0


	@Benchmark
	fun run() {
		runBlocking(Dispatchers.Unconfined) {
			val (channel1, channel2) = fork(
				produce { repeat(nrOfMessages) { send("message $it") } }, outCapacity = outCapacity
			)

			for (message in channel1) {
				channel2.receive()
			}
		}
	}

}