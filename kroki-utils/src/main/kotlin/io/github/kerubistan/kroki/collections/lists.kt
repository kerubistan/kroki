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

/**
 * Merge two lists using properties on both sides.
 * This behaves very much like the join (left, right, inner, outer) in SQL, therefore it should be renamed
 * to join once the function now being called 'join' is called something else.
 * @param leftItems the left-side list of items that should be
 */
inline fun <R : Any, L, reified SUB : R, reified P> List<R>.mergeInstancesWith(
    leftItems: Iterable<L>,
    rightValue: (SUB) -> P,
    leftValue: (L) -> P,
    merge: (SUB, L) -> SUB,
    miss: (SUB) -> R? = { _ -> null },
    missLeft: (L) -> R? = { _ -> null }
): List<R> {
    val leftValuesByProp = leftItems.associateBy(leftValue)
    val rightValuesByProp = this.filterIsInstance<SUB>().associateBy(rightValue)

    return leftValuesByProp.filterKeys { it !in rightValuesByProp.keys }.mapNotNull { (_, v) -> missLeft(v) } +
            this.mapNotNull { item ->
                if (item is SUB) {
                    val rightCounterpart = leftValuesByProp[rightValue(item)]
                    if (rightCounterpart != null) {
                        merge(item, rightCounterpart)
                    } else {
                        miss(item)
                    }
                } else {
                    item
                }
            }
}

/**
 * Update only instances of a certain class in a list, leave the rest unchanged.
 */
inline fun <X, reified T : X> List<X>.updateInstances(
    selector: (T) -> Boolean = { true },
    map : (T) -> T ) : List<X> = this.map { item ->
    if(item is T && selector(item)) {
        map(item)
    } else item
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