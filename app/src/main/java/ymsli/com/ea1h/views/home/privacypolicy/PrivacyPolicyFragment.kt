/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM023)
 * @date    13/02/2020 11:56 AM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * PrivacyPolicyFragment : This fragment represents the Privacy Policy UI element in the user home.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

package ymsli.com.ea1h.views.home.privacypolicy

import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_privacy_policy.*
import ymsli.com.ea1h.BuildConfig
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseFragment
import ymsli.com.ea1h.di.component.FragmentComponent

class PrivacyPolicyFragment : BaseFragment<PrivacyPolicyViewModel>() {

    override fun provideLayoutId() = R.layout.fragment_privacy_policy

    override fun injectDependencies(fragmentComponent: FragmentComponent) =
        fragmentComponent.inject(this)

    override fun setupView(view: View) {
        tv_text_contaner.text = getString(R.string.header_privacy_policy)
        tv_app_version.text = "${resources.getString(R.string.app_version_label)} ${BuildConfig.VERSION_NAME}"

        /**
         * Get the privacy policy from Room(Stored by API call),
         * if not available in room then use the one defined in the constants.
         * @author VE00YM023
         */
        viewModel.getContentFromRoom().observe(this, Observer {
            tv_text_container.text = if(!it.isNullOrBlank()){ it }
            else{ viewModel.getContent() }
        })
    }
}