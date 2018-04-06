package com.luuu.seven

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

    private lateinit var mSubscriptions: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSubscriptions = CompositeDisposable()
        mSubscriptions.add(Observable.timer(4, TimeUnit.SECONDS).subscribe({
            val toMain = Intent(this, MainActivity::class.java)
            startActivity(toMain)
            finish()
        }))
    }

    override fun onDestroy() {
        super.onDestroy()
        mSubscriptions.clear()
    }
}
