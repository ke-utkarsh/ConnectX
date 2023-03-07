package ymsli.com.ea1h.views.ackdialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.ble_info_dialog.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseViewModel
import ymsli.com.ea1h.views.home.HomeViewModel

class BLEInfoDialog(private val viewModel: BaseViewModel): DialogFragment() {

    private var btPermissionRefusedListener: BTPermissionRefused?=null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ble_info_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_ok.setOnClickListener {
            btPermissionRefusedListener?.askBTPermissionOnDenial()
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
        if(context is BTPermissionRefused){
            btPermissionRefusedListener = (context as BTPermissionRefused)
        }
        else{
            throw RuntimeException(context.toString()+ " must implement BTPermissionRefused interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        btPermissionRefusedListener = null
    }

    interface BTPermissionRefused{
        fun askBTPermissionOnDenial()
    }
}
