/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   31/1/20 11:13 AM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * SignupFragment : This Activity is initializes the application
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.views.entrance

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.signup_fragment.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseFragment
import ymsli.com.ea1h.di.component.FragmentComponent
import ymsli.com.ea1h.utils.common.TextWatcherAdapter
import ymsli.com.ea1h.utils.common.Validation

class SignUpFragment : BaseFragment<EntranceViewModel>() {

    /** Contains the fragment instance creation factory.
     *  @author VE00YM023
     */
    companion object {
        const val TAG = "SignUpFragment"
        fun newInstance(): SignUpFragment {
            val args = Bundle()
            val fragment = SignUpFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.signup_fragment

    override fun injectDependencies(fragmentComponent: FragmentComponent) =
        fragmentComponent.inject(this)

    /**
     * Setup text change listeners for different text inputs.
     * as soon as text changes we post it to the viewModel
     */
    override fun setupView(view: View) {
        et_name.addTextChangedListener(object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.fullName.postValue(s.toString().trim())
            }
        })

        et_email.addTextChangedListener(object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.email.postValue(s.toString().trim())
            }
        })

        et_password.addTextChangedListener(object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.password.postValue(s.toString().trim())
            }
        })

        et_phone.addTextChangedListener(object : TextWatcherAdapter() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.phone.postValue(s.toString().trim())
            }
        })

        btn_continue.setOnClickListener {
            hideKeyboard(main_layout)
            viewModel.signUp()
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        /**
         * Observe the validation error's on any field,
         * and sets the error message on corresponding field.
         *
         * @author VE00YM023
         */
        viewModel.fieldError.observe(this, Observer {
            val errorMessage = resources.getString(it.resource.data!!)
            when(it.field){
                Validation.Field.NAME         -> {
                    et_name.error = errorMessage
                    et_name.requestFocus()
                }
                Validation.Field.EMAIL        -> {
                    et_email.error = errorMessage
                    et_email.requestFocus()
                }
                Validation.Field.PASSWORD     -> {
                    et_password.error = errorMessage
                    et_password.requestFocus()
                }

                Validation.Field.PHONE_NUMBER -> {
                    et_phone.error = errorMessage
                    et_phone.requestFocus()
                }
            }
        })
    }

    /**
     * using it for state management
     * among fragments and activities
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        et_name.error = null
        et_email.error = null
        et_password.error = null

        if (viewModel.fullName.value != null) et_name.setText(viewModel.fullName.value) //maintain state of the edit text
        if (viewModel.email.value != null) et_email.setText(viewModel.email.value) //maintain state of the edit text
        if (viewModel.password.value != null) et_password.setText(viewModel.password.value) //maintain state of the edit text
    }

    override fun onResume() {
        super.onResume()
        et_name.error = null
        et_email.error = null
        et_password.error = null
    }
}