package com.example.newsapp.presentation.utils

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addDecorator(decorator: RecyclerView.ItemDecoration) {
    if (itemDecorationCount == 0) addItemDecoration(decorator)
}

fun createDecorator(top: Int, bottom: Int, left: Int, right: Int): RecyclerView.ItemDecoration {
    return object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)
            val itemCount = parent.adapter?.itemCount ?: 0
            with(outRect) {
                this.top = if (position != 0) top else top * 2
                this.left = left
                this.right = right
                this.bottom = if (position != itemCount - 1) bottom else bottom * 2
            }
        }
    }
}

fun ViewGroup.inflater(): LayoutInflater = LayoutInflater.from(context)