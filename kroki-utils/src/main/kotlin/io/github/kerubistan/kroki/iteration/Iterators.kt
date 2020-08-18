package io.github.kerubistan.kroki.iteration

class MappingIterator<S, T>(private val internal : Iterator<S>, private val mapper : (S) -> T) : Iterator<T> {

	override fun hasNext(): Boolean = internal.hasNext()

	override fun next(): T = mapper(internal.next())

}

fun <S, T>  Iterator<S>.map(mapper : (S) -> T) : Iterator<T> = MappingIterator(this, mapper)
