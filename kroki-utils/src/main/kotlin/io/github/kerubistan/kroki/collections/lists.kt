package io.github.kerubistan.kroki.collections

fun <T> Collection<Collection<T>>.join(): List<T> {
    val result = ArrayList<T>(this.sumBy { it.size })
    this.forEach {
        if (it.isNotEmpty()) {
            result.addAll(it)
        }
    }
    return result.toList()
}

inline fun <T, E : Comparable<E>> Iterable<T>.percentile(percentile: Double, crossinline expression: (T) -> E): E {
    require(percentile < 100) {
        "Percentile must be less than 100. Actual: $percentile"
    }
    require(percentile > 0) {
        "Percentile must be more than 0. Actual: $percentile"
    }
    val ordered = this.sortedBy(expression)
    require(ordered.isNotEmpty()) {
        "There is nothing to calculate with. "
    }

    return expression(ordered[(ordered.size * (percentile / 100)).toInt()])
}