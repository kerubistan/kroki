package io.github.kerubistan.kroki.coroutines

import kotlinx.coroutines.*
import java.io.Serializable
import kotlin.reflect.KProperty

/**
 * Delegate interface for all kind of reference (weak, soft) delegates.
 */
interface EagerDelegate<T> : Serializable {
    val value: T
    operator fun getValue(obj: Any?, property: KProperty<*>): T = value
}

private class EagerDelegateImpl<T>(
    private val initializer: () -> T,
    scope: CoroutineScope = GlobalScope
) : EagerDelegate<T> {

    private var job: Deferred<T> = scope.async {
        initializer()
    }

    override val value: T
        get() = runBlocking { job.await() }

}

/**
 * Creates am eager delegate.
 * An eager delegate will not immediately perform the calculation and therefore may avoid CPU usage on the
 * thread where it is created, but it starts a co-routine to perform the operation.
 */
fun <T> eager(scope: CoroutineScope = GlobalScope, initializer: () -> T): EagerDelegate<T> = EagerDelegateImpl(initializer, scope)
