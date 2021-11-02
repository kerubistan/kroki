package io.github.kerubistan.kroki.xml

import org.junit.Assert.assertEquals
import org.junit.Test

class XmlKtTest {

	@Test
	fun readAttributes() {
		"""
			<foo>
				<bar 
					size="XXL"
					color="blue"
					secure="sure"
					/>
			</foo>
		""".trimIndent().byteInputStream().readAsXmlEventStream {
			"foo" {
				"bar" - {
					assertEquals(
						it.readAttributes(),
						mapOf(
							"size" to "XXL",
							"color" to "blue",
							"secure" to "sure"
						)
					)
				}
			}
		}

		"""
			<foo>
				<bar/>
			</foo>
		""".trimIndent().byteInputStream().readAsXmlEventStream {
			"foo" {
				"bar" - {
					assertEquals(
						it.readAttributes(),
						mapOf<String, String>()
					)
				}
			}
		}

	}
}