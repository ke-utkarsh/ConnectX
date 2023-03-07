/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM023)
 * @date    13/02/2020 10:30 AM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * AboutUsViewModel : View model for the AboutUsFragment in the user home section.
 * Since AboutUsFragment shows a large text with associated header, this view model
 * implements the TextContentProvider interface.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

package ymsli.com.ea1h.views.home.aboutus

import io.reactivex.disposables.CompositeDisposable
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.base.BaseViewModel
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.rx.SchedulerProvider

class AboutUsViewModel(schedulerProvider: SchedulerProvider,
                      compositeDisposable: CompositeDisposable,
                      networkHelper: NetworkHelper,
                      eA1HRepository: EA1HRepository
)
    : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, eA1HRepository){

    override fun onCreate() {}

    fun networkAvailable() = checkInternetConnection()
}