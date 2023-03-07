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
 * SchedulerProvider : This is the interface for thread provider
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

import io.reactivex.Scheduler
import javax.inject.Singleton

@Singleton
interface SchedulerProvider {

    fun computation(): Scheduler

    fun io(): Scheduler

    fun ui(): Scheduler
}