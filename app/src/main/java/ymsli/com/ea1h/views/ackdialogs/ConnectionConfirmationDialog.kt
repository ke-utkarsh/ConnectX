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
import ymsli.com.ea1h.utils.common.Utils
import java.sql.Timestamp

class ConnectionConfirmationDialog(private val chassisNumber: String): DialogFragment() {

    private var dialogListener: ConnectionProceedListerner? =null

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
        btn_cancel.setOnClickListener {
            this.dismiss()
        }

        btn_ok.setOnClickListener {
            dialogListener?.proceedConnection(chassisNumber)
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
            STYLE_NORMAL,
            android.R.style.Theme_Black_NoTitleBar_Fullscreen
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is ConnectionProceedListerner){
            dialogListener = (context as ConnectionProceedListerner)
        }
        else{
            throw RuntimeException(context.toString()+ " must implement ConnectionProceedListerner interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        dialogListener = null
    }

    interface ConnectionProceedListerner{
        fun proceedConnection(chassisNumber: String)
    }
}