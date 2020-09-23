package com.luuu.seven.util

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.luuu.seven.ComicApplication
import com.luuu.seven.theme.Theme
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface PreferenceStorage {
    var selectedTheme: String?
    var observableSelectedTheme: LiveData<String>
}

class SharedPreferenceStorage : PreferenceStorage {

    companion object {
        const val PREFS_NAME = "seven_comic"
        val getInstance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = SharedPreferenceStorage()
    }

    private val prefs: Lazy<SharedPreferences> = lazy {
        ComicApplication.mApp.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).apply {
            registerOnSharedPreferenceChangeListener(changeListener)
        }
    }

    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == "pref_dark_mode") {
            observableSelectedThemeResult.value = selectedTheme
        }

    }

    private val observableSelectedThemeResult = MutableLiveData<String>()

    override var selectedTheme by StringPreference(
        prefs, "pref_dark_mode", Theme.SYSTEM.storageKey
    )

    override var observableSelectedTheme: LiveData<String>
        get() {
            observableSelectedThemeResult.value = selectedTheme
            return observableSelectedThemeResult
        }
        set(_) = throw IllegalAccessException("This property can't be changed")

}

class StringPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: String?
) : ReadWriteProperty<Any, String?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String? {
        return preferences.value.getString(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        preferences.value.edit { putString(name, value) }
    }
}