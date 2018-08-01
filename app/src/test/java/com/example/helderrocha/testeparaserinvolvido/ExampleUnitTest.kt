package com.example.helderrocha.testeparaserinvolvido

import io.kotlintest.specs.FreeSpec
import org.junit.Assert.assertEquals

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest : FreeSpec({

    fun asas(head: Int, tail: List<Int>): List<Int> {
        if (tail.isEmpty()) return listOf(head)
        if (head < tail.first()) return listOf(head) + asas(tail.first(), tail.drop(1))
        else return asas(tail.first(), asas(head, tail.drop(1)))
    }

    fun bubble(list: List<Int>): List<Int> {
        if (list.isEmpty()) return list
        else return asas(list.first(), list.drop(1))
    }

    "bubbleTest" - {
        "bubbleTest1" {
            val asas = listOf(4, 2, 3, 1)
            assertEquals(listOf(1, 2, 3, 4), bubble(asas))
        }

        "bubbleTest2" {
            val asas = listOf(1, 2, 3, 4)
            assertEquals(listOf(1, 2, 3, 4), bubble(asas))
        }

        "bubbleTest3" {
            val asas = listOf(4, 3, 2, 1)
            assertEquals(listOf(1, 2, 3, 4), bubble(asas))
        }
    }
})