package io.github.kerubistan.kroki.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.channels.Channel.Factory.RENDEZVOUS
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.System.currentTimeMillis
import kotlin.random.Random
import kotlin.test.assertEquals

class PriorityChannelTest {
	@ExperimentalCoroutinesApi
	@Test
	fun sendAndReceive() = runBlocking {

		suspend fun verify(
			randomNumbers: List<Long>,
			channel: Channel<Long>
		) {
			async {
				randomNumbers.forEachIndexed() { index, item ->
					channel.send(item)
				}
				channel.close()
			}.start()

			val received = withContext(Dispatchers.Default) {
				val result = mutableListOf<Long>()
				for (num in channel) {
					result.add(num)
				}
				result.toList()
			}

			assertEquals(randomNumbers.size, received.size)
			assertEquals(randomNumbers.sorted(), received.sorted())
		}

		val random = Random(currentTimeMillis())

		val randomNumbers = (0 until 100).map { random.nextLong() }

		verify(randomNumbers, priorityChannel(
			maxCapacity = 100,
			comparator = { a: Long, b: Long -> a.compareTo(b) }
		))

		verify(randomNumbers, Channel(RENDEZVOUS))

		verify(randomNumbers, Channel(UNLIMITED))

		verify(randomNumbers, Channel(BUFFERED))

		verify(listOf(), priorityChannel(
			maxCapacity = 100,
			comparator = { a: Long, b: Long -> a.compareTo(b) }
		))

		verify(listOf(), Channel(RENDEZVOUS))

		verify(listOf(), Channel(UNLIMITED))

		verify(listOf(), Channel(BUFFERED))

	}

	@ExperimentalCoroutinesApi
	@Test
	fun validations() {
		assertThrows<IllegalArgumentException>("too small buffer") {
			priorityChannel<String>(
				maxCapacity = 0,
				comparator = Comparator { first, second -> first.compareTo(second) })
		}
		assertThrows<IllegalArgumentException>("too small buffer - same thing with the inline method") {
			priorityChannel<String>(
				maxCapacity = 0
			)
		}
	}

}