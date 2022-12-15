package io.github.kerubistan.kroki.benchmark.xml

import io.github.kerubistan.kroki.delegates.threadLocal
import io.github.kerubistan.kroki.xml.buildXmlEventStreamReader
import io.github.kerubistan.kroki.xml.readAsXmlEventStream
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory

@State(Scope.Benchmark)
open class XmlReaderBenchmark {

	lateinit var xml: String

	private val domFactory by threadLocal { DocumentBuilderFactory.newInstance() }

	private val documentBuilder by threadLocal { domFactory.newDocumentBuilder() }

	@Param("0", "1", "2", "4", "5", "6", "7", "8", "9", "10", "100", "1000", "10000")
	var size = 1

	@Setup
	fun setup() {
		xml = buildString {
			appendLine(
				"""
				<root>
					<tag>
				""".trimIndent()
			)
			(0..size).forEach {
				appendLine("<data>$it</data>")
			}
			appendLine(
				"""
					</tag>
				</root>
				""".trimIndent()
			)
		}
	}

	@Benchmark
	fun readAsXmlEventStream(hole: Blackhole) {
		val result = mutableListOf<String>()
		xml.byteInputStream().readAsXmlEventStream(buildXmlEventStreamReader {
			"root" {
				"tag" {
					"data" - {
						result.add(elementText)
					}
				}
			}
		})

		hole.consume(result)
	}

	@Benchmark
	fun domParse(hole: Blackhole) {
		val result = mutableListOf<String>()
		val document = documentBuilder.parse(xml.byteInputStream())
		val tag = document.documentElement
			.getElementsByTagName("tag").item(0) as Element
		tag
			.getElementsByTagName("data").let {
				for (index in 0 until it.length) {
					val elem = it.item(index) as Element
					result.add(elem.textContent)
				}
			}

		hole.consume(result)
	}

}