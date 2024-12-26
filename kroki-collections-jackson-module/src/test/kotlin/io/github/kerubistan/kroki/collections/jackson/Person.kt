package io.github.kerubistan.kroki.collections.jackson

import java.util.UUID

data class Person(
	val id : UUID,
	val name : String,
	val identifiers : List<Pair<String, String>>
)
