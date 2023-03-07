/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   5/2/20 11:53 AM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * SuccessAckFragment : This is responsible to show successsful operation performed
 *                          by user if any.
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
import android.view.View
import android.view.ViewGroup
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseDialogFragment
import ymsli.com.ea1h.di.component.DialogFragmentComponent
import ymsli.com.ea1h.views.entrance.EntranceViewModel

class SuccessAckFragment: BaseDialogFragment<EntranceViewModel>() {

    companion object{
        const val TAG = "SuccessAckFragment"
        fun newInstance(): SuccessAckFragment {
            val args = Bundle()
            val fragment =
                SuccessAckFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var successMessage: String = "Something went wrong!!"

    fun setMessage(message: String){
        successMessage = message
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.DKGRAY))
        dialog?.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onResume() {
        super.onResume()
    }
    override fun provideLayoutId(): Int = R.layout.user_success_fragment
    override fun injectDependencies(dialogFragmentComponent: DialogFragmentComponent)
            = dialogFragmentComponent.inject(this)

    override fun setupView(view: View) {}
}