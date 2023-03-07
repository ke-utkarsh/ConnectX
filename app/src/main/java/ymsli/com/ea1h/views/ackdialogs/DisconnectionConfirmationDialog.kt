package ymsli.com.ea1h.views.ackdialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.connect_confirm_dialog.*
import ymsli.com.ea1h.R
import java.lang.RuntimeException

class DisconnectionConfirmationDialog: DialogFragment() {

    private var dialogListener: DisconnectionListener? =null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.connect_confirm_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_enter_email_label.text = requireContext().getString(R.string.disconnection_dialog_message)
        btn_cancel.setOnClickListener {
            this.dismiss()
        }

        btn_ok.setOnClickListener {
            dialogListener?.proceedDisconnection()
            this.dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var setFullScreen = false
        if (arguments != null) {
            setFullScreen = requireArguments().getBoolean("fullScreen")
        }
        if (setFullScreen) setStyle(
            DialogFragment.STYLE_NORMAL,
            android.R.style.Theme_Black_NoTitleBar_Fullscreen
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is ConnectionConfirmationDialog.ConnectionProceedListerner){
            dialogListener = (context as DisconnectionListener)
        }
        else{
            throw RuntimeException(context.toString()+ " must implement DisconnectionListener interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        dialogListener = null
    }

    interface DisconnectionListener{
        fun proceedDisconnection()
    }
}