package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class BatchKtTest {

	@Test
	fun batch() {
		runBlocking {
			val batchChannel = batch(
				produce { (0 until 55).forEach { send(it) } }, chunkSize = 10
			)

			var messages = mutableListOf<List<Int>>()
			for (message in batchChannel) {
				messages.add(message)
			}
			assertEquals(
				listOf(
					(0..9).toList(),
					(10..19).toList(),
					(20..29).toList(),
					(30..39).toList(),
					(40..49).toList(),
					(50..54).toList(),
				), messages
			)
		}
	}
}