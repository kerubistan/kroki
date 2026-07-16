package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.time.Duration

class TimeWindowTest {

	@Test
	fun timeWindow(): Unit = runBlocking {
		val inputs = produce {
			repeat(100) { nr ->
				send(nr)
				delay(10)
			}
		}

		val windows = timeWindow(inputs, Duration.parse("100ms"))

		assertFalse { windows.toList().isEmpty() }
	}
}