package com.example.newsapp.presentation.utils

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlin.math.roundToInt

val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()
val Float.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()
val Int.sp: Int get() = (this * Resources.getSystem().displayMetrics.scaledDensity).roundToInt()

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(message: String) {
    requireContext().showToast(message)
}

fun View.gone() = run { visibility = View.GONE }
fun View.show() = run { visibility = View.VISIBLE }
fun View.hide() = run { visibility = View.INVISIBLE }