package com.luuu.seven.theme

import com.luuu.seven.util.MediatorHandling
import com.luuu.seven.util.Result
import com.luuu.seven.util.SharedPreferenceStorage

open class ThemeObserve : MediatorHandling<Unit, Theme>() {
    override fun execute(parameters: Unit) {
        result.addSource(SharedPreferenceStorage.getInstance.observableSelectedTheme) {
            result.postValue(Result.Success(themeFromStorageKey(it)))
        }
    }

}