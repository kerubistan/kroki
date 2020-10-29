package io.github.kerubistan.kroki.flyweight

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class DeduplicateKtTest {

	@Test
	fun deduplicate() {

		data class Astronaut(
			val firstName : String,
			val lastName : String,
			val courage : Int,
			val stupidity : Int
		)

		val candidates = ObjectMapper().registerModule(KotlinModule(strictNullChecks = true)).readValue<List<Astronaut>>(
			"""
			[
				{
					"firstName" : "Bob",
					"lastName" : "Kerman",
					"courage" : 1,
					"stupidity" : 10
				},
				{
					"firstName" : "Bill",
					"lastName" : "Kerman",
					"courage" : 1,
					"stupidity" : 10
				},
				{
					"firstName" : "Jebediah",
					"lastName" : "Kerman",
					"courage" : 1,
					"stupidity" : 10
				}
			]
			""".trimIndent()
		).map { it.deduplicate() }

		assertTrue(candidates.all { it.lastName === candidates[0].lastName })

	}

	@Test
	fun deepDeduplicate() {

		data class Name (
			val first : String,
			val last : String
		)

		data class Astronaut(
			val name: Name,
			val courage : Int,
			val stupidity : Int
		)

		val candidates = ObjectMapper().registerModule(KotlinModule(strictNullChecks = true)).readValue<List<Astronaut>>(
			"""
			[
				{
					"name" : {
						"first" : "Bob",
						"last" : "Kerman"
					},
					"courage" : 1,
					"stupidity" : 10
				},
				{
					"name" : {
						"first" : "Bill",
						"last" : "Kerman"
					},
					"courage" : 1,
					"stupidity" : 10
				},
				{
					"name" : {
						"first" : "Jebediah",
						"last" : "Kerman"
					},
					"courage" : 1,
					"stupidity" : 10
				}
			]
			""".trimIndent()
		).map { it.deduplicate() }

		assertTrue(candidates.all { it.name.last === candidates[0].name.last })

	}
}