package io.github.kerubistan.kroki.benchmark.xml

import io.github.kerubistan.kroki.FormatMode
import io.github.kerubistan.kroki.xml
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class XmlGenerationBenchmark {

	@Param("COMPACT", "PRETTY_TABS", "PRETTY_BIG_SPACE_NAZI", "PRETTY_SMALL_SPACE_NAZI")
	var formatModeStr: String = ""

	lateinit var formatMode: FormatMode

	@Setup
	fun setup() {
		formatMode = FormatMode.valueOf(formatModeStr)
	}

	@Benchmark
	fun generate(hole: Blackhole) {
		hole.consume(
			xml(formatMode = formatMode, root = "root") {
				!"comment"
				tag("tag", "attribute" to "value")
			}.reader().readText()
		)
	}
}