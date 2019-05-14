package com.github.kroki.collections

fun <T> Collection<Collection<T>>.join(): List<T> {
    var result = listOf<T>()
    this.forEach {
        //performance: the += operation is relatively expensive, even with empty lists
        //checking for empty improves throughput from 1.5M ops/sec to 1.8M ops/sec
        if (it.isNotEmpty()) {
            result = result + it
        }
    }
    return result
}
