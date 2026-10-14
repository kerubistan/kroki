package io.github.kerubistan.kroki.reflection

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.UUID

class IsEnumTest {
	enum class MyEnum {
		FOO, BAR
	}

	data class Blah(
		val id : UUID = UUID.randomUUID(),
	)

	@Test
	fun isEnum() {
		assertTrue { MyEnum::class.isEnum }
		assertFalse { String::class.isEnum }
		assertTrue { Blah::class.isEnum }
	}

}