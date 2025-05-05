package io.github.kerubistan.kroki.benchmark.xml

import io.github.kerubistan.kroki.delegates.threadLocal
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole
import org.xml.sax.InputSource
import javax.xml.xpath.XPathFactory

@State(Scope.Benchmark)
open class XPathBenchmark {

	val tinyXml = """
		<html>
			<head>
				<title>blah</title>
			</head>
			<body>
				<table>
					<thead>
						<tr>
							<td>name</td>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td id="yes">blah</td>
						</tr>
					</tbody>
				</table>
			</body>
		</html>
	""".trimIndent()

	val bigXml = """
		<html>
			<head>
				<title>blah</title>
				<script source="example.com/js"/>
			</head>
			<body>
				<p>
					lot of blah
					lot of blah
					lot of blah
					lot of blah
				</p>
				<table>
					<thead>
						<tr>
							<td>name</td>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td id="no">blah</td>
						</tr>
						<tr>
							<td id="nooo">blah</td>
						</tr>
						<tr>
							<td id="noooooo">blah</td>
						</tr>
						<tr>
							<td id="no-that-either">blah</td>
						</tr>
						<tr>
							<td id="yes">blah</td>
						</tr>
					</tbody>
				</table>
			</body>
		</html>
	""".trimIndent()

	@Param("tiny", "big")
	var input: String = "tiny"

	@Param(
		"//html/head/title/text()",
		"//html/body/table/tbody/tr/td[@id='yes']/text()"
	)
	var pathExpression = "//html/head/title/text()"

	lateinit var xml: String

	val xPath by threadLocal { XPathFactory.newInstance().newXPath() }

	@Setup
	fun setup() {
		xml = when (input) {
			"tiny" -> {
				tinyXml
			}
			"big" -> {
				bigXml
			}
			else -> throw IllegalArgumentException("$input is unknown")
		}
	}

	@Benchmark
	fun getByXpath(hole: Blackhole) {
		hole.consume(xPath.evaluate(pathExpression, InputSource(xml.byteInputStream())))
	}

}