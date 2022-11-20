package com.juraj.stocksbrowser.utils

fun String.toAlphaNumericString() = trim().replace(Regex("[^A-Za-z0-9 ]"), "")