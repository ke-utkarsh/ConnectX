package ymsli.com.ea1h.views.userengagement

/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   23/7/20 10:11 AM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * MFECUProgressFragment : This class is responsilbe for showing progress bar which
 *                      specifically shown while sending commands to MFECU.
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *
 *  * -----------------------------------------------------------------------------------
 *
 */

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.progress_layout_mfecu_commands.*
import kotlinx.android.synthetic.main.progress_layout_mfecu_commands.view.*
import ymsli.com.ea1h.R

class MFECUProgressFragment: DialogFragment() {

    lateinit var dialogView: View

    companion object{
        const val TAG = "MFECUProgressFragment"
        fun newInstance(): MFECUProgressFragment {
            val args = Bundle()
            val fragment =
                MFECUProgressFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (arguments != null)
        {
            if (arguments?.getBoolean("notAlertDialog")!!)
            {
                return super.onCreateDialog(savedInstanceState)
            }
        }
        val builder = AlertDialog.Builder(activity)
        builder.setView(R.layout.progress_layout_mfecu_commands)
        builder.setCancelable(false)
        return builder.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialogView =  inflater.inflate(R.layout.progress_layout_mfecu_commands, container, false)
        return dialogView
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}