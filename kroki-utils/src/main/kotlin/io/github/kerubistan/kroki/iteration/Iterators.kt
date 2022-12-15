package io.github.kerubistan.kroki.iteration

/**
 * @suppress
 */
internal class MappingIterator<S, T>(private val internal: Iterator<S>, private val mapper: (S) -> T) : Iterator<T> {

	override fun hasNext(): Boolean = internal.hasNext()

	override fun next(): T = mapper(internal.next())

}

/**
 * Creates an iterator over a collection that transforms the objects while iterating.
 * @receiver the collection to transform while iterating
 * @param mapper the transformation function literal
 * @return an iterator over the transformed objects
 * @sample io.github.kerubistan.kroki.iteration.IteratorsKtTest.iteratorToMap
 */
fun <S, T> Iterator<S>.map(mapper: (S) -> T): Iterator<T> = MappingIterator(this, mapper)

/**
 * Builds a map out of a iterator using a item to key-value pair transform function literal.
 * @param map transformation from the item type to pair of key and value
 * @sample io.github.kerubistan.kroki.iteration.IteratorsKtTest.iteratorToMap
 */
fun <T, K, V> Iterator<T>.toMap(map: T.() -> Pair<K, V>): Map<K, V> = mutableMapOf<K, V>().let { target ->
	this.forEach {
		val (key, value) = it.map()
		target[key] = value
	}
	target
}

/**
 * Builds a list in case only an iterator is available.
 * @receiver the iterator out of which the list will be built
 * @sample io.github.kerubistan.kroki.iteration.IteratorsKtTest.toList
 */
fun <T> Iterator<T>.toList(): List<T> = mutableListOf<T>().apply {
	while (this@toList.hasNext()) {
		add(this@toList.next())
	}
}
