package io.github.kerubistan.kroki.benchmark.coroutines

import io.github.kerubistan.kroki.coroutines.priorityChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import kotlin.random.Random

@State(Scope.Benchmark)
open class PriorityChannelBenchmark {
	@Param("1", "2", "4", "8", "16", "32", "64", "128", "256", "512", "1024", "2048", "4096")
	var nrOfMessages = 0

	@Param("2", "4", "8", "16", "32", "64", "128", "256", "512", "1024", "2048", "4096")
	var bufferSize = 0

	lateinit var messages: List<Long>

	@Setup
	fun setup() {
		val random = Random(0)
		messages = (0 until nrOfMessages).map { 100000 + random.nextLong(99999) }
	}

	@ExperimentalCoroutinesApi
	@Benchmark
	fun priority(hole: Blackhole) = runBlocking {
		val channel = priorityChannel(
			maxCapacity = bufferSize,
			comparator = Comparator<Long> { first, second -> first.compareTo(second) }
		)
		async {
			messages.forEach {
				channel.send(it)
			}
			channel.close()
		}.start()

		async {
			for (msg in channel) {
				hole.consume(msg)
			}
		}.start()

	}

}