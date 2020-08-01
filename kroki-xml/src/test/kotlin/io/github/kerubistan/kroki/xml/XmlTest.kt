package io.github.kerubistan.kroki.xml

import org.junit.Test
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class XmlTest {
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
						- "no"
					}
				}
			}.reader().readText()
		)

		assertEquals(
			"<test><tag>null</tag></test>",
			xml(root = "test") {
				"tag" {
					- null
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
				"value" { - null }
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
			ByteArrayOutputStream().let {
				it.use {output ->
					xml(formatMode = FormatMode.PRETTY_SMALL_SPACE_NAZI, root = "test", out = output) {
						tag(
							"pass",
							"really" to true
						)
					}
				}
				it.toByteArray().toString(Charsets.UTF_8)
			}
		)
		assertEquals(
			"""
<test pass="true">
</test>""",
			ByteArrayOutputStream().let {
				it.use {output ->
					xml(
						formatMode = FormatMode.PRETTY_SMALL_SPACE_NAZI,
						root = "test",
						out = output,
						atts = *arrayOf("pass" to true)
					)
				}
				it.toByteArray().toString(Charsets.UTF_8)
			}
		)
	}

}
	@Test
	fun parseXml() {
		assertEquals(
			"""
				<xml>
					<data>hello world</data>
				</xml>
			""".trimIndent().parseAsXml {
				tag("xml") {
					tag("data") { yield(text()) }
				}
			},
			"hello world"
		)

		assertEquals(
			"""
				<xml>
					<blah>something else</blah>
					<data>hello world</data>
					<blah>something else again</blah>
				</xml>
				""".trimIndent().parseAsXml {
				tag("xml") {
					tag("data") { yield(text()) }
				}
			},
			"hello world"
		)

		assertEquals(
			"""<xml>
					<blah>something else</blah>
					<data>hello world</data>
					<blah>something else again</blah>
				</xml>""".trimIndent().parseAsXml {
				tag("xml") {
					tag("data") { yield(text()) }
				}
			},
			"hello world"
		)

		assertEquals(
			"""<xml>
					<blah>something else</blah>
					<data>hello world</data>
					<blah>something else again</blah>
				</xml>""".trimIndent().parseAsXml { "xml" / "data" / this::text },
			"hello world"
		)

		assertEquals(
			"""<xml>
					<blah>1</blah>
					<data>2</data>
					<blah>3</blah>
				</xml>""".trimIndent().parseAsXml { "xml" / "data" / this::int },
			2
		)

		assertEquals(
			"""<xml>
					<blah><data>1</data></blah>
					<data>2</data>
					<blah><data>3</data></blah>
				</xml>
				""".trimIndent().parseAsXml { "xml" / "data" / this::int },
			2
		)

		assertEquals(
			"""<person>
					<name>John Doe</name>
					<age>25</age>
					<gender>Male</gender>
				</person>""".trimIndent().parseAsXml {
				"person" {
					Person(
						name = -"name",
						age = -"age" / this::int,
						gender = Gender.valueOf(-"gender")
					)
				}
			},
			Person(
				name = "John Doe",
				age = 25,
				gender = Gender.Male
			)
		)

		assertEquals(
			"""
				<persons>
					<person>
						<name>J. I. Joe</name>
						<age>24</age>
						<gender>Male</gender>
					</person>
					<person>
						<name>J. I. Jane</name>
						<age>24</age>
						<gender>Female</gender>
					</person>
				</persons>
				""".trimIndent().parseAsXml {
				"persons" {
					"person" * {
						Person(
							name = -"name",
							age = -"age" / this::int,
							gender = Gender.valueOf(-"gender")
						)
					}
				}
			},
			listOf(
				Person(
					name = "J. I. Joe",
					age = 24,
					gender = Gender.Male
				),
				Person(
					name = "J. I. Jane",
					age = 24,
					gender = Gender.Female
				)
			)
		)

		assertEquals(
			"""
				<employees>
					<employee>
						<name>Bob</(bob>
						<skills>
							<skill>
								<name>Engineering</name>
								<level>"rockstar"</level>
							</skill>
							<skill>
								<name>Music</name>
								<level>newbie</level>
							</skill>
						</skills>
					</employee>
					<employee>
						<name>Mike</bob>
						<skills>
							<skill>
								<name>Engineering</name>
								<level>professional</level>
							</skill>
						</skills>
					</employee>
				</employees>
			""".trimIndent().parseAsXml {
				"employees" {
					"employee" * {
						Employee(
							name = -"name",
							skills = ("skills" * {
								Skill.valueOf(-"name")
							})
						)
					}
				}
			},
			listOf(
				Employee(name = "Bob", skills = setOf(Skill.Engineering, Skill.Music)),
				Employee(name = "Mike", skills = setOf(Skill.Engineering))
			)
		)

		assertEquals(
			"""
				<employees>
					<employee boss="true">
						<name>Bob</(bob>
						<skills>
							<skill>
								<name>Engineering</name>
								<level>"rockstar"</level>
							</skill>
							<skill>
								<name>Music</name>
								<level>newbie</level>
							</skill>
						</skills>
					</employee>
					<employee>
						<name>Mike</bob>
						<skills>
							<skill>
								<name>Engineering</name>
								<level>professional</level>
							</skill>
						</skills>
					</employee>
				</employees>
			""".trimIndent().parseAsXml {
				"employees" {
					("employee"("boss" to true)) {
						Employee(
							name = -"name",
							skills = ("skills" * {
								Skill.valueOf(-"name")
							})
						)
					}
				}
			},
			Employee(name = "Bob", skills = setOf(Skill.Engineering, Skill.Music))
		)


	}

}








