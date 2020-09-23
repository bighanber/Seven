package com.luuu.seven.util.scheduler

import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Predicate
import io.reactivex.internal.schedulers.TrampolineScheduler
import io.reactivex.plugins.RxJavaPlugins
import java.util.concurrent.TimeUnit


class ImmediateScheduler(schedulerImpl: Scheduler, shouldJustRunInCurrentThread: Predicate<Thread>) : Scheduler() {

    private val actual: Scheduler = schedulerImpl
    private val runInCurrentThread: Predicate<Thread> = shouldJustRunInCurrentThread

    override fun createWorker(): Worker {
        return ImmediateWorker(actual, runInCurrentThread)
    }

    override fun scheduleDirect(run: Runnable): Disposable {
        return if (predicate(runInCurrentThread)) {
            TrampolineScheduler.instance().scheduleDirect(run)
        } else {
            actual.scheduleDirect(run)
        }
    }

    override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
        return if (predicate(runInCurrentThread)) {
            TrampolineScheduler.instance().scheduleDirect(run, delay, unit)
        } else {
            actual.scheduleDirect(run, delay, unit)
        }
    }

    override fun schedulePeriodicallyDirect(
        run: Runnable,
        initialDelay: Long,
        period: Long,
        unit: TimeUnit
    ): Disposable {
        return if (predicate(runInCurrentThread)) {
            TrampolineScheduler.instance().schedulePeriodicallyDirect(
                run,
                initialDelay,
                period,
                unit
            )
        } else {
            actual.schedulePeriodicallyDirect(run, initialDelay, period, unit)
        }
    }

    class ImmediateWorker(actual: Scheduler, shouldJustRunInCurrentThread: Predicate<Thread>) : Scheduler.Worker() {
        private val shouldJustRunInCurrentThread: Predicate<Thread> = shouldJustRunInCurrentThread
        private val currentThreadWorker = TrampolineScheduler.instance().createWorker()
        private val actualWorker: Worker = actual.createWorker()

        override fun schedule(run: Runnable): Disposable {
            return if (predicate(shouldJustRunInCurrentThread)) currentThreadWorker.schedule(run) else actualWorker.schedule(run)
        }

        override fun dispose() {
            currentThreadWorker.dispose()
            actualWorker.dispose()
        }

        override fun isDisposed(): Boolean {
            return actualWorker.isDisposed
        }

        override fun now(unit: TimeUnit): Long {
            return actualWorker.now(unit)
        }

        override fun schedule(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
            return if (predicate(shouldJustRunInCurrentThread)) currentThreadWorker.schedule(run, delay, unit) else actualWorker.schedule(run, delay, unit)
        }

        override fun schedulePeriodically(
            run: Runnable,
            initialDelay: Long,
            period: Long,
            unit: TimeUnit
        ): Disposable {
            return if (predicate(shouldJustRunInCurrentThread))
                currentThreadWorker.schedulePeriodically(run, initialDelay, period, unit)
            else
                actualWorker.schedulePeriodically(run, initialDelay, period, unit)
        }
    }

    companion object {
        fun predicate(predicate: Predicate<Thread>): Boolean {
            var inCurrent = false
            try {
                inCurrent = predicate.test(Thread.currentThread())
            } catch (e: Exception) {
                RxJavaPlugins.onError(e)
            }
            return inCurrent
        }
    }
}