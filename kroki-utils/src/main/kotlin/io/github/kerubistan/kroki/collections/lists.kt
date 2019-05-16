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
