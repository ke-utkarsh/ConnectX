/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   5/2/20 10:12 AM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * BaseDialogFragment : This abstract class acts as the base class for all the dialog
 *                          fragments. The dialog fragments acknowledge the user
 *                          for any error or successful acknowledgement.
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.base

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import ymsli.com.ea1h.EA1HApplication
import ymsli.com.ea1h.di.component.DaggerDialogFragmentComponent
import ymsli.com.ea1h.di.component.DialogFragmentComponent
import ymsli.com.ea1h.di.module.DialogFragmentModule
import javax.inject.Inject

abstract class BaseDialogFragment<VM : BaseViewModel>: DialogFragment() {

    @Inject
    lateinit var viewModel: VM

    /**
     * used to create object of properties
     * annotated with @Inject
     * The feature is handled by Dagger2 framework
     */
    private fun buildFragmentComponent() =
        DaggerDialogFragmentComponent
            .builder()
            .applicationComponent((requireContext().applicationContext as EA1HApplication).applicationComponent)
            .dialogFragmentModule(DialogFragmentModule(this))
            .build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(provideLayoutId(), container, false)


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (arguments != null && arguments?.getBoolean(ARG_KEY_NOT_ALERT)!!) {
            return super.onCreateDialog(savedInstanceState)
        }
        injectDependencies(buildFragmentComponent())
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(provideLayoutId(),null)
        builder.setView(view)
        setupView(requireView())
        builder.setCancelable(false)
        return builder.create()
    }

    @LayoutRes
    protected abstract fun provideLayoutId(): Int
    protected open fun setupObservers(){
        //nothing observed in base class of dialog fragment
    }
    protected abstract fun injectDependencies(dialogFragmentComponent: DialogFragmentComponent)
    protected abstract fun setupView(view: View)

    private companion object{ private const val ARG_KEY_NOT_ALERT = "notAlertDialog" }
}