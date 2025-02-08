package com.example.newsapp.data.pref

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KProperty

abstract class Preferences {

    companion object {
        @Volatile
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null

        /**
         * Only pass Application Context here in @param [appContext]
         */
        fun init(appContext: Context) {
            context = appContext.applicationContext
        }
    }

    private val normalPrefName: String
        get() = "${javaClass.simpleName}:${context?.packageName}-SharedPrefs"

    private val prefs: SharedPreferences by lazy {
        context?.getSharedPreferences(normalPrefName, Context.MODE_PRIVATE)
            ?: throw IllegalStateException("SharedPreference initialization failed: Context not initialized. Call Preferences.init(context) first.")
    }

    private val listeners = mutableListOf<SharedPrefsListener>()

    abstract class PrefDelegate<T>(private val prefKey: String?) {
        abstract operator fun getValue(thisRef: Any?, property: KProperty<*>): T
        abstract operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
    }

    interface SharedPrefsListener {
        fun onSharedPrefChanged(property: KProperty<*>)
    }

    private fun <T> createPrefDelegate(
        prefKey: String,
        defaultValue: T,
        getter: (String, T) -> T,
        setter: (String, T) -> Unit
    ): PrefDelegate<T> {
        return object : PrefDelegate<T>(prefKey) {
            override fun getValue(thisRef: Any?, property: KProperty<*>): T {
                return getter(prefKey, defaultValue)
            }

            override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
                setter(prefKey, value)
                onPrefChanged(property)
            }
        }
    }

    protected fun stringPref(prefKey: String, defaultValue: String? = null) =
        createPrefDelegate(prefKey, defaultValue, prefs::getString) { key, value ->
            prefs.edit().putString(key, value).apply()
        }

    protected fun intPref(prefKey: String, defaultValue: Int = 0) =
        createPrefDelegate(prefKey, defaultValue, prefs::getInt) { key, value ->
            prefs.edit().putInt(key, value).apply()
        }

    protected fun floatPref(prefKey: String, defaultValue: Float = 0f) =
        createPrefDelegate(prefKey, defaultValue, prefs::getFloat) { key, value ->
            prefs.edit().putFloat(key, value).apply()
        }

    protected fun booleanPref(prefKey: String, defaultValue: Boolean = false) =
        createPrefDelegate(prefKey, defaultValue, prefs::getBoolean) { key, value ->
            prefs.edit().putBoolean(key, value).apply()
        }

    protected fun longPref(prefKey: String, defaultValue: Long = 0L) =
        createPrefDelegate(prefKey, defaultValue, prefs::getLong) { key, value ->
            prefs.edit().putLong(key, value).apply()
        }

    protected fun clearPrefs() {
        prefs.edit().clear().apply()
    }

    private fun onPrefChanged(property: KProperty<*>) {
        listeners.forEach { it.onSharedPrefChanged(property) }
    }
}
