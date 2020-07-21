package io.github.kerubistan.kroki.xml

import io.github.kerubistan.kroki.xml.Gender

data class Person(
	val name : String,
	val age : Int,
	val gender: Gender
)