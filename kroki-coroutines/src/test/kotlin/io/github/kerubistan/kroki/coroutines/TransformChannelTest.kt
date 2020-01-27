package io.github.kerubistan.kroki.coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.RENDEZVOUS
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.Exception

class TransformChannelTest {

    @Test
    fun send() = runBlocking {
        val channel = transformChannel<Int, String>(channel = Channel(10)) {
            "transformed: $it"
        }
        assertNull(channel.poll())
        channel.send(1)
        assertEquals("transformed: 1", channel.receive())
    }

    @Test
    fun offer() = runBlocking {
        val channel = transformChannel<Int, String>(channel = Channel(RENDEZVOUS)) {
            "transformed: $it"
        }
        assertFalse(channel.offer(1))
        assertNull(channel.poll())
    }

    @Test
    fun iterator() = runBlocking {
        val channel = transformChannel<Int, String>(channel = Channel(10)) {
            "$it"
        }
        async {
            (0 until 10).forEach {
                channel.send(it)
            }
            channel.close()
        }.start()
        val result = async {
            val items = mutableListOf<String>()
            for(msg in channel) {
                items += msg
            }
            items
        }.await()

        assertEquals(listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"), result)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun close() = runBlocking {
        val channel = transformChannel<Int, String>(channel = Channel(RENDEZVOUS)) {
            "transformed: $it"
        }
        channel.close()
        try {
            channel.send(1)
            fail<Exception>("exception expected")
        } catch (e: ClosedSendChannelException) {
            //expected
        }
        assertTrue(channel.isClosedForSend)
        Unit
    }

    @ExperimentalCoroutinesApi
    @Test
    fun isEmpty() {
        val channel = transformChannel<Int, String>(channel = Channel(RENDEZVOUS)) {
            "transformed: $it"
        }
        assertTrue(channel.isEmpty)

    }

}