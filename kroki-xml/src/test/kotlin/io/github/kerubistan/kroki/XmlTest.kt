package io.github.kerubistan.kroki

import org.junit.Test
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
				+"world"
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

	}

}