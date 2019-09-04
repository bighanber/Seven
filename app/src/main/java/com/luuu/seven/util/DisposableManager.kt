package com.luuu.seven.util

object DisposableManager {
    private val mObservers = HashMap<String, DestroyObserver?>()

    fun getLifecycleObserver(key: String): DestroyObserver? {
        return mObservers[key]
    }

    fun addLifecycleObserver(destroyObserver: DestroyObserver) {

        mObservers[destroyObserver.getObserverKey()] = destroyObserver
        destroyObserver.toRemove {
            mObservers.remove(it.getObserverKey())
        }
    }
}