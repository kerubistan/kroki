package io.github.kerubistan.kroki.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.RENDEZVOUS
import kotlinx.coroutines.channels.ChannelIterator
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.ValueOrClosed
import kotlinx.coroutines.selects.SelectClause1
import kotlinx.coroutines.selects.SelectClause2
import java.util.*

/**
 * Hides a coroutine between two channels, uniting them as a single channel.
 */
internal open class ProcessChannel<T>(
	internal val inChannel: Channel<T>,
	internal val outChannel: Channel<T>
) :	Channel<T> {
	@ExperimentalCoroutinesApi
	override val isClosedForReceive: Boolean
		get() = outChannel.isClosedForReceive
	@ExperimentalCoroutinesApi
	override val isClosedForSend: Boolean
		get() = inChannel.isClosedForSend
	@ExperimentalCoroutinesApi
	override val isEmpty: Boolean
		get() = outChannel.isEmpty
	@ExperimentalCoroutinesApi
	override val isFull: Boolean
		get() = false

	override val onReceive: SelectClause1<T> get() = outChannel.onReceive
	@InternalCoroutinesApi
	override val onReceiveOrClosed: SelectClause1<ValueOrClosed<T>>
		get() = outChannel.onReceiveOrClosed

	@ObsoleteCoroutinesApi
	override val onReceiveOrNull: SelectClause1<T?>
		get() = outChannel.onReceiveOrNull

	override val onSend: SelectClause2<T, SendChannel<T>> get() = inChannel.onSend

	@Deprecated(level = DeprecationLevel.HIDDEN, message = "deprecated in interface but need to implement it")
	override fun cancel(cause: Throwable?): Boolean {
		outChannel.cancel()
		return true
	}

	override fun cancel(cause: CancellationException?) = outChannel.cancel(cause)

	override fun close(cause: Throwable?): Boolean = inChannel.close(cause)

	@ExperimentalCoroutinesApi
	override fun invokeOnClose(handler: (cause: Throwable?) -> Unit) {
		inChannel.invokeOnClose(handler)
	}

	override fun iterator(): ChannelIterator<T> = outChannel.iterator()

	override fun offer(element: T): Boolean = inChannel.offer(element)

	override fun poll(): T? = outChannel.poll()

	override suspend fun receive(): T = outChannel.receive()

	@InternalCoroutinesApi
	override suspend fun receiveOrClosed(): ValueOrClosed<T> = outChannel.receiveOrClosed()

	@ObsoleteCoroutinesApi
	override suspend fun receiveOrNull(): T? = outChannel.receiveOrNull()

	override suspend fun send(element: T) = inChannel.send(element)

}

@ExperimentalCoroutinesApi
internal class PriorityChannel<T>(
	private val maxCapacity: Int = 4096,
	scope: CoroutineScope = GlobalScope,
	comparator: Comparator<T>
) : ProcessChannel<T>(
	// why a rendezvous channel should be the input channel?
	// because we buffer and sort the messages in the co-routine
	// that is where the capacity constraint is enforced
	// and the buffer we keep sorted, the input channel we can't
	inChannel = Channel<T>(RENDEZVOUS),
	// output channel is rendezvous channel because we may still
	// get higher priority input meanwhile and we will send that
	// when output consumer is ready to take it
	outChannel = Channel<T>(RENDEZVOUS)
) {
	private val buffer = PriorityQueue<T>(comparator)

	private fun PriorityQueue<T>.isNotFull() = this.size < maxCapacity

	private fun PriorityQueue<T>.isFull() = this.size >= maxCapacity

	// non-suspending way to get all messages available at the moment
	// as long as we have anything to receive and the buffer is not full
	// we should keep receiving
	private fun tryGetSome() {
		if (buffer.isNotFull()) {
			var received = inChannel.poll()
			if (received != null) {
				buffer.add(received)
				while (buffer.isNotFull() && received != null) {
					received = inChannel.poll()
					if (received != null) {
						buffer.add(received)
					}
				}
			}
		}
	}

	private suspend fun getAtLeastOne() {
		buffer.add(inChannel.receive())
		tryGetSome()
	}

	private suspend fun trySendSome() {
		when {
			buffer.isEmpty() -> {
				yield()
			}
			buffer.isFull() -> {
				outChannel.send(buffer.poll())
			}
			else -> {
				while (buffer.isNotEmpty() && outChannel.offer(buffer.peek())) {
					buffer.poll()
					tryGetSome()
				}
			}
		}
	}

	private suspend fun sendAll() {
		while (buffer.isNotEmpty()) {
			outChannel.send(buffer.poll())
		}
	}

	init {
		require(maxCapacity >= 2) {
			"priorityChannel maxCapacity < 2 does not make any sense"
		}

		scope.async {
			try {
				getAtLeastOne()

				while (!inChannel.isClosedForReceive) {
					trySendSome()
					tryGetSome()
				}
			} finally {
				// input channel closed, send the buffer to out channel
				sendAll()
				// and finally close the output channel, signaling that that this was it
				outChannel.close()
			}
		}.start()

	}
}

/**
 * Creates a channel that always outputs the highest priority element received so far.
 * It is important to note here that while the coroutine API channels are all FIFO, this
 * one is not.
 * @param maxCapacity the number of items the channel can keep inside
 * @param scope coroutine-scope to run the sorting in
 * @param comparator a comparator for the
 */
@ExperimentalCoroutinesApi
fun <T> priorityChannel(
	maxCapacity: Int = 4096,
	scope: CoroutineScope = GlobalScope,
	comparator: Comparator<T>
): Channel<T> = PriorityChannel(maxCapacity, scope, comparator)

