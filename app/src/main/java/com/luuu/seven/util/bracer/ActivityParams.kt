package com.luuu.seven.util.bracer

import android.app.Activity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

//@UseExperimental(ExperimentalStdlibApi::class)
//class ActivityParamsDelegate<T>(
//    private val customKey: String = "",
//    private val defaultValue: T? = null
//) : ReadOnlyProperty<Activity, T> {
//
//    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
//        return Optional(thisRef.intent).get(customKey, property, defaultValue)
//    }
//
//}

//@UseExperimental(ExperimentalStdlibApi::class)
//class ActivityMutableParamsDelegate<T>(
//    private val customKey: String = "",
//    private val defaultValue: T? = null
//) : ReadWriteProperty<Activity, T> {
//
//    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
//        return Optional(thisRef.intent).get(customKey, property, defaultValue)
//    }
//
//    override fun setValue(thisRef: Activity, property: KProperty<*>, value: T) {
//        var intent = intentsMap[thisRef]
//        if (intent == null) {
//            intent = Intent()
//            intentsMap[thisRef] = intent
//        }
//
//        Optional(intent).put(customKey, property, value)
//    }
//}