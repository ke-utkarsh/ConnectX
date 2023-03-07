package ymsli.com.ea1h.utils.common

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM129)
 * @date    18/07/2020 11:45 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * Event : This class live data from being fired multiple times when a fragment is
 * loaded over same activity.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import java.util.concurrent.atomic.AtomicBoolean
/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
data class Event<out T>(private val content: T) {

    private var hasBeenHandled = AtomicBoolean(false)


    /**
     * Returns the content and prevents its use again.
     */
    fun getIfNotHandled(): T? = if (hasBeenHandled.getAndSet(true)) null else content

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peek(): T = content
}