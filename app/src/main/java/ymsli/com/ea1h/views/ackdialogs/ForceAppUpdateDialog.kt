package ymsli.com.ea1h.views.ackdialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_force_update.*
import ymsli.com.ea1h.R
import java.lang.RuntimeException

class ForceAppUpdateDialog(private val message: String): DialogFragment()  {

    private var dialogListener:ForceUpdateListener?  = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_force_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_label_change_password_hint.text = message
        btn_ok.setOnClickListener {
            dialogListener?.performForceUpdate()
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
        if(context is ForceUpdateListener){
            dialogListener = (context as ForceUpdateListener)
        }
        else{
            throw RuntimeException(context.toString()+ " must implement ForceUpdateListener interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        dialogListener = null
    }

    interface ForceUpdateListener{
        fun performForceUpdate()
    }
}
