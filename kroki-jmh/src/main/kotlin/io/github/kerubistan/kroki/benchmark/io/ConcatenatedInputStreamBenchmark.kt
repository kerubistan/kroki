package io.github.kerubistan.kroki.benchmark.io

import io.github.kerubistan.kroki.io.ConcatenatedInputStream
import io.github.kerubistan.kroki.io.NullOutputStream
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import java.io.ByteArrayInputStream

@State(Scope.Benchmark)
open class ConcatenatedInputStreamBenchmark {

	@Param("0", "1", "2", "16", "32", "128", "1024")
	var size: Int = 0

	@Param("0", "1", "2", "4")
	var nrOfInputStreams: Int = 0

	@Benchmark
	fun concatenate(hole: Blackhole) {
		ConcatenatedInputStream((0 until nrOfInputStreams).map {
			ByteArrayInputStream(ByteArray(size))
		}).copyTo(NullOutputStream)
	}

}