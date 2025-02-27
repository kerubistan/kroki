package io.github.kerubistan.kroki.xml

import org.junit.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.text.Charsets.UTF_8

class XmlTest {

	@Test
	fun generateLongText() {
		assertEquals(
			"""<test>${"shallala \nhallala \n".repeat(1000)}</test>""",
			xml(root = "test") {
				text(
					"shallala \nhallala \n".repeat(1000).reader()
				)
			}.reader(UTF_8).readText()
		)
	}

	@Test
	fun rootElmentAttributes() {
		assertEquals(
			"""<test a="foo" b="bar"/>""",
			xml(root = "test", atts = arrayOf("a" to "foo", "b" to "bar")).reader(
				UTF_8
			).readText()
		)
	}

	@Test
	fun generation() {
		xml(root = "test") {
			!"comment"
			tag("hello", "a" to "1", "b" to "2") {
				cdata("world")
			}
			comment("there is a horse in my garden")
			tag("hello") { -"world" }
			tag("hello") {
				!"lazy"
				"world"()
			}
			text("")
			text { ('a'..'z').forEach { append(it) } }
		}.reader().readText().let {
			assertTrue { it.isNotBlank() }
			assertTrue { it.contains("there is a horse in my garden") }
		}

		assertEquals("<test/>", xml(root = "test").reader().readText())

		assertEquals("<test><pass/></test>", xml(root = "test") { tag("pass") }.reader().readText())

		assertEquals(
			"<test><pass really=\"true\"/></test>",
			xml(root = "test") { tag("pass", "really" to true) }.reader().readText()
		)

		assertEquals(
			"<test><pass really=\"true\"/></test>",
			xml(root = "test") { "pass"("really" to true) }.reader().readText()
		)

		assertEquals(
			"<test><pass really=\"true\"><seriously>no</seriously></pass></test>",
			xml(root = "test") {
				"pass"("really" to true) {
					"seriously" {
						-"no"
					}
				}
			}.reader().readText()
		)

		assertEquals(
			"<test><tag>null</tag></test>",
			xml(root = "test") {
				"tag" {
					-null
				}
			}.reader().readText()
		)

		assertEquals(
			"<test><value>1</value></test>",
			xml(root = "test") {
				"value" { text(1) }
			}.reader().readText()
		)

		assertEquals(
			"<test><value>null</value></test>",
			xml(root = "test") {
				"value" { -null }
			}.reader().readText()
		)

	}

	@Test
	fun prettyPrint() {
		assertEquals(
			"""
<test>
	<pass really="true"/>
</test>""",
			xml(formatMode = FormatMode.PRETTY_TABS, root = "test") { tag("pass", "really" to true) }.reader()
				.readText()
		)
		assertEquals(
			"""
<test>
    <pass really="true"/>
</test>""",
			xml(formatMode = FormatMode.PRETTY_BIG_SPACE_NAZI, root = "test") { tag("pass", "really" to true) }.reader()
				.readText()
		)
		assertEquals(
			"""
<test>
  <pass really="true"/>
</test>""",
			xml(formatMode = FormatMode.PRETTY_SMALL_SPACE_NAZI, root = "test") {
				tag(
					"pass",
					"really" to true
				)
			}.reader()
				.readText()
		)

	}

	@Test
	fun xmlToOutputStream() {
		assertEquals(
			"""
<test>
  <pass really="true"/>
</test>""",
			ByteArrayOutputStream().apply {
				xml(formatMode = FormatMode.PRETTY_SMALL_SPACE_NAZI, root = "test") {
					tag(
						"pass",
						"really" to true
					)
				}
			}.run { toByteArray().toString(UTF_8) }
		)
		assertEquals(
			"""
<test pass="true">
</test>""",
			ByteArrayOutputStream().apply {
				xml(
					formatMode = FormatMode.PRETTY_SMALL_SPACE_NAZI,
					root = "test",
					atts = arrayOf("pass" to true)
				)
			}.run {
				toByteArray().toString(UTF_8)
			}
		)
	}

	@Test
	fun parseXmlAndConsumeMultiple() {
		val results = mutableMapOf<Int, String>()
		"""
			<map>
				<entry>
					<key>0</key>
					<value>zero</value>
				</entry>
				<entry>
					<key>1</key>
					<value>one</value>
				</entry>
				<entry>
					<key>2</key>
					<value>two</value>
				</entry>
			</map>
		""".trimIndent().readAsXmlEventStream {
			"map" {
				"entry" {
					var key = 0
					var value: String?
					"key" - {
						key = elementText.toInt()
					}
					"value" - {
						value = elementText
						results[key] = value!!
					}
				}
			}
		}
		assertEquals(
			mapOf(
				0 to "zero",
				1 to "one",
				2 to "two"
			), results
		)
	}

	@Test
	fun parseXmlAndConsumeMultipleFromDifferentPaths() {
		val results = mutableMapOf<Int, String>()
		"""
			<map>
				<entry>
					<key>0</key>
					<values>
						<value>zero</value>
					</values>
				</entry>
				<entry>
					<key>1</key>
					<values>
						<value>one</value>
					</values>
				</entry>
				<entry>
					<key>2</key>
					<values>
						<value>two</value>
					</values>
				</entry>
			</map>
		""".trimIndent().readAsXmlEventStream {
			"map" {
				"entry" {
					var key = 0
					var value: String?
					"key" - {
						key = elementText.toInt()
					}
					"values" {
						"value" - {
							value = elementText
							results[key] = value!!
						}
					}
				}
			}
		}
		assertEquals(
			mapOf(
				0 to "zero",
				1 to "one",
				2 to "two"
			), results
		)
	}

