package io.github.kerubistan.kroki.coroutines

import io.github.kerubistan.kroki.objects.comparator
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.channels.Channel.Factory.RENDEZVOUS
import kotlinx.coroutines.selects.SelectClause1
import kotlinx.coroutines.selects.SelectClause2
import java.util.*

/**
 * Hides a coroutine between two channels, uniting them as a single channel.
 */
internal open class ProcessChannel<T>(
	internal val inChannel: Channel<T>,
	internal val outChannel: Channel<T>
) : Channel<T> {
	@ExperimentalCoroutinesApi
	override val isClosedForReceive: Boolean
		get() = outChannel.isClosedForReceive

	@ExperimentalCoroutinesApi
	override val isClosedForSend: Boolean
		get() = inChannel.isClosedForSend

	@ExperimentalCoroutinesApi
	override val isEmpty: Boolean
		get() = outChannel.isEmpty

	override val onReceive: SelectClause1<T> get() = outChannel.onReceive

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

	override suspend fun send(element: T) = inChannel.send(element)
	override val onReceiveCatching: SelectClause1<ChannelResult<T>>
		get() = TODO("not implemented")

	override suspend fun receiveCatching(): ChannelResult<T> {
		TODO("not implemented")
	}

	override fun tryReceive(): ChannelResult<T> {
		TODO("not implemented")
	}

	override fun trySend(element: T): ChannelResult<Unit> {
		TODO("not implemented")
	}

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
	inChannel = Channel(RENDEZVOUS),
	// output channel is rendezvous channel because we may still
	// get higher priority input meanwhile and we will send that
	// when output consumer is ready to take it
	outChannel = Channel(RENDEZVOUS)
) {
	private val buffer = PriorityQueue(comparator)

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
					received?.let { buffer.add(it) }
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

@Suppress("deprecated")
class TransformChannel<I, O>(val transform: (I) -> O, private val wrapped: Channel<O>) : ReceiveChannel<O>,
	SendChannel<I> {

	// receive channel

	@ExperimentalCoroutinesApi
	override val isClosedForReceive: Boolean
		get() = wrapped.isClosedForReceive

	@ExperimentalCoroutinesApi
	override val isEmpty: Boolean
		get() = wrapped.isEmpty
	override val onReceive: SelectClause1<O>
		get() = wrapped.onReceive

	override fun cancel(cause: Throwable?): Boolean {
		wrapped.cancel()
		return true
	}

	override fun cancel(cause: CancellationException?) {
		wrapped.cancel(cause)
	}

	override fun iterator(): ChannelIterator<O> =
		wrapped.iterator()

	override fun poll(): O? = wrapped.poll()

	override suspend fun receive(): O = wrapped.receive()

	// send channel

	@ExperimentalCoroutinesApi
	override val isClosedForSend: Boolean
		get() = wrapped.isClosedForSend

	override val onSend: SelectClause2<I, SendChannel<I>>
		get() = TODO("not implemented")

	override fun close(cause: Throwable?): Boolean = wrapped.close(cause)

	@ExperimentalCoroutinesApi
	override fun invokeOnClose(handler: (cause: Throwable?) -> Unit) = wrapped.invokeOnClose(handler)

	override fun offer(element: I): Boolean = wrapped.offer(transform(element))

	override suspend fun send(element: I) {
		wrapped.send(transform(element))
	}

	override val onReceiveCatching: SelectClause1<ChannelResult<O>>
		get() = wrapped.onReceiveCatching

	override suspend fun receiveCatching(): ChannelResult<O> {
		TODO("not implemented")
	}

	override fun tryReceive(): ChannelResult<O> {
		TODO("not implemented")
	}

	override fun trySend(element: I): ChannelResult<Unit> {
		TODO("not implemented")
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

@ExperimentalCoroutinesApi
inline fun <reified T : Comparable<T>> priorityChannel(
	maxCapacity: Int = 4096,
	scope: CoroutineScope = GlobalScope
): Channel<T> = priorityChannel(maxCapacity, scope, T::class.comparator())

/**
 * Creates a channel that transforms the input to the required output.
 * @param channel output channel
 * @param transform transformation logic
 */
fun <I, O> transformChannel(channel: Channel<O> = Channel(), transform: (I) -> O) = TransformChannel(transform, channel)
