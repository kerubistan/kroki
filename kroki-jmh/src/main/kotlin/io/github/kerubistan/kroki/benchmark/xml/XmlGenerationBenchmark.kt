package io.github.kerubistan.kroki.benchmark.xml

import io.github.kerubistan.kroki.io.NullOutputStream
import io.github.kerubistan.kroki.xml.FormatMode
import io.github.kerubistan.kroki.xml.XmlBuilder
import io.github.kerubistan.kroki.xml.xml
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole

@State(Scope.Benchmark)
open class XmlGenerationBenchmark {

	@Param("COMPACT", "PRETTY_TABS", "PRETTY_BIG_SPACE_NAZI", "PRETTY_SMALL_SPACE_NAZI")
	var formatModeStr: String = ""

	@Param("blank", "tiny", "medium", "big", "huge")
	var size: String = ""

	private lateinit var formatMode: FormatMode

	private lateinit var builder: XmlBuilder.() -> Unit

	private val blankBuilder : XmlBuilder.() -> Unit = {}

	private val tinyBuilder : XmlBuilder.() -> Unit = {
		!"comment"
		tag("tag", "attribute" to "value")
	}

	private fun builder(size : Int) : XmlBuilder.() -> Unit = {
		repeat(size) {
			!"comment"
			tag("tag", "attribute" to "value")
		}
	}

	@Setup
	fun setup() {
		formatMode = FormatMode.valueOf(formatModeStr)
		builder = when(size) {
			"blank" -> blankBuilder
			"tiny" -> tinyBuilder
			"medium" -> builder(10)
			"big" -> builder(100)
			"huge" -> builder(1000)
			else -> TODO()
		}
	}


	@Benchmark
	fun generate(hole: Blackhole) {
		hole.consume(
			xml(formatMode = formatMode, root = "root", builder = builder).reader().readText()
		)
	}

	@Benchmark
	fun generateDirectly(hole: Blackhole) {
		hole.consume(
			xml(formatMode = formatMode, root = "root", out = NullOutputStream, builder = builder)
		)
	}

}