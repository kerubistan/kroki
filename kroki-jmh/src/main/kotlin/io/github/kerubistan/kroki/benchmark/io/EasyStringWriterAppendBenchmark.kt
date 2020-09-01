package io.github.kerubistan.kroki.benchmark.io

import io.github.kerubistan.kroki.io.EasyStringWriter
import io.github.kerubistan.kroki.objects.useIt
import org.openjdk.jmh.annotations.Benchmark

/**
 * Was used to test whether overriding the append(CharSequence) method could bring some better
 * performance. Did not help anything at all.
 */
open class EasyStringWriterAppendBenchmark {

	@Benchmark
	fun appendCharSequence() {
		EasyStringWriter().useIt {
			append("hello")
		}
	}

	@Benchmark
	fun appendCharSequenceWithNull() {
		EasyStringWriter().useIt {
			append(null)
		}
	}

}