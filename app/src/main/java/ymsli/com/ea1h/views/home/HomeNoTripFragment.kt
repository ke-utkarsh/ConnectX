package ymsli.com.ea1h.views.home

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.home_fragment_no_trip.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseFragment
import ymsli.com.ea1h.di.component.FragmentComponent

class HomeNoTripFragment : BaseFragment<HomeViewModel>() {

    /** Fragment object creation factory. */
    companion object {
        const val TAG = "HomeNoTripFragment"
        fun newInstance(): HomeNoTripFragment {
            val args = Bundle()
            val fragment =
                HomeNoTripFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.home_fragment_no_trip

    override fun injectDependencies(fragmentComponent: FragmentComponent) = fragmentComponent.inject(this)

    override fun setupView(view: View) {
        tv_welcome.text = "${requireContext().resources.getString(R.string.welcome_string)} ${viewModel.getUserProfileDetail().fullName?:""}"
    }
}