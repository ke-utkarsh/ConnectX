package ymsli.com.ea1h.utils.rx

/**
 * Project Name : EA1H
 * @company YMSLI
 * @author VE00YM129
 * @date   August 20, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * RxSchedulerProvider : This provides threads for execution of a particular piece of code
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Singleton
class RxSchedulerProvider: SchedulerProvider {
    override fun computation(): Scheduler = Schedulers.computation()

    override fun io(): Scheduler  = Schedulers.io()

    override fun ui(): Scheduler  = AndroidSchedulers.mainThread()
}