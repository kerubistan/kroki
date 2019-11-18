package io.github.kerubistan.kroki.collections

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ListsKtTest {
    @Test
    fun join() {
        assertEquals(listOf("A", "B", "C", "D"), listOf(listOf("A"), listOf("B", "C", "D")).join())
        assertEquals(listOf("A", "B", "C", "D"), listOf(listOf("A", "B"), listOf("C", "D")).join())
        assertEquals(listOf(), listOf(listOf(), listOf<String>()).join())
    }

    @Test
    fun percentile() {
        assertThrows<IllegalArgumentException> {
            (0..100).toList().shuffled().percentile(-1.0) {it}
        }
        assertThrows<IllegalArgumentException> {
            (0..100).toList().shuffled().percentile(0.0) {it}
        }
        assertThrows<IllegalArgumentException> {
            (0..100).toList().shuffled().percentile(100.0) {it}
        }
        assertThrows<IllegalArgumentException> {
            (0..100).toList().shuffled().percentile(101.0) {it}
        }
        assertThrows<IllegalArgumentException> {
            listOf<Int>().percentile(90.0) {it}
        }

        assertEquals(99, (0..100).toList().shuffled().percentile(99.0) {it})
    }

    @Test
    fun updateInstances() {
        assertEquals(
            listOf<Number>(2, 1.5, BigDecimal("1.7"), 2.toShort()),
            listOf(1, 1.5, BigDecimal("1.7"), 2.toShort()).updateInstances { nr : Int -> nr * 2 }
        )
        assertTrue {
            listOf<Number>().updateInstances { it }.isEmpty()
        }

        assertEquals(
            listOf(1, 2, 3, 4),
            listOf(1, 2, 3, 4).updateInstances(selector = { false }) {
                    nr : Int -> nr / 0 // whatever, it will never get called
            }
        )
    }

    @Test
    fun mergeInstancesWithUsingJoinProperties() {
        data class Hat (val color : String)
        abstract class Creature {
            abstract val weight : Double
        }
        data class Human(
            val name: String,
            override val weight: Double,
            val favoriteColor: String,
            val hat: Hat? = null
        ) : Creature()

        data class Crocodile(val id: String, override val weight: Double) : Creature()
        assertEquals(
            listOf(
                Human(name = "Bob", favoriteColor = "blue", weight = 80.0, hat = Hat(color = "blue")),
                Crocodile(id = "1", weight = 100.0)
            ),
            listOf(Human(name = "Bob", favoriteColor = "blue", weight = 80.0), Crocodile(id = "1", weight = 100.0))
                .mergeInstancesWith(
                    leftItems = listOf(Hat(color = "blue")),
                    rightValue = Human::favoriteColor,
                    leftValue = Hat::color,
                    merge = { human, hat -> human.copy(hat = hat) }
                )
        )

        assertEquals(
            listOf(Human(name = "Bob", favoriteColor = "blue", weight = 80.0), Crocodile(id= "1", weight = 100.0)),
            listOf(Human(name = "Bob", favoriteColor = "blue", weight = 80.0), Crocodile(id= "1", weight = 100.0))
                .mergeInstancesWith(
                    leftItems = listOf(),
                    rightValue = Human::favoriteColor,
                    leftValue = Hat::color,
                    miss = { it },
                    merge = { human, hat -> human.copy(hat = hat) })
        )

        assertEquals(
            listOf(
                Human(name = "Bob", favoriteColor = "blue", weight = 80.0, hat = Hat(color = "blue")),
                Crocodile(id = "1", weight = 100.0)
            ),
            listOf(Human(name = "Bob", favoriteColor = "blue", weight = 80.0), Crocodile(id= "1", weight = 100.0))
                .mergeInstancesWith(
                    leftItems = listOf(Hat(color = "blue")),
                    rightValue = Human::favoriteColor,
                    leftValue = Hat::color,
                    missLeft = { hat -> Human(name = "Bob", favoriteColor = "blue", weight = 80.0, hat = hat) },
                    merge = { human, hat -> human.copy(hat = hat) })
        )

        assertEquals(
            listOf(Crocodile(id= "1", weight = 100.0)),
            listOf(Human(name = "Bob", favoriteColor = "blue", weight = 80.0), Crocodile(id= "1", weight = 100.0))
                .mergeInstancesWith(
                    leftItems = listOf(),
                    rightValue = Human::favoriteColor,
                    leftValue = Hat::color,
                    merge = { human, hat -> human.copy(hat = hat) })
        )

        assertEquals(
            listOf(Human(name = "Bob", favoriteColor = "blue", weight = 80.0), Crocodile(id= "1", weight = 100.0)),
            listOf(Human(name = "Bob", favoriteColor = "blue", weight = 80.0), Crocodile(id= "1", weight = 100.0))
                .mergeInstancesWith(
                    leftItems = listOf(),
                    rightValue = Human::favoriteColor,
                    leftValue = Hat::color,
                    merge = { human, hat -> human.copy(hat = hat) },
                    miss = { it })
        )

        assertEquals(
            listOf(Human(name = "Bob", favoriteColor = "blue", weight = 80.0), Crocodile(id= "1", weight = 100.0)),
            listOf(Human(name = "Bob", favoriteColor = "blue", weight = 80.0), Crocodile(id= "1", weight = 100.0))
                .mergeInstancesWith(
                    leftItems = listOf(Hat(color = "red")),
                    rightValue = Human::favoriteColor,
                    leftValue = Hat::color,
                    merge = { human, hat -> human.copy(hat = hat) },
                    miss = { it })
        )

        assertEquals(
            listOf(Crocodile(id= "1", weight = 100.0)),
            listOf(Human(name = "Bob", favoriteColor = "blue", weight = 80.0), Crocodile(id= "1", weight = 100.0))
                .mergeInstancesWith(
                    leftItems = listOf(Hat(color = "red")),
                    rightValue = Human::favoriteColor,
                    leftValue = Hat::color,
                    merge = { human, hat -> human.copy(hat = hat) },
                    miss = { null }
                )
        )

        assertEquals(
            listOf(
                Human(name = "dead hat", favoriteColor = "red", weight = 0.0, hat = Hat(color = "red")),
                Crocodile(id = "1", weight = 100.0)
            ),
            listOf(Human(name = "Bob", favoriteColor = "blue", weight = 80.0), Crocodile(id = "1", weight = 100.0))
                .mergeInstancesWith(
                    leftItems = listOf(Hat(color = "red")),
                    rightValue = Human::favoriteColor,
                    leftValue = Hat::color,
                    merge = { human, hat -> human.copy(hat = hat) },
                    miss = { null },
                    missLeft = { hat -> Human(name = "dead hat", hat = hat, weight = 0.0, favoriteColor = hat.color) }
                )
        )

    }

    @Test
    fun iterableIsEmpty() {
        assertTrue((emptySet<String>() as Iterable<String>).isEmpty())
        assertTrue((emptyList<String>() as Iterable<String>).isEmpty())
        assertTrue((emptyMap<String, String>().entries as Iterable<Map.Entry<String, String>>).isEmpty())

        assertFalse((listOf("") as Iterable<String>).isEmpty())
        assertFalse((setOf("") as Iterable<String>).isEmpty())

        assertTrue {
            val iterable = mock<Iterable<String>>()
            val iterator = mock<Iterator<String>>()
            whenever(iterable.iterator()).thenReturn(iterator)
            whenever(iterator.hasNext()).thenReturn(false)

            iterable.isEmpty()
        }

        assertFalse {
            val iterable = mock<Iterable<String>>()
            val iterator = mock<Iterator<String>>()
            whenever(iterable.iterator()).thenReturn(iterator)
            whenever(iterator.hasNext()).thenReturn(true)

            iterable.isEmpty()
        }
    }

    @Test
    fun collectionReplace() {
        val original = listOf("A", "b", "c")
        val replacement = original.replace({ it == "A" }, { it.toLowerCase() })

        Assert.assertThat(replacement, CoreMatchers.hasItem("b"))
        Assert.assertThat(replacement, CoreMatchers.hasItem("c"))
        Assert.assertThat(replacement, CoreMatchers.not(CoreMatchers.hasItem("A")))
        Assert.assertThat(replacement, CoreMatchers.hasItem("a"))
    }


}