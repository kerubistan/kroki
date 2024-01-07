package io.github.kerubistan.kroki.coroutines.channels

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MergeChannelKtTest {

    @Test
    fun mergeChannel() = runBlocking {
        val channel = Channel<Pair<String, Int>>(4)
        val mergeChannel = mergeChannel(channel, key = Pair<String, Int>::first) { first, second ->
            first.first to first.second + second.second
        }

        launch {
            channel.send("A" to 1)
            channel.send("A" to 2)
            channel.close()
        }

        val iterator = mergeChannel.iterator()
        assertTrue(iterator.hasNext())
        val pair = iterator.next()
        assertFalse(iterator.hasNext())
        assertEquals("A" to 3, pair)
    }

    @Test
    fun mergeChannelEmpty() = runBlocking {
        val channel = Channel<Pair<String, Int>>()
        val mergeChannel = mergeChannel(channel, key = Pair<String, Int>::first) { _, _ ->
            throw IllegalArgumentException("Shouldn't ever be called, since the input channel remains empty")
        }

        launch {
            channel.close()
        }

        assertFalse(mergeChannel.iterator().hasNext())
    }

    @Test
    fun mergeChannelMultiple() = runBlocking {
        val channel = Channel<Pair<String, Int>>()
        val mergeChannel = mergeChannel(channel, key = Pair<String, Int>::first) { first, second ->
            first.first to first.second + second.second
        }

        launch {
            channel.send("A" to 1)
            channel.send("A" to 2)
            channel.send("B" to 1)
            channel.send("C" to 2)
            channel.send("C" to 1)
            channel.close()
        }

        val a = mergeChannel.receive()
        val b = mergeChannel.receive()
        val c = mergeChannel.receive()
        assertFalse(mergeChannel.iterator().hasNext())
        assertEquals("A" to 3, a)
        assertEquals("B" to 1, b)
        assertEquals("C" to 3, c)
    }

}
