package com.luuu.seven.util.scheduler

import io.reactivex.Scheduler
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.plugins.RxJavaPlugins


class SchedulerSuppress {

    companion object {
        fun SuppressIo() {
            val old = RxJavaPlugins.getIoSchedulerHandler()
            RxJavaPlugins.setIoSchedulerHandler(IoSuppression(old))
        }

        fun SuppressCompute() {
            val old = RxJavaPlugins.getComputationSchedulerHandler()
            RxJavaPlugins.setComputationSchedulerHandler(ComputeSuppression(old))
        }

        fun SuppressBackground() {
            val oldCompute = RxJavaPlugins.getComputationSchedulerHandler()
            RxJavaPlugins.setComputationSchedulerHandler(BackgroundThreadSuppression(oldCompute))
            val oldIo = RxJavaPlugins.getIoSchedulerHandler()
            RxJavaPlugins.setIoSchedulerHandler(BackgroundThreadSuppression(oldIo))
        }
    }

    class BackgroundThreadSuppression : AbstractSuppression {
        constructor() : super()
        constructor(previousTransformer: Function<in Scheduler, out Scheduler>?) : super(
            previousTransformer
        )

        override fun shouldJustRunInCurrentThread(task: Thread): Boolean {
            val threadName = task.name
            return threadName != null && (threadName.startsWith("RxCached") || threadName.startsWith(
                "RxComputation"
            ))
        }
    }

    class ComputeSuppression : AbstractSuppression {
        constructor() : super()
        constructor(previousTransformer: Function<in Scheduler, out Scheduler>?) : super(
            previousTransformer
        )

        override fun shouldJustRunInCurrentThread(task: Thread): Boolean {
            val threadName = task.name
            return threadName != null && threadName.startsWith("RxComputation")
        }
    }

    class IoSuppression : AbstractSuppression {
        constructor() : super()
        constructor(previousTransformer: Function<in Scheduler, out Scheduler>?) : super(
            previousTransformer
        )

        override fun shouldJustRunInCurrentThread(task: Thread): Boolean {
            val threadName = task.name
            return threadName != null && threadName.startsWith("RxCached")
        }
    }

    abstract class AbstractSuppression : Function<Scheduler, Scheduler>, Predicate<Thread> {

        private var previousTransformer: Function<in Scheduler, out Scheduler>? = null

        constructor() : this(null)

        constructor(previousTransformer: Function<in Scheduler, out Scheduler>?) {
            this.previousTransformer = previousTransformer
        }

        override fun test(t: Thread): Boolean {
            return shouldJustRunInCurrentThread(t)
        }

        override fun apply(t: Scheduler): Scheduler {
            return if (previousTransformer != null) ImmediateScheduler(
                previousTransformer!!.apply(t), this
            ) else ImmediateScheduler(t, this)
        }

        abstract fun shouldJustRunInCurrentThread(task: Thread): Boolean
    }
}