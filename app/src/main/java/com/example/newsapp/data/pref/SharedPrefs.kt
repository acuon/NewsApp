package com.example.newsapp.data.pref

import javax.inject.Inject

class SharedPrefs @Inject constructor() : Preferences() {
    val apiKey by stringPref(PrefKeys.API_KEY.value)
    val baseUrl by stringPref(PrefKeys.BASE_URL.value)
}