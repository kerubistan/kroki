package io.github.kerubistan.kroki.coroutines

import io.github.kerubistan.kroki.exceptions.insist
import io.github.kerubistan.kroki.time.H
import io.github.kerubistan.kroki.time.now
import kotlinx.coroutines.*
import java.io.Serializable
import kotlin.reflect.KProperty

/**
 * Delegate interface for all kind of delegates.
 */
interface Delegate<T> : Serializable {
	val value: T
	operator fun getValue(obj: Any?, property: KProperty<*>): T = value
}

private class EagerDelegateImpl<T>(
	private val initializer: () -> T,
	scope: CoroutineScope = GlobalScope
) : Delegate<T> {

	private var job: Deferred<T> = scope.async {
		initializer()
	}

	override val value: T
		get() = runBlocking { job.await() }

}

private class CachedDelegate<T>(
	/**
	 * Coroutine scope to run the load/reload logic in.
	 */
	val scope: CoroutineScope = GlobalScope,
	/**
	 * Time To Live.
	 * After loading, how long do we consider the value to be valid.
	 */
	val ttl: Long = 1.H,
	/**
	 * How many times should should the load be attempted before throwing exception.
	 */
	val retryOnFail: Int = 0,
	/**
	 * Delay between two attempts to load the data.
	 */
	val delayOnLoadError: Long = 0,
	/**
	 * Function literal that gets invoked when error occurs.
	 * One can use this for logging or rethrowing the error.
	 */
	private val errorHandler: (attempt: Int, t: Throwable) -> Unit = { _, _ -> },
	/**
	 * Function literal that loads the data.
	 */
	private val loader: () -> T,
) : Delegate<T> {

	private var lastLoaded: Long? = null
	private var deferred: Deferred<T>

	init {
		deferred = scope.async { load() }
		scope.launch {
			while (true) {
				delay(ttl)
				deferred = async { load() }
			}
		}
	}

	private suspend fun load(): T {
		return insist(retryOnFail, onError = { attempt, throwable -> handleError(attempt, throwable) }) {
			val value = loader()
			lastLoaded = now()
			value
		}
	}

	private suspend fun handleError(attempt: Int, throwable: Throwable) {
		errorHandler(attempt, throwable)
		delay(delayOnLoadError)
	}

	override val value: T
		get() = runBlocking(scope.coroutineContext) {
			deferred.await()
		}

}

/**
 * Creates an eager delegate.
 * An eager delegate will not block the thread to perform the calculation and therefore may avoid CPU usage on the
 * thread where it is created, but it starts a co-routine to perform the operation.
 */
fun <T> eager(scope: CoroutineScope = GlobalScope, initializer: () -> T): Delegate<T> =
	EagerDelegateImpl(initializer, scope)

/**
 * Creates a cached delegate.
 * The value will be refreshed in a regular interval.
 * The retryOnFail, delayOnLoadError and the errorHandler provides some error tolerance.
 */
fun <T> cached(
	scope: CoroutineScope = GlobalScope,
	ttl: Long = 1.H,
	retryOnFail: Int = 0,
	delayOnLoadError: Long = 0,
	errorHandler: (attempt: Int, t: Throwable) -> Unit = { _, _ -> },
	loader: () -> T
): Delegate<T> =
	CachedDelegate(
		scope = scope,
		errorHandler = errorHandler,
		loader = loader,
		ttl = ttl,
		retryOnFail = retryOnFail,
		delayOnLoadError = delayOnLoadError
	)
