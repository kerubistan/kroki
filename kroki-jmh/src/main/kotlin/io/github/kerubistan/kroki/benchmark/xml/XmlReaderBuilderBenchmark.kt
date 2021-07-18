package io.github.kerubistan.kroki.benchmark.xml

import io.github.kerubistan.kroki.xml.buildXmlEventStreamReader
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.infra.Blackhole

open class XmlReaderBuilderBenchmark {
	@Benchmark
	fun buildReaderBlank(hole : Blackhole) {
		hole.consume(
			buildXmlEventStreamReader {
			}
		)
	}

	@Benchmark
	fun buildReaderSingle(hole : Blackhole) {
		hole.consume(
			buildXmlEventStreamReader {
				"tag" {

				}
			}
		)
	}

	@Benchmark
	fun buildReaderXhtml(hole : Blackhole) {
		hole.consume(
			buildXmlEventStreamReader {
				"html" {
					"head" {
						"title" - {

						}
					}
					"body" {
						"h1" - {

						}
					}
				}
			}
		)
	}

}