package io.github.kerubistan.kroki.iteration

class MappingIterator<S, T>(private val internal: Iterator<S>, private val mapper: (S) -> T) : Iterator<T> {

	override fun hasNext(): Boolean = internal.hasNext()

	override fun next(): T = mapper(internal.next())

}

fun <S, T> Iterator<S>.map(mapper: (S) -> T): Iterator<T> = MappingIterator(this, mapper)

fun <T, K, V> Iterator<T>.toMap(map: T.() -> Pair<K, V>): Map<K, V> = mutableMapOf<K, V>().let { target ->
	this.forEach {
		val (key, value) = it.map()
		target[key] = value
	}
	target
}

/**
 * Builds a list in case only an iterator is available.s
 */
fun <T> Iterator<T>.toList(): List<T> = mutableListOf<T>().apply {
	while (this@toList.hasNext()) {
		add(this@toList.next())
	}
}
