package io.github.kerubistan.kroki.flyweight

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

class FlyWeightKtTest {

	@Test
	fun flyweight() {

		data class Astronaut(
			val firstName: String,
			val lastName: String,
			val courage: Int,
			val stupidity: Int
		)

		val candidates = objectMapper().readValue<List<Astronaut>>(
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
		).map { it.flyWeight() }

		assertTrue(candidates.all { it.lastName === candidates[0].lastName })

	}

	@Test
	fun deepFlyweight() {

		data class Name(
			val first: String,
			val last: String
		)

		data class Astronaut(
			val name: Name,
			val courage: Int,
			val stupidity: Int
		)

		val candidates = objectMapper().readValue<List<Astronaut>>(
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
		).map { it.flyWeight() }

		assertTrue(candidates.all { it.name.last === candidates[0].name.last })

	}

	private fun objectMapper() = ObjectMapper().registerModule(KotlinModule(strictNullChecks = true))

	enum class Status {
		TODO,
		DONE
	}

	@Test
	fun flyWeightEnum() {

		data class Task(
			val status: Status,
			val description: String,
			val id: UUID,
			val requestedBy: String
		)

		val tasks = objectMapper().readValue<List<Task>>(
			"""
			[
				{
					"status" : "TODO",
					"description" : "shopping",
					"id" : "e3e1700f-9cdf-4bab-8d71-7c96c570bce2",
					"requestedBy" : "wife"
				},
				{
					"status" : "TODO",
					"description" : "playground",
					"id" : "08214867-1a60-4ec4-a631-c090b4ccaec1",
					"requestedBy" : "kid"
				},
				{
					"status" : "DONE",
					"description" : "cartoon",
					"id" : "a75540b3-1544-43be-bfc6-68ff5e88734c",
					"requestedBy" : "kid"
				},
				{
					"status" : "TODO",
					"description" : "work",
					"id" : "73c65aa1-d176-4798-bf57-b24e9b2db594",
					"requestedBy" : "boss"
				},
				{
					"status" : "DONE",
					"description" : "timesheet",
					"id" : "2907e04c-d6c9-4b72-ae5e-eb21cc9e5312",
					"requestedBy" : "boss"
				},
				{
					"status" : "TODO",
					"description" : "sleep",
					"id" : "1b40a29a-e419-451f-8f84-20bd93a16139",
					"requestedBy" : "me"
				}
			]
		""".trimIndent()
		).map { it.flyWeight() }

		tasks.filter { it.requestedBy == "me" }.let { myTasks ->
			assertTrue(myTasks.all { it.requestedBy === myTasks[0].requestedBy })
		}

		tasks.filter { it.requestedBy == "boss" }.let { workTasks ->
			assertTrue(workTasks.all { it.requestedBy === workTasks[0].requestedBy })
		}

	}

}