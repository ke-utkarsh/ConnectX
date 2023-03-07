package ymsli.com.ea1h.views.home.unregisteruser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_about_us.*


import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseFragment
import ymsli.com.ea1h.di.component.FragmentComponent
import ymsli.com.ea1h.utils.common.Constants


class UnregisterUserFragment : BaseFragment<UnregisterUserViewModel>() {

    override fun provideLayoutId() = R.layout.fragment_unregister_user
    override fun injectDependencies(fc: FragmentComponent) = fc.inject(this)

    override fun setupView(view: View) {
        if(viewModel.networkAvailable()){
            progress_bar.visibility = View.VISIBLE
            web_view.settings.javaScriptEnabled = true
            web_view.settings.javaScriptCanOpenWindowsAutomatically = true
            web_view.loadUrl(Constants.UNREGISTERED_USER_URL)

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