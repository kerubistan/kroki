package io.github.kerubistan.kroki.collections

class ImmutableListBuilder<T : Any> {
	private var increment : Int = 128
	private var size : Int = 0
	private var items : Array<Any?> = arrayOfNulls(increment)

	fun add(item : T) {
		if (size >= items.size) {
			items = items.copyOf(size + increment)
		}
		items[size] = item
		size ++
	}

	fun build() : List<T> =
		when (size) {
			0 -> emptyList()
			items.size -> {
				ImmutableArrayList(items as Array<T>)
			}
			else -> {
				ImmutableArrayList((items.copyOf(size)) as Array<T>)
			}
		}
}