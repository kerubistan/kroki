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

	fun addAll(vararg items : T) {
		if(size + items.size >= this.items.size) {
			this.items = this.items.copyOf(size + maxOf(items.size, increment))
		}
		items.copyInto(this.items, this.size)
		size += items.size
	}

	fun addAll(newItems : Iterable<T>) {
		when(newItems) {
			is ImmutableArrayList -> {
				if(size + newItems.items.size >= this.items.size) {
					this.items = this.items.copyOf(size + maxOf(newItems.items.size, increment))
				}
				newItems.items.copyInto(this.items, size)
				size += newItems.items.size
			}
			else ->
				// this is a bit slow this way, it should be avoided if possible
				newItems.forEach { add(it) }
		}
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