package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FlowTest {
	val inputs = listOf("foo", "bar", "baz")
	@Test
	fun toFlow() = runBlocking {
		val channel = Channel<String>()
		launch { inputs.forEach { channel.send(it) }; channel.close() }

		assertEquals(inputs, toFlow(channel).toList())
	}

	@Test
	fun toChannel() = runBlocking {
		val flow = flow {
			inputs.forEach { emit(it) }
		}

		assertEquals(inputs, toChannel(flow).toList())
	}

}