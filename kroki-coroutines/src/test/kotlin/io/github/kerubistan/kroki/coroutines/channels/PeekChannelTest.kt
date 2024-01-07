package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PeekChannelTest {

    @Test
    fun hasNextWithSingleItem(): Unit = runBlocking {
        // GIVEN
        val channel = Channel<String>(capacity = 10)
        val peekChannel = PeekChannel(channel)

        // WHEN
        launch {
            channel.send("1")
            channel.close()
        }

        // THEN
        launch {
            assertTrue(peekChannel.hasNext())
            peekChannel.next()
            assertFalse(peekChannel.hasNext())
        }

    }

    @Test
    fun hasNextWithMultipleItems(): Unit = runBlocking {
        // GIVEN
        val channel = Channel<String>(capacity = 10)
        val peekChannel = PeekChannel(channel)

        // WHEN
        launch {
            repeat(10) {
                channel.send(it.toString())
            }
            channel.close()
        }

        // THEN
        launch {
            repeat(10) {
                assertTrue(peekChannel.hasNext())
                assertEquals(it.toString(), peekChannel.next())
            }
            assertFalse(peekChannel.hasNext())
        }

    }


    @Test
    fun hasNextEmptyChannel() = runBlocking {
        // GIVEN
        val channel = Channel<String>()
        val peekChannel = PeekChannel(channel)

        // WHEN
        channel.close()

        // THEN
        assertFalse(peekChannel.hasNext())
    }

}
