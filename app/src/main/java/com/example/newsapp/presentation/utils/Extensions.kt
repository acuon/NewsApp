package com.example.newsapp.presentation.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.newsapp.BuildConfig
import com.example.newsapp.R
import com.example.newsapp.presentation.commons.DataSource
import com.example.newsapp.presentation.commons.Resource
import com.google.android.material.snackbar.Snackbar
import kotlin.math.roundToInt

val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()
val Float.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()
val Int.sp: Int get() = (this * Resources.getSystem().displayMetrics.scaledDensity).roundToInt()

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    requireContext().showToast(message, duration)
}

fun Fragment.showSnackBar(
    message: String,
    duration: Int = Snackbar.LENGTH_LONG,
    actionText: String? = null,
    action: (() -> Unit)? = null,
    persistent: Boolean = false
) {
    val snackBar = Snackbar.make(
        requireView(),
        message,
        if (persistent) Snackbar.LENGTH_INDEFINITE else duration
    )
    if (actionText != null && action != null) {
        snackBar.setAction(actionText) { action.invoke() }
    }
    snackBar.show()
}

fun View.gone() = run { visibility = View.GONE }
fun View.show() = run { visibility = View.VISIBLE }
fun View.hide() = run { visibility = View.INVISIBLE }

fun Fragment.isNetworkAvailable(): Boolean {
    val connectivityManager =
        requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
}

fun <T> Fragment.handleResourceState(
    state: Resource<T>,
    onLoading: (() -> Unit)? = null,
    onError: ((String, Resource.Error<T>) -> Unit)? = null,
    onSuccess: ((Resource.Success<T>) -> Unit)? = null,
    showCachedDataMessage: Boolean = true,
    retryAction: (() -> Unit)? = null
) {
    when (state) {
        is Resource.Loading -> {
            onLoading?.invoke()
        }

        is Resource.Error -> {
            val errorMessage = when {
                !isNetworkAvailable() && state.data == null ->
                    "No internet connection. Please check your connection and try again."

                !isNetworkAvailable() && state.data != null ->
                    "You're offline. Showing last saved data."

                state.code in 0..10 -> state.message
                state.code == 429 -> "Too many requests. Please try again later."
                state.code in 500..599 -> "Server error. We're working on it."
                state.code == 401 -> "Session expired. Please login again."
                state.code == 403 -> "Access denied. Please check your permissions."
                BuildConfig.DEBUG -> "Error: ${state.message}"
                else -> "Something went wrong. Please try again."
            }
            if (!isNetworkAvailable()) {
                showSnackBar(
                    message = "You're offline. Try again",
                    actionText = "Retry",
                    action = { retryAction?.invoke() },
                    persistent = true
                )
            } else {
                showSnackBar(errorMessage)
            }
            onError?.invoke(errorMessage, state)
        }

        is Resource.Success -> {
            onSuccess?.invoke(state)
            if (showCachedDataMessage || state.source == DataSource.CACHE) {
                if (!isNetworkAvailable()) {
                    showSnackBar(
                        message = "You're offline. Showing saved content.",
                        actionText = "Retry",
                        action = { retryAction?.invoke() },
                        persistent = true
                    )
                }
            }
        }
    }
}