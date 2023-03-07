/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM023)
 * @date    25/02/2020 12:00 AM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ChangePasswordFragment : This fragment represents the change password UI element.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

package ymsli.com.ea1h.views.home.changepassword

import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_change_password.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseFragment
import ymsli.com.ea1h.di.component.FragmentComponent

class ChangePasswordFragment : BaseFragment<ChangePasswordViewModel>() {

    override fun provideLayoutId() = R.layout.fragment_change_password

    override fun injectDependencies(fragmentComponent: FragmentComponent) =
        fragmentComponent.inject(this)

    override fun setupView(view: View) {
        /* Password is auto filled in input field and it is uneditable */
        et_email.setText(viewModel.getUserEmail())
        viewModel.emailField.postValue(viewModel.getUserEmail().trim())

        btn_reset_password.setOnClickListener {
            viewModel.resetPassword()
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.emailFieldError.observe(this, Observer {
            et_email.error = this.resources.getString(it)
        })

        viewModel.dialogPositiveButtonAction.observe(this, Observer {
            //alert OK click handler if any requirement comes up in future
        })

        viewModel.showProgress.observe(this, Observer {
            if(it) { showProgressChangePassword(true) }
            else   { showProgressChangePassword(false) }
        })
    }

    /** Show/hide the progress bar depending on the value passed */
    private fun showProgressChangePassword(showProgress: Boolean){
        if(showProgress){
            pb_change_password.visibility = View.VISIBLE
        }
        else{
            pb_change_password.visibility = View.GONE
        }
    }
}