package io.github.kerubistan.kroki.benchmark.coroutines.channels.fs

import io.github.kerubistan.kroki.coroutines.channels.fs.readLines
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.runBlocking
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.TearDown
import org.openjdk.jmh.infra.Blackhole
import java.io.File

@State(Scope.Benchmark)
open class TextFileBenchmark {

	@Param("1", "1024", "1048576")
	var nrOfLines = 1
	lateinit var file: File

	@Setup
	fun setup() {
		file = File.createTempFile("test", "txt")
		file.writer().use { writer ->
			repeat(nrOfLines) { lineNr ->
				writer.write("line-${lineNr}\n")
			}
		}
	}

	@TearDown
	fun cleanup() {
		file.delete()
	}

	@Benchmark
	fun run(hole: Blackhole) = runBlocking {
		readLines(file).consumeEach { hole.consume(it) }
	}
}
