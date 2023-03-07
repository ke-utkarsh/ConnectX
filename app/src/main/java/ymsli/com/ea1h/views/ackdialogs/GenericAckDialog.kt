package ymsli.com.ea1h.views.ackdialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.user_ack_alert.*
import ymsli.com.ea1h.R
import java.lang.RuntimeException

class GenericAckDialog(private val message :String): DialogFragment()  {

    private var dialogListener:OKClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_ack_alert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_user_ack_message.text = message

        btn_ok.setOnClickListener {
            dialogListener?.proceedWithAction()
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
        if(context is OKClickListener){
            dialogListener = (context as OKClickListener)
        }
        else{
            throw RuntimeException(context.toString()+ " must implement OKClickListener interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        dialogListener = null
    }

    interface OKClickListener{
        fun proceedWithAction()
    }

}