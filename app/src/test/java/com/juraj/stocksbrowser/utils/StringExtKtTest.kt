package com.juraj.stocksbrowser.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

internal class StringExtKtTest {

    @Test
    fun `test toSafeString`() {
        assertEquals("(No Safe)", "(No Safe)@".toSafeString())
    }

    @Test
    fun `test toNumericString`() {
        assertEquals("123.45", "$123.45".toNumericString())
        assertEquals("-0.44738435", "-0.44738435%".toNumericString())
    }

    @Test
    fun `test toLocalDateOrNull`() {
        assertEquals(LocalDate.of(2022, 10, 21), "2022-10-21".toLocalDateOrNull())
        assertEquals(null, "2022-25-21".toLocalDateOrNull())
    }
}