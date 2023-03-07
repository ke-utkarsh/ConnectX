package ymsli.com.ea1h.views.home.elock

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author   (VE00YM129)
 * @date    24/7/20 3:10 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ELockActivity : This activity is responsible for reading/writing elock pattern
 * to/from the device.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.elock_fragment.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseFragment
import ymsli.com.ea1h.di.component.FragmentComponent
import ymsli.com.ea1h.services.ECUParameters
import ymsli.com.ea1h.utils.common.Event
import ymsli.com.ea1h.utils.common.ViewUtils
import ymsli.com.ea1h.views.home.HomeViewModel
import java.lang.Exception

class ELockFragment : BaseFragment<HomeViewModel>() {

    private lateinit var ecuParameters:ECUParameters

    override fun injectDependencies(fragmentComponent: FragmentComponent) = fragmentComponent.inject(this)

    override fun setupView(view: View) {
        ecuParameters = ECUParameters.getEcuParametersInstance()
        disableAllRadioButtons()
        btn_set_elock.setOnClickListener {
            if(ecuParameters.isConnected) {
                if(ecuParameters.isIgnited) {
                    if (switch_enable_elock.isChecked) {
                        //sends MFECU command to set ELock pattern
                        val indexArray = arrayOf(
                            rg_seq1.indexOfChild(rg_seq1.findViewById<View>(rg_seq1.checkedRadioButtonId)),
                            rg_seq2.indexOfChild(rg_seq2.findViewById<View>(rg_seq2.checkedRadioButtonId)),
                            rg_seq3.indexOfChild(rg_seq3.findViewById<View>(rg_seq3.checkedRadioButtonId)),
                            rg_seq4.indexOfChild(rg_seq4.findViewById<View>(rg_seq4.checkedRadioButtonId))
                        )
                        val sum = indexArray[0]+indexArray[1]+indexArray[2]+indexArray[3]
                        if(sum == 0 || sum == 4){
                            showErrorsMessageViaSnackbar(requireContext().getString(R.string.choose_another_pattern))
                        }
                        else{
                            viewModel.elockSettingState.value = 2
                            val bytesQuery = ViewUtils.generateELockCommand(indexArray)
                            viewModel.setELockPattern.postValue(Event(bytesQuery))
                        }
                    }
                }
                else {
                    showErrorsMessageViaSnackbar(requireContext().getString(R.string.ignition_off))
                }
            }
            else{
                showErrorsMessageViaSnackbar(resources.getString(R.string.ecu_disconnected))
            }
        }

        changePatternButtonUI(false)
        //Toggle switch click listener to enable/disable radio buttons and save button
        switch_enable_elock.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(!ecuParameters.isConnected){
                    showErrorsMessageViaSnackbar(resources.getString(R.string.ecu_disconnected))
                    switch_enable_elock.isChecked = false
                }
                else{
                    rad_btn_left1.isClickable = isChecked
                    rad_btn_right1.isEnabled = isChecked

                    rad_btn_left2.isClickable = isChecked
                    rad_btn_right2.isEnabled = isChecked

                    rad_btn_left3.isClickable = isChecked
                    rad_btn_right3.isEnabled = isChecked

                    rad_btn_left4.isClickable = isChecked
                    rad_btn_right4.isEnabled = isChecked

                    if(!isChecked){
                        uncheckAllRadioButtons()
                        changePatternButtonUI(false)
                        if(!isAutoChecked && viewModel.getIfElockCanBeDisabled()){
                            viewModel.showProgress.postValue(true)
                            Handler(Looper.getMainLooper()).postDelayed({
                                viewModel.showProgress.postValue(false)
                            }, 2000)
                            // send disable ELock command
                            viewModel.elockSettingState.value = 0
                            viewModel.enableELock.postValue(Event(false))
                        }
                        isAutoChecked = true
                    }
                    else{
                        checkDefaultRadioButtons()
                        getELockPattern()
                        changePatternButtonUI(true)
                        if(isAutoChecked && viewModel.getIfElockCanBeDisabled()) {
                            viewModel.showProgress.postValue(true)
                            Handler(Looper.getMainLooper()).postDelayed({
                                viewModel.showProgress.postValue(false)
                            }, 2000)
                            // send enable ELock command
                            viewModel.elockSettingState.value = 1
                            viewModel.enableELock.postValue(Event(true))
                        }
                        isAutoChecked = false
                    }
                }
            }
        })
    }

    private var isAutoChecked: Boolean = true

    override fun onStart() {
        super.onStart()
        if(ecuParameters.isConnected){
            val status = viewModel.getELockEnabledStatus()
            switch_enable_elock.isChecked = status
            isAutoChecked = !status
            if(status) {
                getELockPattern()
                changePatternButtonUI(true)
            }
        }
        else{
            switch_enable_elock.isChecked = false
        }
    }

    /**
     * shows disabled/enabled pattern button on UI
     */
    private fun changePatternButtonUI(isEnable: Boolean){
        if(isEnable){
            rad_btn_left1.background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_elock_btns_left)
            rad_btn_left2.background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_elock_btns_left)
            rad_btn_left3.background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_elock_btns_left)
            rad_btn_left4.background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_elock_btns_left)
            rad_btn_right1.background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_elock_btns_left)
            rad_btn_right2.background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_elock_btns_left)
            rad_btn_right3.background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_elock_btns_left)
            rad_btn_right4.background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_elock_btns_left)
            btn_set_elock.background = ContextCompat.getDrawable(requireContext(),R.color.colorPrimary)
        }
        else{
            rad_btn_left1.background = ContextCompat.getDrawable(requireContext(),R.drawable.indicator_button_left_disabled)
            rad_btn_left2.background = ContextCompat.getDrawable(requireContext(),R.drawable.indicator_button_left_disabled)
            rad_btn_left3.background = ContextCompat.getDrawable(requireContext(),R.drawable.indicator_button_left_disabled)
            rad_btn_left4.background = ContextCompat.getDrawable(requireContext(),R.drawable.indicator_button_left_disabled)
            rad_btn_right1.background = ContextCompat.getDrawable(requireContext(),R.drawable.left_indicator_button_not_selected_shape)
            rad_btn_right2.background = ContextCompat.getDrawable(requireContext(),R.drawable.left_indicator_button_not_selected_shape)
            rad_btn_right3.background = ContextCompat.getDrawable(requireContext(),R.drawable.left_indicator_button_not_selected_shape)
            rad_btn_right4.background = ContextCompat.getDrawable(requireContext(),R.drawable.left_indicator_button_not_selected_shape)
            btn_set_elock.background = ContextCompat.getDrawable(requireContext(),R.color.colorPrimary)
        }
    }

    override fun provideLayoutId(): Int = R.layout.elock_fragment

    override fun setupObservers() {
        super.setupObservers()
        viewModel.eLockPattern.observe(this, Observer {//triggered when elock pattern is successfully read.
            it.getIfNotHandled()?.let {
                try {
                    if (it.get(0) == ViewUtils.CHAR_L) rad_btn_left1.isChecked = true
                    if (it.get(0) == ViewUtils.CHAR_R) rad_btn_right1.isChecked = true

                    if (it.get(1) == ViewUtils.CHAR_L) rad_btn_left2.isChecked = true
                    if (it.get(1) == ViewUtils.CHAR_R) rad_btn_right2.isChecked = true

                    if (it.get(2) == ViewUtils.CHAR_L) rad_btn_left3.isChecked = true
                    if (it.get(2) == ViewUtils.CHAR_R) rad_btn_right3.isChecked = true

                    if (it.get(3) == ViewUtils.CHAR_L) rad_btn_left4.isChecked = true
                    if (it.get(3) == ViewUtils.CHAR_R) rad_btn_right4.isChecked = true
                }
                catch (ex: Exception){

                }
            }

        })
    }

    /**
     * reads elock pattern from
     * mfecu by sending read command
     */
    private fun getELockPattern(){
        if(ecuParameters.isConnected) {
            viewModel.getELockPattern.postValue(Event(true))
        }
    }

    private fun disableAllRadioButtons(){
        val elockStatus = viewModel.getELockEnabledStatus()
        rad_btn_left1.isClickable = elockStatus
        rad_btn_left2.isClickable = elockStatus
        rad_btn_left3.isClickable = elockStatus
        rad_btn_left4.isClickable = elockStatus
        rad_btn_right1.isEnabled = elockStatus
        rad_btn_right2.isEnabled = elockStatus
        rad_btn_right3.isEnabled = elockStatus
        rad_btn_right4.isEnabled = elockStatus
    }

    private fun checkDefaultRadioButtons(){
        rg_seq1.check(R.id.rad_btn_right1)
        rg_seq2.check(R.id.rad_btn_right2)
        rg_seq3.check(R.id.rad_btn_right3)
        rg_seq4.check(R.id.rad_btn_right4)
    }

    private fun uncheckAllRadioButtons(){
        rg_seq1.clearCheck()
        rg_seq2.clearCheck()
        rg_seq3.clearCheck()
        rg_seq4.clearCheck()
    }
}
