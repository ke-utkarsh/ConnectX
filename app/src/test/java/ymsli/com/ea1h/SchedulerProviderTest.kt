package ymsli.com.ea1h

import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler
import ymsli.com.ea1h.utils.rx.SchedulerProvider

class SchedulerProviderTest(private val testScheduler: TestScheduler):
    SchedulerProvider {

    override fun computation(): Scheduler = testScheduler

    override fun io(): Scheduler = testScheduler

    override fun ui(): Scheduler = testScheduler

}