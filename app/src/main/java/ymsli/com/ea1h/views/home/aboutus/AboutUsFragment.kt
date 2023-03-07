/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM023)
 * @date    12/02/2020 3:15 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * AboutUsFragment : This fragment represents the AboutUs UI element in the user home.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

package ymsli.com.ea1h.views.home.aboutus

import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_about_us.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseFragment
import ymsli.com.ea1h.di.component.FragmentComponent
import ymsli.com.ea1h.utils.common.Constants

class AboutUsFragment : BaseFragment<AboutUsViewModel>() {

    override fun provideLayoutId() = R.layout.fragment_about_us
    override fun injectDependencies(fc: FragmentComponent) = fc.inject(this)

    override fun setupView(view: View) {
        if(viewModel.networkAvailable()){
            progress_bar.visibility = View.VISIBLE
            web_view.settings.javaScriptEnabled = true
            web_view.settings.javaScriptCanOpenWindowsAutomatically = true
            web_view.loadUrl(Constants.WEB_URL)

            web_view.webViewClient = object: WebViewClient(){
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progress_bar?.visibility = View.GONE
                }
            }
        }
        else {
            val message = getString(R.string.label_no_internet_dialog)
            showNotificationDialog(message, R.drawable.ic_error){
                findNavController().navigate(R.id.nav_home)
            }
        }
    }
}