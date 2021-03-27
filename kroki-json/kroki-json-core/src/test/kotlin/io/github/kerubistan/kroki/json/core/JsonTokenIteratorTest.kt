package io.github.kerubistan.kroki.json.core

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import kotlin.test.*

class JsonTokenIteratorTest {

	@Test
	fun iterate() {
		for(node in ObjectMapper().createParser(
			""" 
			{
				"foo": "bar",
				"baz" : [1],
				"boolean":true 
			} 
		""".trimIndent()
		).iterator()) {
			assertNotNull(node)
		}

		ObjectMapper().createParser(
			""" 
			{
				"foo": "bar",
				"baz" : [1],
				"boolean":true 
			} 
		""".trimIndent()
		).iterator().forEach {
			assertNotNull(it)
		}

		ObjectMapper().createParser(
			""" 
			{
				"foo": "bar",
				"baz" : [1],
				"boolean":true 
			} 
		""".trimIndent()
		).iterator().let {
			assertTrue(it.hasNext())
			while (it.hasNext()) {
				assertNotNull(it.next())
			}
			assertFalse(it.hasNext())
			try {
				it.next()
				fail("exception expected")
			} catch (ise : IllegalStateException) {
				// expected
			}
		}

	}

}