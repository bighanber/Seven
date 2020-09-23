package com.luuu.seven.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.vertical() {
    layoutManager = LinearLayoutManager(context)
}

fun RecyclerView.horizontal() {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
}