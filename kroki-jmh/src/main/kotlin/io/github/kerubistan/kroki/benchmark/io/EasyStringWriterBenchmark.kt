package io.github.kerubistan.kroki.benchmark.io

import io.github.kerubistan.kroki.io.EasyStringWriter
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.infra.Blackhole
import java.io.StringWriter
import java.io.Writer

open class EasyStringWriterBenchmark {

	companion object {
		val testStrings = listOf(
			"foo", "bar", "the lazy dog jumped over the lazy fux", "bar"
		)
	}

	@Benchmark
	fun easyStringWriter(hole: Blackhole) {
		hole.consume(
			EasyStringWriter(64).use(::writeTestStrings)
		)
	}

	private fun writeTestStrings(writer: Writer) {
		testStrings.forEach {
			writer.write(it)
		}
	}

	@Benchmark
	fun stringWriter(hole: Blackhole) {
		hole.consume(
			StringWriter(64).use(::writeTestStrings)
		)
	}

}