package io.github.kerubistan.kroki.reflection

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.UUID

class IsComparableTest {

	data class TestSomething(val something: String)

	@Test
	fun isComparable() {
		assertTrue { Int::class.isComparable }
		assertTrue { Long::class.isComparable }
		assertTrue { Double::class.isComparable }
		assertTrue { String::class.isComparable }
		assertTrue { UUID::class.isComparable }
		assertFalse { TestSomething::class.isComparable }
	}

}