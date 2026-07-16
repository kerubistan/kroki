package io.github.kerubistan.kroki.benchmark.coroutines.channels

import io.github.kerubistan.kroki.coroutines.channels.joinChannels
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class JoinBenchmark {

	@Param("0", "1", "16", "256", "1024", "4096")
	var nrOfMessages = 1

	@Param("0", "1", "8", "16")
	var capacity = 1

	@Param("1", "2", "4", "8", "16", "32", "64", "128", "256", "512", "1024")
	var nrOfChannels = 1

	@Benchmark
	fun run(hole: Blackhole) = runBlocking {
		val jobs = (0..nrOfChannels).map { channelNr ->
			produce(capacity = capacity) {
				repeat(nrOfMessages) { messageNr ->
					this.send("message-$messageNr from channel $channelNr")
				}
			}
		}
		val joined = joinChannels(jobs, capacity, String::compareTo)
		for (message in joined) {
			hole.consume(message)
		}
	}

}