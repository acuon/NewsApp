package com.example.newsapp.presentation.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"

fun String?.humanFriendlyTime(currentFormat: String = SERVER_DATE_FORMAT): String? {
    try {
        if (this.isNullOrEmpty()) throw Exception("Date cannot be null or empty")
        val dateFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val inputDate = dateFormat.parse(this)!!

        val currentTime = Date()

        val timeDifference = currentTime.time - inputDate.time
        val seconds = timeDifference / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days == 0L -> {
                when {
                    hours == 0L -> {
                        if (minutes == 0L) "Just now"
                        else "$minutes minutes ago"
                    }

                    else -> "$hours hours ago"
                }
            }

            days == 1L -> "Yesterday"
            days < 7L -> "$days days ago"
            else -> {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                sdf.format(inputDate)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}
