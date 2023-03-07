package ymsli.com.ea1h.base

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import ymsli.com.ea1h.EA1HApplication
import ymsli.com.ea1h.R
import ymsli.com.ea1h.di.component.DaggerFragmentComponent
import ymsli.com.ea1h.di.component.FragmentComponent
import ymsli.com.ea1h.di.module.FragmentModule
import ymsli.com.ea1h.utils.common.Constants.ACTION_CANCEL
import ymsli.com.ea1h.utils.common.Constants.ACTION_OK
import ymsli.com.ea1h.utils.common.Event
import ymsli.com.ea1h.views.userengagement.ProgressFragment
import javax.inject.Inject

/**
 * Project Name : EA1H
 * @company YMSLI
 * @author VE00YM129
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * BaseFragment : This abstract class is the base fragment of all the fragments
 *                  and contains common code of all the fragments
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    @Inject lateinit var viewModel: VM

    @Inject
    lateinit var progressFragment: ProgressFragment

    @LayoutRes
    protected abstract fun provideLayoutId(): Int
    protected abstract fun injectDependencies(fragmentComponent: FragmentComponent)
    protected abstract fun setupView(view: View)

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies(buildFragmentComponent())
        super.onCreate(savedInstanceState)
        viewModel.onCreate()
    }


    /**
     * used to create object of properties
     * annotated with @Inject
     * The feature is handled by Dagger2 framework
     */
    private fun buildFragmentComponent() =
        DaggerFragmentComponent
            .builder()
            .applicationComponent((requireActivity().applicationContext as EA1HApplication).applicationComponent)
            .fragmentModule(FragmentModule(this))
            .build()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = getPersistentView(inflater, container, savedInstanceState, provideLayoutId())

    /**
     * contains all LiveData observers.
     * these parameters are present in
     * BaseViewModel class
     */
    protected open fun setupObservers() {
        viewModel.messageString?.observe(viewLifecycleOwner, Observer {
            it?.data?.run { showMessage(this) }
        })

        viewModel.messageStringId?.observe(viewLifecycleOwner, Observer {
            it?.data?.run { showMessage(this) }
        })

        viewModel.userAlertDialog.observe(viewLifecycleOwner, Observer {
            it.getIfNotHandled()?.let {
                showAckDialog(resources.getString(R.string.alert_label),it)
            }
        })
    }


    /** shows successful ack dialogs to user, using an alert dialog */
    private fun showAckDialog(title: String,messageId: Int){
        val message = resources.getString(messageId)
        (AlertDialog.Builder(context))
            .setTitle(title)
            .setMessage(message).setPositiveButton(getString(R.string.ACTION_OK)) { dialog, _ ->
                dialog?.dismiss()
                viewModel.dialogPositiveButtonAction.postValue(Event(true))
            }.show()
    }

    /** used for hiding keyboard upon button click */
    fun hideKeyboard(viewId: ViewGroup){
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow((viewId as View).windowToken, 0)
    }

    /** used to ack user of any validation, server response, internet connection, etc */
    fun showMessage(message: String) = context?.let {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!hasInitializedRootView){
            hasInitializedRootView = true
            setupView(view)
            setupObservers()
        }
    }

    protected open fun showBikeIsConnected(message: String){
        showNotificationDialog(message, R.drawable.ic_success){}
    }
    /**
     * acknowldege user about any error/issue
     * using dialog fragment
     */
    protected open fun showErrorsMessageViaSnackbar(message: String) {
        showNotificationDialog(message, R.drawable.ic_error){}
//        Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
//            .setActionTextColor(
//                ContextCompat.getColor(requireActivity(), R.color.white)
//            ).show()
    }

    /** show or hide progress based on input parameter */
    fun showProgress(showProgress: Boolean) {
        if (showProgress) {
            if (!progressFragment.isAdded) {
                val frag = requireActivity().supportFragmentManager.findFragmentByTag(ProgressFragment.TAG)
                if (!(frag != null && frag is ProgressFragment)) {
                    progressFragment.show(requireActivity().supportFragmentManager, ProgressFragment.TAG)
                    progressFragment.isCancelable = false
                }
            }
        } else if (progressFragment.isResumed) progressFragment.dismiss()
    }

    /**
     * Inflates the UI for a confirmation dialog, which contains message text and
     * two action buttons.
     * @param message confirmation message to display
     * @param labelOk label for the 'POSITIVE' action button
     * @return Triple containing dialog view as first, 'POSITIVE BTN' as second
     *         and 'CANCEL BTN' as third
     *
     * @author VE00YM023
     */
    private fun inflateConfirmationDialogView(message: String, labelOk: String)
            : Triple<View, Button, Button>{
        val dialogView = layoutInflater.inflate(R.layout.dialog_confirmation, null, false)
        val tvMessage = dialogView.findViewById(R.id.tv_message) as TextView
        val btnCancel = dialogView.findViewById(R.id.btn_cancel) as Button
        val btnOk = dialogView.findViewById(R.id.btn_ok) as Button
        btnOk.text = labelOk
        tvMessage.text = message
        return Triple(dialogView, btnOk, btnCancel)
    }

    /**
     * Displays a confirmation dialog containing a message and two actions buttons.
     * @param msg confirmation message to be displayed
     * @param labelAction label of 'POSITIVE' Action
     * @param actionCancel
     * @param actionOk
     * @author VE00YM023
     */
    protected fun showConfirmationDialog(msg: String, labelAction: String = getString(R.string.ACTION_OK),
                                         actionCancel: () -> Unit, actionOk: () -> Unit){
        val (dialogView, btnOk, btnCancel) = inflateConfirmationDialogView(msg, labelAction)
        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setView(dialogView)
            .create()
        btnCancel.setOnClickListener { actionCancel(); dialog.dismiss() }
        btnOk.setOnClickListener { dialog.dismiss(); actionOk() }
        dialog.show()
    }

    /**
     * Displays a notification dialog containing a message and an action button.
     * @param message notification message to be displayed
     * @param iconId resource id of icon to be used
     * @param actionOk
     * @author VE00YM023
     */
    protected fun showNotificationDialog(message: String, iconId: Int,
                                         actionOk: () -> Unit){
        val dialogView = layoutInflater.inflate(R.layout.dialog_notification, null, false)
        val tvMessage = dialogView.findViewById(R.id.tv_message) as TextView
        val icon = dialogView.findViewById(R.id.iv_icon) as ImageView
        val btnOk = dialogView.findViewById(R.id.btn_ok) as Button
        tvMessage.text = message
        icon.setImageResource(iconId)
        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setView(dialogView)
            .create()
        btnOk.setOnClickListener {
            dialog.dismiss()
            actionOk()
        }
        dialog.show()
    }

    /**
     * Displays the background settings dialog with 'Don't ask me again' checkbox.
     * value of checkbox is returned in the actionOk event handler.
     * @param action higher order function to be executed on click of action buttons
     * @author VE00YM023
     */
    protected fun showBackgroundSettingsDialog(action: (actionType: String, dontAskAgain: Boolean) -> Unit){
        val dialogView = layoutInflater.inflate(R.layout.dialog_background_settings_vehicle_list, null, false)
        val btnCancel = dialogView.findViewById(R.id.btn_cancel) as Button
        val btnOk = dialogView.findViewById(R.id.btn_ok) as Button
        val checkBoxDontAskAgain = dialogView.findViewById(R.id.checkbox_dont_ask_again) as CheckBox
        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setView(dialogView)
            .create()
        btnCancel.setOnClickListener { dialog.dismiss(); action(ACTION_CANCEL, checkBoxDontAskAgain.isChecked) }
        btnOk.setOnClickListener { dialog.dismiss(); action(ACTION_OK, checkBoxDontAskAgain.isChecked) }
        dialog.show()
    }

    var hasInitializedRootView = false
    var rootView: View? = null

    fun getPersistentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?, layout: Int): View? {
        if (rootView == null) {
            // Inflate the layout for this fragment
            rootView = inflater?.inflate(layout,container,false)
        } else {
            // Do not inflate the layout again.
            // The returned View of onCreateView will be added into the fragment.
            // However it is not allowed to be added twice even if the parent is same.
            // So we must remove rootView from the existing parent view group
            // (it will be added back).
            (rootView?.getParent() as? ViewGroup)?.removeView(rootView)
        }

        return rootView
    }
}