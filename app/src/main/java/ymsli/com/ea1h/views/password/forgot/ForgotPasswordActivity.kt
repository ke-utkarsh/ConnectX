/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   8/2/20 10:11 AM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * ForgotPasswordActivity : This Activity helps user to reset the password in case he
 *                              forget the actual password.
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.views.password.forgot

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.forgot_password_activity.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseActivity
import ymsli.com.ea1h.di.component.ActivityComponent
import ymsli.com.ea1h.utils.common.TextWatcherAdapter
import ymsli.com.ea1h.utils.common.Validation
import ymsli.com.ea1h.views.ackdialogs.ForgotPasswordDialog
import ymsli.com.ea1h.views.entrance.EntranceActivity

class ForgotPasswordActivity : BaseActivity<ForgotPasswordViewModel>(){

    companion object {
        const val PHONE_NUMBER_TAG = "PHONE_NUMBER_TAG"
        const val OTP_TAG = "OTP_TAG"
    }

    override fun provideLayoutId(): Int = R.layout.forgot_password_activity

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {
        //Make activity full screen
        window.setFlags(FLAG_FULLSCREEN,FLAG_FULLSCREEN)
        btn_change_password.setOnClickListener {
            viewModel.changePassword()
        }

        et_email.addTextChangedListener(object : TextWatcherAdapter(){
            override fun afterTextChanged(s: Editable?) {
                viewModel.emailField.postValue(s.toString().trim())
            }
        })
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.emailFieldError.observe(this, Observer {
            et_email.error = this.resources.getString(it)
        })

        viewModel.dialogPositiveButtonAction.observe(this, Observer {
            openEntranceActivity()
        })

        viewModel.showProgress.observe(this, Observer {
            if(it){
                showProgress(true)
            }
            else showProgress(false)
        })
    }

    /**
     * kills current activity and open
     * entrance activity
     */
    private fun openEntranceActivity(){
        val entranceActivityIntent = Intent(this,EntranceActivity::class.java)//once user is done, open entrance activity
        entranceActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(entranceActivityIntent)
        finishAndRemoveTask()
    }
}