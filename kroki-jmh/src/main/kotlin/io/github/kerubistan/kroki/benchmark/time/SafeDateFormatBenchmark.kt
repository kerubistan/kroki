package io.github.kerubistan.kroki.benchmark.time

import io.github.kerubistan.kroki.delegates.threadLocal
import io.github.kerubistan.kroki.time.SafeDateFormat
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.infra.Blackhole
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

open class SafeDateFormatBenchmark {

	companion object {
		val pattern = "yyyy-MM-dd"
		val safeDateFormat = SafeDateFormat(pattern)
		val simpleDateFormat = SimpleDateFormat(pattern)
		val simpleDateFormatThreadLocal by threadLocal { simpleDateFormat }
		val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
		val date = simpleDateFormat.parse("2017-09-27")
		val localDate = LocalDate.of(2017, 9, 27)
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
	fun formatByDateTimeFormatter(hole: Blackhole) {
		hole.consume(dateTimeFormatter.format(localDate))
	}

	@Benchmark
	fun formatByDateTimeFormatterDate(hole: Blackhole) {
		hole.consume(dateTimeFormatter.format(date.toInstant()))
	}

	@Benchmark
	fun formatBySimpleDateFormatThreadLocal(hole: Blackhole) {
		hole.consume(simpleDateFormatThreadLocal.format(date))
	}

}