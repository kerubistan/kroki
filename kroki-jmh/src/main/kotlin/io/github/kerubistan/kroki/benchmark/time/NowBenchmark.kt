package io.github.kerubistan.kroki.benchmark.time

import io.github.kerubistan.kroki.time.now
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.infra.Blackhole

open class NowBenchmark {
	@Benchmark
	fun benchmarkNow(hole: Blackhole) {
		hole.consume(now())
	}
}