/*
 *
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM023)
 * @date    4/2/20 12:15 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * SignInFragment : This class represents the Sign In fragment.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

package ymsli.com.ea1h.views.entrance

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.signin_fragment.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseFragment
import ymsli.com.ea1h.di.component.FragmentComponent
import ymsli.com.ea1h.utils.common.TextWatcherAdapter
import ymsli.com.ea1h.views.ackdialogs.ForgotPasswordDialog
import ymsli.com.ea1h.views.password.forgot.ForgotPasswordActivity

class SignInFragment: BaseFragment<EntranceViewModel>() {

    /** Contains class level constants as well as the fragment instance creation factory.
     *  @author VE00YM023
     */
    companion object {
        const val TAG = "SignInFragment"
        fun newInstance(): SignInFragment {
            val args = Bundle()
            val fragment = SignInFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.signin_fragment
    override fun injectDependencies(fc: FragmentComponent) = fc.inject(this)
    override fun setupView(view: View) {
        btn_sign_in.setOnClickListener {

            viewModel.getFCMTokenAndLogin()
        }

        tv_sign_in_email.addTextChangedListener(object : TextWatcherAdapter(){
            override fun afterTextChanged(s: Editable?) {
                viewModel.email.postValue(s.toString().trim())
            }
        })

        tv_password.addTextChangedListener(object : TextWatcherAdapter(){
            override fun afterTextChanged(s: Editable?) {
                viewModel.password.postValue(s.toString().trim())
            }
        })

        /* redirect user where he can change password */
        tv_forgot_password.setOnClickListener {
            val forgotPasswordDialog = ForgotPasswordDialog()
            val ft = childFragmentManager.beginTransaction().addToBackStack(null)
            forgotPasswordDialog.show(ft,null)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Maintain state of signin parameter
        if(viewModel.signinParameter.value!=null) {
            tv_sign_in_email.setText(viewModel.signinParameter.value)
        }
        tv_sign_in_email.error = null
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.invalidUserEmailPhone.observe(this, Observer {
            if(it != null){
                showNotificationDialog(getString(it), R.drawable.ic_error){}
                viewModel.invalidUserEmailPhone.value = null
            }
        })

        viewModel.emptyPassword.observe(this, Observer {
            if(it){
                showNotificationDialog(getString(R.string.empty_password), R.drawable.ic_error){}
                viewModel.emptyPassword.value = false
            }
        })
    }
}