package io.github.kerubistan.kroki.benchmark.coroutines.channels

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import java.util.Optional
import java.util.concurrent.BlockingDeque
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque

@State(Scope.Benchmark)
open class ThreadCommunicationBenchmark {
	@Param("2", "4", "8")
	var nrOfThreads: Int = 2

	@Param("1", "2", "4", "8", "16", "32", "64", "128", "256")
	var capacity: Int = 1

	class Generator(private val output: BlockingQueue<Optional<Int>>) : Runnable {
		override fun run() {
			repeat(1000_000) {
				output.put(Optional.of(it))
			}
			output.put(Optional.empty())
		}
	}

	class Terminator(private val input: BlockingDeque<Optional<Int>>) : Runnable {
		override fun run() {
			var element = input.takeFirst()
			while (element.isPresent) {
				element = input.takeFirst()
			}
		}
	}

	class Worker(private val input: BlockingDeque<Optional<Int>>, private val output: BlockingQueue<Optional<Int>>) :
		Runnable {
		override fun run() {
			var element = input.takeFirst()
			output.put(element)
			while (element.isPresent) {
				element = input.takeFirst()
				output.put(element)
			}
		}
	}

	@Benchmark
	fun run() {
		var queue = LinkedBlockingDeque<Optional<Int>>(capacity)
		val generator = Thread(Generator(queue))
		generator.start()

		if (nrOfThreads > 2) {
			(2..nrOfThreads).forEach {
				val swap = LinkedBlockingDeque<Optional<Int>>(capacity)
				Thread(Worker(queue, swap)).start()
				queue = swap
			}
		}

		val terminator = Thread(Terminator(queue))
		terminator.start()

		generator.join()
		terminator.join()
	}

}