	@Test
	fun parseXmlAndConsumeMultipleFromVariousPaths() {
		val results = mutableMapOf<String, String>()
		"""
			<map>
				<namespace>1</namespace>
				<entry>
					<key>0</key>
					<values>
						<value>zero</value>
					</values>
				</entry>
				<entry>
					<key>1</key>
					<values>
						<value>one</value>
					</values>
				</entry>
				<entry>
					<key>2</key>
					<values>
						<value>two</value>
					</values>
				</entry>
			</map>
		""".trimIndent().readAsXmlEventStream {
			"map" {
				var namespace = 0
				"namespace" - {
					namespace = elementText.toInt()
				}
				"entry" {
					var key = ""
					var value: String?
					"key" - {
						key = elementText
					}
					"values" {
						"value" - {
							value = elementText
							results["$namespace:$key"] = value!!
						}
					}
				}
			}
		}
		assertEquals(
			mapOf(
				"1:0" to "zero",
				"1:1" to "one",
				"1:2" to "two"
			), results
		)
	}

	@Test
	fun xmlEventStream() {
		assertTrue {
			var value = ""
			"""
			<foo>
				<bar>
					<baz>text-1</baz>
				</bar>
			</foo>
		""".trimIndent().readAsXmlEventStream {
				"foo" {
					"bar" {
						use("baz") {
							value = elementText
						}
					}
				}
			}
			value == "text-1"
		}
		assertTrue {
			var value = ""
			"""
			<foo>
				<bar>
					<baz>text-1</baz>
				</bar>
			</foo>
		""".trimIndent().readAsXmlEventStream {
				"foo" {
					"bar" {
						"baz" - {
							value = this.elementText
						}
					}
				}
			}
			value == "text-1"
		}
		assertTrue {
			val values = mutableListOf<String>()
			"""
			<foo>
				<bar>
					<baz>text-1</baz>
					<baz>text-2</baz>
				</bar>
			</foo>
		""".trimIndent().readAsXmlEventStream {
				"foo" {
					"bar" {
						use("baz") {
							values += elementText
						}
					}
				}
			}
			values == listOf("text-1", "text-2")
		}

		assertTrue {
			val values = mutableListOf<String>()
			"""
			<foo>
				<baz>this is to be ignored because it is not in the bar tag</baz>
				<bar>
					<baz>text-1</baz>
					<baz>text-2</baz>
				</bar>
			</foo>
		""".trimIndent().readAsXmlEventStream {
				"foo" {
					"bar" {
						use("baz") {
							values += elementText
						}
					}
				}
			}
			values == listOf("text-1", "text-2")
		}

		assertTrue {
			val values = mutableListOf<String>()
			"""
			<foo>
				<baz>text-1</baz>
				<bar>
					<baz>text-2</baz>
				</bar>
			</foo>
		""".trimIndent().readAsXmlEventStream {
				"foo" {
					"baz" - {
						values += elementText
					}
					"bar" {
						"baz" - {
							values += elementText
						}
					}
				}
			}
			values == listOf("text-1", "text-2")
		}

	}

	@Test
	fun useAsXmlEventStream() {
		assertTrue {
			val values = mutableListOf<String>()
			"""
			<foo>
				<bar>
					<baz>text</baz>
				</bar>
			</foo>
		""".trimIndent().reader().useAsXmlEventStream {
				"foo" {
					"bar" {
						"baz" - {
							values += elementText
						}
					}
				}
			}
			values == listOf("text")
		}

	}

	@Test
	fun attributes() {
		assertTrue {
			val values = mutableMapOf<String, String>()
			"""
			<foo>
				<bar>
					<baz id="1" value="one"></baz>
					<baz id="2" value="two"></baz>
				</bar>
			</foo>
		""".trimIndent().reader().useAsXmlEventStream {
				"foo" {
					"bar" {
						"baz" - {
							it.readAttributes().apply {
								values[getValue("id")] = getValue("value")
							}
						}
					}
				}
			}
			values == mapOf("1" to "one", "2" to "two")
		}

	}

	@Test
	fun builderValidations() {
		assertThrows<IllegalArgumentException>("one should not be able to add the same tag twice") {
			buildXmlEventStreamReader {
				"tag-1" {}
				"tag-2" {}
				"tag-1" {} // again - should throw exception
			}
		}
		assertThrows<IllegalArgumentException>("one should not be able to add the same tag twice") {
			buildXmlEventStreamReader {
				"tag-1" {
					"tag-2" - {}
					"tag-2" - {}
				}
			}
		}
	}

	@Test
	fun reUseReader() {
		val values = mutableListOf<String>()
		val reader = buildXmlEventStreamReader {
			"foo" {
				"bar" {
					"baz" - {
						values += elementText
					}
				}
			}
		}
		"""
			<foo>
				<bar>
					<baz>first</baz>
				</bar>
			</foo>
			""".trimIndent().byteInputStream().useAsXmlEventStream(reader)
		assertEquals(listOf("first"), values)
		values.clear()

		"""
			<foo>
				<bar>
					<baz>second</baz>
				</bar>
			</foo>
			""".trimIndent().byteInputStream().useAsXmlEventStream(reader)
		assertEquals(listOf("second"), values)

	}

	@Test
	fun testBlank() {
		"""
			<foo>
				<bar>
					<baz>second</baz>
				</bar>
			</foo>
			""".trimIndent().byteInputStream().useAsXmlEventStream {

		}

	}

}