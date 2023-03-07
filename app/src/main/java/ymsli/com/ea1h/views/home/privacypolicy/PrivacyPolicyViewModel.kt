/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM023)
 * @date    13/02/2020 11:56 AM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * PrivacyPolicyViewModel : View model for the PrivacyPolicyFragment in the user home section.
 * Since PrivacyPolicyViewFragment shows a large text with associated header, this view model
 * implements the TextContentProvider interface.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

package ymsli.com.ea1h.views.home.privacypolicy

import androidx.lifecycle.LiveData
import io.reactivex.disposables.CompositeDisposable
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.base.BaseViewModel
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.common.Constants
import ymsli.com.ea1h.utils.rx.SchedulerProvider

class PrivacyPolicyViewModel(schedulerProvider: SchedulerProvider,
                             compositeDisposable: CompositeDisposable,
                             networkHelper: NetworkHelper,
                             eA1HRepository: EA1HRepository)
    : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, eA1HRepository){

    override fun onCreate() {}
    companion object{ const val PRIVACY_POLICY_DESCRIPTION_ID = 2 }

    fun getContentFromRoom(): LiveData<String> = eA1HRepository.getContent(PRIVACY_POLICY_DESCRIPTION_ID)

    /**
     * Provide the main content for the text UI, only call this if the
     * content provided by API is not available.
     * @author VE00YM023
     */
    fun getContent() = Constants.CONTENT_PRIVACY_POLICY
}