/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   5/2/20 10:11 AM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * ErrorAckFragment : This class shows any error/problem related acknowledgment to
 *                      the user in the form of Dialog Fragment
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.views.userengagement

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import kotlinx.android.synthetic.main.user_acknowledgemen_fragment.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseDialogFragment
import ymsli.com.ea1h.di.component.DialogFragmentComponent
import ymsli.com.ea1h.views.entrance.EntranceViewModel

class ErrorAckFragment: BaseDialogFragment<EntranceViewModel>() {

    companion object{
        const val TAG = "UserAckFragment"
        fun newInstance(): ErrorAckFragment {
            val args = Bundle()
            val fragment =
                ErrorAckFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.user_acknowledgemen_fragment

    override fun setupView(view: View) {

    }

    var errorMessage: String = "Something went wrong!!"

    fun setMessage(message: String){
        errorMessage = message
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.DKGRAY))
        dialog?.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onResume() {
        super.onResume()
        dialog?.error_message_textView?.text = errorMessage
        dialog?.ack_button?.setOnClickListener {
            dialog?.dismiss()
        }
    }
    override fun injectDependencies(dialogFragmentComponent: DialogFragmentComponent) = dialogFragmentComponent.inject(this)
}