package com.luuu.seven.util

import java.text.SimpleDateFormat
import java.util.*



fun Long.toDateString(format: String = "yyyy-MM-dd") = SimpleDateFormat(format, Locale.getDefault()).format(Date(this))

fun Int.toDateString(format: String = "yyyy-MM-dd") = SimpleDateFormat(format, Locale.getDefault()).format(Date(this.toLong()))