package io.github.kerubistan.kroki.json.stream

import org.junit.jupiter.api.Test

class StreamsKtTest {

	@Test
	fun buildJsonEventStreamReader() {
		"""
			{
				"name" : {
					"firstName" : "John",
					"lastName" : "Doe",
					"title" : "Mr"
				},
				"languages" : [
					"DE", "HU", "EN"
				],
				"userName" : "johnd",
				"role" : "admin"
			}
		""".trimIndent().byteInputStream().readAsJsonEventStream {
			 root {
				"name" {

				}
			}
		}
	}

	@Test
	fun readAsXmlEventStream() {
	}
}