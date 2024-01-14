package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class JoinChannelTest {

    @Test
    fun channelTest(): Unit = runBlocking {
        val channel = Channel<String>()

        launch {
            channel.send("A")
            channel.send("B")
            channel.send("C")
            channel.close()
        }

        launch {
            val messages = mutableListOf<String>()
            for (message in channel) {
                messages.add(message)
            }
            assertEquals(listOf("A", "B", "C"), messages)
        }
    }

	@Test
	fun channelTestWithJoin(): Unit = runBlocking {

		val inputChannels = listOf(
			produce {
				send("A")
				send("C")
			},
			produce {
				send("B")
				send("D")
			},
			produce {
				send("E")
			}

		)

		val joinedChannel = joinChannels(inputChannels) { a, b ->
			a.compareTo(b)
		}

		launch {
			val messages = mutableListOf<String>()
			for (message in joinedChannel) {
				messages.add(message)
			}
			assertEquals(listOf("A", "B", "C", "D", "E"), messages)
		}
	}

	@Test
	fun channelTestWithJoinUnordered(): Unit = runBlocking {
		val inputChannels = listOf(
			produce {  },
			produce {
				send("B")
				send("D")
			},
			produce {  },
			produce {
				send("A")
				send("C")
			},
			produce {  },
			produce {
				send("E")
			},
			produce {  },
		)

		val joinedChannel = joinChannels(inputChannels) { a, b ->
			a.compareTo(b)
		}

		launch {
			val messages = mutableListOf<String>()
			for (message in joinedChannel) {
				messages.add(message)
			}
			assertEquals(listOf("A", "B", "C", "D", "E"), messages)
		}
	}

	@Test
	fun channelTestWithJoinSingleEmpty(): Unit = runBlocking {
		val inputChannels = listOf(
			produce<String> {  },)

		val joinedChannel = joinChannels(inputChannels) { a, b ->
			a.compareTo(b)
		}

		launch {
			val messages = mutableListOf<String>()
			for (message in joinedChannel) {
				messages.add(message)
			}
			assertEquals(listOf<String>(), messages)
		}
	}

	@Test
	fun channelTestWithJoinMultipleEmpty(): Unit = runBlocking {
		val inputChannels = listOf(
			produce<String> {  },
			produce<String> {  },
			produce<String> {  },
			)

		val joinedChannel = joinChannels(inputChannels) { a, b ->
			a.compareTo(b)
		}

		launch {
			val messages = mutableListOf<String>()
			for (message in joinedChannel) {
				messages.add(message)
			}
			assertEquals(listOf<String>(), messages)
		}
	}


	@Test
    fun joinSingleEmptyChannel(): Unit = runBlocking {
        val inputChannel = Channel<String>()
        launch {
            inputChannel.close()
        }

        val outputChannel = joinChannels(listOf(inputChannel)) { first, second -> first.compareTo(second) }

        launch {
            val messages = mutableListOf<String>()
            for (message in outputChannel) {
                messages.add(message)
            }
            assertEquals(listOf<String>(), messages)
        }
    }


    @Test
    fun joinSingleChannel(): Unit = runBlocking {
        val inputChannel = Channel<String>()

        val outputChannel = joinChannels(listOf(inputChannel)) { first, second -> first.compareTo(second) }

        launch {
            inputChannel.send("a")
            delay(1)
            inputChannel.send("b")
            delay(1)
            inputChannel.send("c")
            delay(1)
            inputChannel.close()
        }

        launch {
            val messages = mutableListOf<String>()
            for (message in outputChannel) {
                messages.add(message)
            }
            assertEquals(listOf("a", "b", "c"), messages)
        }
    }

    @Test
    fun joinChannels(): Unit = runBlocking {
        val channelA = Channel<String>()
        val channelB = Channel<String>()
        val channelC = Channel<String>()

        val joinChannel = joinChannels(listOf(channelA, channelB, channelC)) { first, second ->
            first.compareTo(second)
        }

        launch {
            channelA.send("A1")
            channelA.send("A2")
            channelA.send("A3")
            channelA.close()
        }

        launch {
            channelB.send("B1")
            channelB.send("B2")
            channelB.close()
        }

        launch {
            channelC.send("C1")
            channelC.send("C2")
            channelC.send("C3")
            channelC.send("C4")
            channelC.close()
        }

        launch {
            val messages = mutableListOf<String>()
            for (item in joinChannel) {
                messages.add(item)
            }

            assertEquals(
                listOf("A1", "A2", "A3", "B1", "B2", "C1", "C2", "C3", "C4"),
                messages
            )
        }

    }
}
