package io.github.kerubistan.kroki.collections.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

class KrokiCollectionsJacksonTest {
	@Test
	fun deserializeObject() {
		val mapper = ObjectMapper().registerModules(
			KotlinModule.Builder().build(),
			KrokiCollectionsModule()
		)
		val person = mapper.reader().readValue(
			"""
				{
					"id": "${UUID.randomUUID()}",
					"name": "John Brown",
					"identifiers": [
						{
							"first" : "PassportNr",
							"second" : "ABC-1234567890"
						},
						{
							"first" : "NationalIdNr",
							"second" : "98237-492372-34223"
						}
					]
				}
			""".trimIndent(),
			Person::class.java
		)

		person.identifiers.size shouldBe 2
	}
}