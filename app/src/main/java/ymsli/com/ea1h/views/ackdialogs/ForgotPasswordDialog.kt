package ymsli.com.ea1h.views.ackdialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.forgot_password_dialog.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.utils.common.Status
import ymsli.com.ea1h.utils.common.Validator
import java.lang.RuntimeException

class ForgotPasswordDialog : DialogFragment() {

    private var dialogListener: ForgotPasswordDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.forgot_password_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_cancel.setOnClickListener {
            this.dismiss()
        }

        btn_ok.setOnClickListener {
            val email = tv_sign_in_email.text.toString().trim()
            validateEmail(email)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is ForgotPasswordDialogListener){
            dialogListener = (context as ForgotPasswordDialogListener)
        }
        else{
            throw RuntimeException(context.toString()+ " must implement DialogListener interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        dialogListener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("API123", "onCreate")
        var setFullScreen = false
        if (arguments != null) {
            setFullScreen = requireArguments().getBoolean("fullScreen")
        }
        if (setFullScreen) setStyle(
            STYLE_NORMAL,
            android.R.style.Theme_Black_NoTitleBar_Fullscreen
        )
    }

    /**
     * validates input email
     */
    private fun validateEmail(email: String){
        val validation = Validator.validateEmail(email)
        val failedValidation = validation.filter { it.resource.status == Status.ERROR }
        if (failedValidation.isNotEmpty() && failedValidation[0].resource.data!=null) {
            tv_sign_in_email.error = this.resources.getString(failedValidation[0].resource.data!!)
        }
        else{
            dialogListener?.onFinishEditDialog(email)
            this.dismiss()
        }
    }

    interface ForgotPasswordDialogListener {
        fun onFinishEditDialog(inputText: String?)
    }
}