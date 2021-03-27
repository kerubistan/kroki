package io.github.kerubistan.kroki.json.stream

import org.junit.jupiter.api.Test

class StreamsKtTest {

	@Test
	fun buildJsonEventStreamReader() {

		data class Name(
			val firstName: String,
			val lastName: String,
			val title: String
		)

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
			`$` { // meaning I expect an object
				println(read<Any>("name"))
			}
		}
	}

	@Test
	fun articleStream() {
		data class Article(
			val title : String,
			val text : String
		)
		"""
			[
				{
					"title" : "Article-1",
					"text" : "lorem ipsum shalalla-hallala"
				},
				{
					"title" : "Article-2",
					"text" : "blah blah"
				}
			]
		""".trimIndent().byteInputStream().readAsJsonEventStream {
			"$[*]" {

			}
		}
	}


	@Test
	fun readAsXmlEventStream() {
	}
}