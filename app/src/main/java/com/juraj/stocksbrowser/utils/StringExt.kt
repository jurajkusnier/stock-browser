package com.juraj.stocksbrowser.utils

import java.time.LocalDate

fun String.toSafeString() = trim().replace(Regex("[^A-Za-z0-9[-.()&] ]"), "")

fun String.toNumericString() = trim().replace(Regex("[^0-9.\\-]"), "")

fun String.toLocalDateOrNull(): LocalDate? {
    return try {
        LocalDate.parse(this)
    } catch (e: Exception) {
        null
    }
}
