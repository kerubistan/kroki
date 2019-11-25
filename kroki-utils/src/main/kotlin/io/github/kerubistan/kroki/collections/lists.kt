package io.github.kerubistan.kroki.collections

/**
 * Concatenate a "list of list of items" into a "list of items".
 */
fun <T> Collection<Collection<T>>.concat(): List<T> {
    val result = ArrayList<T>(this.sumBy { it.size })
    this.forEach {
        if (it.isNotEmpty()) {
            result.addAll(it)
        }
    }
    return result.toList()
}

/**
 * Check if an iterable object is empty.
 */
fun <T> Iterable<T>.isEmpty() =
    if (this is Collection)
        this.isEmpty()
    else
        !this.iterator().hasNext()

/**
 * Merge two lists using properties on both sides.
 * This behaves very much like the join (left, right, inner, outer) in SQL, therefore it should be renamed
 * to join once the function now being called 'join' is called something else.
 * @param leftItems the left-side list of items that should be
 * @param rightValue extracts the join value from the right side (*this* list)
 * @param leftValue extracts the join value from the left side (param leftItems)
 * @param merge merges values from the right and from the left into one value
 * @param miss what to do with values from the left where there is no match on the left side (null means skip)
 * @param missLeft what to do with values from the right side where there is no match on the right side
 *      (null means skip)
 */
inline fun <R : Any, L, reified SUB : R, reified P> List<R>.mergeInstancesWith(
    leftItems: Iterable<L>,
    rightValue: (SUB) -> P,
    leftValue: (L) -> P,
    merge: (SUB, L) -> SUB,
    miss: (SUB) -> R? = { null },
    missLeft: (L) -> R? = { null }
): List<R> {
    // accelerator: if the left list is empty, then we can just map all of the right list
    // and no merge will happen as there are no matches
    if (leftItems.isEmpty()) {
        return this.mapNotNull { item ->
            if (item is SUB) {
                miss(item)
            } else {
                item
            }
        }
    }
    // accelerator: if the right list is empty, then we can just map the left list
    // and again no merge will happen as there are no matches
    if (this.isEmpty()) {
        return leftItems.mapNotNull(missLeft)
    }
    //none of the above, then let's do the expensive calculations and then we will have to merge
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
    map: (T) -> T
): List<X> = this.map { item ->
    if (item is T && selector(item)) {
        map(item)
    } else item
}

/**
 * Calculate percentile from am iterable.
 * @param percentile the percentile - must be less than 100 and more than 0
 * @param expression extract a numeric value from the item
 *
 * @throws IllegalArgumentException if percentile is not between 0 and 100 or if the *this* iterable is empty.
 */
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

/**
 * Replace any number of items in a collection.
 * @param filter select which item to replace
 * @param replacer replace the item
 */
fun <T> Collection<T>.replace(
    filter: (T) -> Boolean,
    replacer: (T) -> T
): List<T> = this.map {
    if (filter(it)) {
        replacer(it)
    } else {
        it
    }
}

/**
 * Creates multiple groups out of a list. Each item can be grouped into more than one group.
 */
inline fun <V : Any, K: Any> Collection<V>.groupsBy(crossinline keys : (V) -> Iterable<K> ) : Map<K, Set<V>> {
    val result = mutableMapOf<K, MutableSet<V>>()
    this.forEach {
        item ->
        keys(item).forEach {
            key ->
            val items = result[key]
            if(items == null) {
                result[key] = mutableSetOf(item)
            } else {
                items.add(item)
            }
        }
    }
    return result
}
