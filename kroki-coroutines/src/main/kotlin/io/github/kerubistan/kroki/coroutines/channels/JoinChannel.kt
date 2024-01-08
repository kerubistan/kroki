package io.github.kerubistan.kroki.coroutines.channels

import io.github.kerubistan.kroki.objects.isGreaterThan
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

internal class PeekChannel<X>(channel: ReceiveChannel<X>) {
    private val iterator = channel.iterator()
    private var last: X? = null
    suspend fun peek(): X {
        return if (last != null) {
            last!!
        } else if (iterator.hasNext()) {
            last = iterator.next()
            last!!
        } else {
            throw ClosedReceiveChannelException("")
        }
    }

    fun next(): X {
        return if (last == null) {
            iterator.next()
        } else {
            val ret = last!!
            last = null
            ret
        }
    }

    suspend fun hasNext(): Boolean = if (last != null) {
        true
    } else {
        if (iterator.hasNext()) {
            last = iterator.next()
            true
        } else {
            false
        }
    }
}


internal suspend fun <X> List<PeekChannel<X>>.maxOrNull(comparator: Comparator<X>): PeekChannel<X>? {
    var max: PeekChannel<X>? = null
    this.forEach {
        if (it.hasNext()) {
            val channelValue = it.peek()
            if (max == null || max!!.peek()!!.isGreaterThan(channelValue, comparator)) {
                max = it
            }
        }
    }
    return max
}

const val IDEAL_JOIN_COUNT = 16
suspend fun <T> CoroutineScope.twoLevelJoin(
    inputChannels: List<Channel<T>>,
	outChannelCapacity: Int = 128,
    comparator: Comparator<T>
): Channel<T> = if (inputChannels.size < IDEAL_JOIN_COUNT) {
    joinChannels(inputChannels, outChannelCapacity, comparator)
} else {
    joinChannels(inputChannels.chunked(IDEAL_JOIN_COUNT).map {
        joinChannels(inputChannels, outChannelCapacity, comparator)
    }, outChannelCapacity, comparator)
}

suspend fun <T> CoroutineScope.joinChannels(
    inputChannels: List<ReceiveChannel<T>>,
	outChannelCapacity: Int = 128,
    comparator: Comparator<T>
): Channel<T> {
    val outChannel = Channel<T>(capacity = outChannelCapacity)

    launch {
        val peekChannels = inputChannels.map { PeekChannel(it) }
        var head = peekChannels.maxOrNull(comparator)
        while (head != null) {
            val element = head.next()
            outChannel.send(element)
            head = peekChannels.maxOrNull(comparator)
        }
        outChannel.close()
    }
    return outChannel
}
