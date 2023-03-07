/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   20/3/20 11:14 AM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * BottomFilterFragment : This base class is for the filters in the application
 *                          The filter UI is of the form Bottom Dialog box.
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
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import ymsli.com.ea1h.EA1HApplication
import ymsli.com.ea1h.di.component.BottomFilterComponent
import ymsli.com.ea1h.di.component.DaggerBottomFilterComponent
import ymsli.com.ea1h.di.module.BottomFilterModule
import javax.inject.Inject

abstract class FilterFragment<VM : BaseViewModel> : DialogFragment() {

    lateinit var dialogView: Dialog

    @Inject lateinit var viewModel: VM

    @LayoutRes
    protected abstract fun provideLayoutId(): Int

    @StyleRes
    protected abstract fun provideLayoutStyle(): Int

    protected abstract fun injectDependencies(bottomFilterComponent: BottomFilterComponent)

    protected abstract fun setupView(view: View)

    private fun buildBottomFilterComponent() =
        DaggerBottomFilterComponent
            .builder()
            .applicationComponent((activity?.application as EA1HApplication).applicationComponent)
            .bottomFilterModule(BottomFilterModule(this))
            .build()


    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(buildBottomFilterComponent())
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity,provideLayoutStyle())
        builder.setView(provideLayoutId())
        val alert = builder.create()
        dialogView = alert
        alert.window?.setGravity(Gravity.TOP)
        alert.show()
        return alert
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView(view)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(provideLayoutId(),container,false)
    }

    protected open fun setupObservers() {
        //nothing observed here
    }
}