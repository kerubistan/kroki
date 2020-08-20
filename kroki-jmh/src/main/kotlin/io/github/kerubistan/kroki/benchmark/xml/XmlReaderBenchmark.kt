package io.github.kerubistan.kroki.benchmark.xml

import io.github.kerubistan.kroki.delegates.threadLocal
import io.github.kerubistan.kroki.xml.XmlEventStreamReader
import io.github.kerubistan.kroki.xml.buildXmlEventStreamReader
import io.github.kerubistan.kroki.xml.readAsXmlEventStream
import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import javax.xml.parsers.DocumentBuilderFactory

@State(Scope.Benchmark)
open class XmlReaderBenchmark {

	lateinit var xml: String
	val reader: XmlEventStreamReader = buildXmlEventStreamReader {
		"root" {
			"tag" {
				"data" - {
					// do nothing with the data
				}
			}
		}
	}

	val domFactory by threadLocal { DocumentBuilderFactory.newInstance() }

	@Param("0", "1", "2", "4", "5", "6", "7", "8", "9", "10", "100", "1000", "10000")
	var size = 1

	@Setup
	fun setup() {
		xml = buildString {
			append(
				"""
				<root>
					<tag>
				""".trimIndent()
			)
			(0..size).forEach {
				append("<data>$it</data>")
			}
			append(
				"""
					</tag>
				</root>
				""".trimIndent()
			)
		}
	}

	@Benchmark
	fun readAsXmlEventStream() {
		xml.byteInputStream().readAsXmlEventStream(reader)
	}

	@Benchmark
	fun domParse(hole : Blackhole) {
		hole.consume(
			domFactory.newDocumentBuilder().parse(xml.byteInputStream())
		)
	}

}