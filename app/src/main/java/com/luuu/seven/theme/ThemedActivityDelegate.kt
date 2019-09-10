package com.luuu.seven.theme

import androidx.lifecycle.LiveData
import com.luuu.seven.util.Result
import com.luuu.seven.util.SharedPreferenceStorage
import com.luuu.seven.util.map

interface ThemedActivityDelegate {
    val theme: LiveData<Theme>
    val currentTheme: Theme
}

class ThemedActivityDelegateImpl : ThemedActivityDelegate {

    val ob = ThemeObserve()

    init {
        ob.execute(Unit)
    }

    override val theme: LiveData<Theme> by lazy(LazyThreadSafetyMode.NONE) {
        ob.observe().map {
            if (it is Result.Success) it.data else Theme.SYSTEM
        }
    }
    override val currentTheme: Theme
        get() = SharedPreferenceStorage.getInstance.selectedTheme?.let { key ->
            return themeFromStorageKey(key)
        } ?: Theme.SYSTEM

}