package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class DelayTest {
	@Test
	fun testDelay() = runBlocking {
		// GIVEN
		val inputs = listOf("foo", "hello world", "bar", "baz", "")
		val inputChannel = Channel<String>()
		val delayedChannel = delay(inputChannel) {
			it.length.toLong()
		}

		// WHEN
		inputs.forEach {
			inputChannel.send(it)
		}
		inputChannel.close()

		val results = mutableListOf<String>()
		for (message in delayedChannel) {
			results.add(message)
		}

		// THEN
		assertEquals(inputs.size, results.size)
	}

	@Test
	fun testEmptyDelay() = runBlocking {
		val inputChannel = Channel<String>()
		val delayedChannel = delay(inputChannel) {
			it.length.toLong()
		}

		// WHEN
		inputChannel.close()

		val results = mutableListOf<String>()
		for (message in delayedChannel) {
			results.add(message)
		}

		// THEN
		assertEquals(0, results.size)

	}
}