package io.github.kerubistan.kroki.benchmark.time

import io.github.kerubistan.kroki.delegates.threadLocal
import io.github.kerubistan.kroki.time.SafeDateFormat
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.infra.Blackhole
import java.text.SimpleDateFormat

open class SafeDateFormatBenchmark {

	companion object {
		val pattern = "yyyy-MM-dd"
		val safeDateFormat = SafeDateFormat(pattern)
		val simpleDateFormat = SimpleDateFormat(pattern)
		val simpleDateFormatThreadLocal by threadLocal { simpleDateFormat }
		val date = simpleDateFormat.parse("2017-09-27")
	}

	@Benchmark
	fun formatBySafeDateFormat(hole: Blackhole) {
		hole.consume(safeDateFormat.format(date))
	}

	@Benchmark
	fun formatBySimpleDateFormat(hole: Blackhole) {
		hole.consume(simpleDateFormat.format(date))
	}

	@Benchmark
	fun formatBySimpleDateFormatThreadLocal(hole: Blackhole) {
		hole.consume(simpleDateFormatThreadLocal.format(date))
	}

}