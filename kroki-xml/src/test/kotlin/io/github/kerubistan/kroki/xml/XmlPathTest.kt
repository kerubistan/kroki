package io.github.kerubistan.kroki.xml

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class XmlPathTest {
	@Test
	fun xmlPath() {
		assertEquals(
			"PASS",
			XmlPath.of("//foo/bar@baz")
				.evaluate(
					"""
					<foo>
						<bar baz="PASS">
					</foo>
				""".trimIndent()
				)
		)

		assertEquals(
			"PASS",
			XmlPath.of("//foo/bar/text()")
				.evaluate(
					"""
					<foo>
						<bar>PASS</bar>
					</foo>
				""".trimIndent()
				)
		)

		assertEquals(
			"PASS",
			XmlPath.of("//foo/text()")
				.evaluate(
					"""
					<foo>PASS</foo>
				""".trimIndent()
				)
		)

	}
}
