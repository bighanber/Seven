package com.luuu.seven.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*



fun Long.toDateString(format: String = "yyyy-MM-dd"): String = SimpleDateFormat(format, Locale.getDefault()).format(Date(this))

fun Int.toDateString(format: String = "yyyy-MM-dd"): String = SimpleDateFormat(format, Locale.getDefault()).format(Date(this.toLong()))

fun Int.toDate(): Date = Date(this.toLong() * 1000L)

val Int.asDate: Date
    get() = Date(this.toLong() * 1000L)

/***
 * val format = "yyyy-MM-dd HH:mm:ss"
 * val date = Date()
 * val str = date.toString(format)
 * val date2 = str.toDate(format)
 */
fun String.toDate(format: String = "yyyy-MM-dd"): Date? {
    val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
    return try {
        dateFormatter.parse(this)
    } catch (e: ParseException) {
        null
    }
}

fun Date.toString(format: String = "yyyy-MM-dd"): String {
    val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
    return dateFormatter.format(this)
}