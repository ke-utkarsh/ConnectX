package ymsli.com.ea1h.views.home.drivinghistory.filter

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author   (VE00YM023)
 * @date    20/8/20 3:10 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * TripFilterDialogFragment : This is the filter fragment wherein user can filter
 * list of trips based on speed, distance, bike, etc.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * VE00YM023            SEP 29, 2020        Modifications for the new UI
 * -----------------------------------------------------------------------------------
 */

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.appyvet.materialrangebar.RangeBar
import kotlinx.android.synthetic.main.dialog_trip_history_filter.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.adapters.VehicleListAdapter
import ymsli.com.ea1h.database.EA1HSharedPreferences.Companion.EMPTY_STRING
import ymsli.com.ea1h.model.TripHistoryFilterModel
import ymsli.com.ea1h.views.home.HomeViewModel
import ymsli.com.ea1h.views.home.drivinghistory.TripHistoryBluetoothViewModel
import java.text.SimpleDateFormat
import java.util.*

class TripFilterDialogFragment(private val viewModel: HomeViewModel): DialogFragment(),
    VehicleListAdapter.StatusListener {

    private val cal = Calendar.getInstance()
    private var currentState = TripHistoryFilterModel()
    private var previousState = TripHistoryFilterModel()
    private val dateFormatter = SimpleDateFormat("dd/MM/yy")

    companion object{
        val COLOR_VEHICLE_SELECTED = Color.parseColor("#0B2265")
        val COLOR_VEHICLE = Color.parseColor("#666666")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.dialog_trip_history_filter, container,  false)
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.statusBarColor = resources.getColor(R.color.bg_status_bar)
        dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
        setupCollapseExpandFeature()
        setupRangeBars()
        setupDialogState()
        setupUIByPreviousState()
        setupClickListeners()
        initDatePickers()
        setupVehicleList()
        setClearAndApplyBtnStateByCurrentState()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar)
    }

    /**
     * Configures collapse/expand feature for all the different filter sections.
     * @author VE00YM023
     */
    private fun setupCollapseExpandFeature(){
        content_vehicles.setUpCollapsible(iv_collapse_vehicle)
        content_date.setUpCollapsible(iv_collapse_date)
        content_distance.setUpCollapsible(iv_collapse_distance)
        content_brake_count.setUpCollapsible(iv_collapse_brake_count)
        content_avg_speed.setUpCollapsible(iv_collapse_avg_speed)
    }

    /**
     * Sets up a view to collapse/expand on click of given view.
     * @param toggle view to toggle the state between collapse/expand
     * @author VE00YM023
     */
    private fun View.setUpCollapsible(toggle: ImageView){
        toggle.setOnClickListener {
            this.visibility = if(this.isVisible) View.GONE else View.VISIBLE
            toggle.setImageResource(if(this.isVisible) R.drawable.ic_expand_less else R.drawable.ic_expand_more)
        }
    }

    /**
     * Setup the range bars to update the associated UI elements when RangeBar's
     * values are changes by moving the slider.
     * @author VE00YM023
     */
    private fun setupRangeBars(){
        range_bar_distance.setOnRangeBarChangeListener(object : RangeBarChangeAdapter(){
            override fun onValuesChanged(from: String?, to: String?) {
                if(from == null || to == null) return
                updateRangeBarUI(tv_from_distance, tv_to_distance, from, to)
                currentState.distance.from = from.toInt()
                currentState.distance.to = to.toInt()
                setClearAndApplyBtnStateByCurrentState()
            }
        })

        range_bar_brake_count.setOnRangeBarChangeListener(object : RangeBarChangeAdapter(){
            override fun onValuesChanged(from: String?, to: String?) {
                if(from == null || to == null) return
                updateRangeBarUI(tv_from_brake_count, tv_to_brake_count, from, to, EMPTY_STRING)
                currentState.brakeCount.from = from.toInt()
                currentState.brakeCount.to = to.toInt()
                setClearAndApplyBtnStateByCurrentState()
            }
        })

        range_bar_avg_speed.setOnRangeBarChangeListener(object : RangeBarChangeAdapter(){
            override fun onValuesChanged(from: String?, to: String?) {
                if(from == null || to == null) return
                updateRangeBarUI(tv_from_avg_speed, tv_to_avg_speed, from, to,"Km/h")
                currentState.avgSpeed.from = from.toInt()
                currentState.avgSpeed.to = to.toInt()
                setClearAndApplyBtnStateByCurrentState()
            }
        })
    }

    /**
     * Updates the UI associated with this range bar, given by the Text views.
     * @author VE00YM023
     */
    private fun updateRangeBarUI(fromTv: TextView, toTv: TextView,
                                 fromVal: String, toVal: String, suffix: String = "Km"){
        fromTv.text = "$fromVal $suffix"
        toTv.text = "$toVal $suffix"
        if(toVal.toInt() >= 100) { toTv.text = ">100 $suffix" }
    }

    /**
     * Gets the previous state of filter dialog from viewModel
     * and makes a copy of it named 'currentState'.
     */
    private fun setupDialogState() {
        previousState = viewModel.filterState
        currentState = previousState.clone()
    }

    /**
     * Depending on the previous state of the FilterDialog (as received from the viewModel),
     * configures the UI (checking appropriate radio's, setting up value of fromDate and toDate).
     */
    private fun setupUIByPreviousState() {
        if(previousState.date.from != 0L) {
            tv_from_date.text = dateFormatter.format(previousState.date.from)
        }
        if(previousState.date.to != 0L) {
            tv_to_date.text = dateFormatter.format(previousState.date.to)
        }

        range_bar_distance.setRangePinsByValue(previousState.distance.from.toFloat(), previousState.distance.to.toFloat())
        range_bar_brake_count.setRangePinsByValue(previousState.brakeCount.from.toFloat(), previousState.brakeCount.to.toFloat())
        range_bar_avg_speed.setRangePinsByValue(previousState.avgSpeed.from.toFloat(), previousState.avgSpeed.to.toFloat())
    }


    /**
     * Sets up the click listeners for the Clear and Apply buttons.
     */
    private fun setupClickListeners(){
        btn_clear.setOnClickListener {
            currentState.resetToDefault()
            resetUIToDefaultState()
            setClearAndApplyBtnStateByCurrentState()
        }

        btn_apply.setOnClickListener {
            viewModel.filterState = currentState
            viewModel.applyFilter.postValue(currentState)
            dismiss()
        }

        btn_close.setOnClickListener { dismiss() }
    }

    /**
     * Init the date pickers UI and configure the handlers to updated UI when a date is
     * selected.
     * @author Balraj VE00YM023
     */
    private fun initDatePickers(){
        configureDateInput(tv_from_date)
        configureDateInput(tv_to_date)
    }

    /**
     * Sets up the list of vehicles, multiple vehicles can be selected
     * to filter the trip list.
     * @author VE00YM023
     */
    private fun setupVehicleList(){
        currentState.vehicles?.let {
            rv_vehicles.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, true)
            rv_vehicles.adapter = VehicleListAdapter(it, this)
        }
    }

    /**
     * Configure a date input to use a OnDateSetListener which will pass the selected date
     * to UI so that it can update itself with the selected date.
     *
     * @author Balraj VE00YM023
     */
    private fun configureDateInput(datePicker: TextView) {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.clear()
            if(datePicker == tv_from_date) cal.set(year, monthOfYear, dayOfMonth, 0,0,0)
            else cal.set(year, monthOfYear, dayOfMonth, 23,59,59)
            updateDateWidget(datePicker, cal.timeInMillis)
        }

        datePicker.setOnClickListener {
            val datePick = DatePickerDialog(
                requireContext(),
                R.style.DialogTheme,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )

            if(datePicker == tv_from_date){
                datePick.datePicker.maxDate = System.currentTimeMillis()
                if(currentState.date.to != 0L){
                    val selectedToDate = currentState.date.to
                    cal.timeInMillis = selectedToDate
                    datePick.datePicker.maxDate = cal.timeInMillis
                }
            }

            if(datePicker == tv_to_date){
                datePick.datePicker.maxDate = System.currentTimeMillis()
                if(currentState.date.from != 0L){
                    val selectedFromDate = currentState.date.from
                    cal.timeInMillis = selectedFromDate
                    datePick.datePicker.minDate = (cal.timeInMillis)
                }
            }
            datePick.show()
        }
    }

    /**
     * Set the UI of date input fields
     * @author Balraj VE00YM023
     */
    private fun updateDateWidget(datePicker: TextView, millis: Long){
        if(datePicker == tv_to_date) { currentState.date.to = millis }
        else { currentState.date.from = millis }
        var dateString = dateFormatter.format(millis)
        datePicker.text = dateString
        setClearAndApplyBtnStateByCurrentState()
    }

    private fun resetUIToDefaultState(){
        tv_from_date.text = getString(R.string.placeholder_date)
        tv_to_date.text = getString(R.string.placeholder_date)
        range_bar_distance.setRangePinsByValue(0F, 100F)
        range_bar_brake_count.setRangePinsByValue(0F, 100F)
        range_bar_avg_speed.setRangePinsByValue(0F, 100F)
        rv_vehicles.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, true)
        rv_vehicles.adapter = VehicleListAdapter(currentState.vehicles!!, this)
    }

    /**
     * Depending on the current state of filter dialog,
     * enables/disables the Clear and Apply buttons.
     * @author VE00YM023
     */
    private fun setClearAndApplyBtnStateByCurrentState(){
        if(currentState.isSet()){
            btn_clear.isEnabled = true
            btn_apply.isEnabled = false
            if(currentState != previousState) { btn_apply.isEnabled = true }
        }
        else if(currentState != previousState){
            btn_clear.isEnabled = true
            btn_apply.isEnabled = true
        }
        else {
            btn_clear.isEnabled = false
            btn_apply.isEnabled = false
        }
    }

    override fun onStatusChanges() {
        setClearAndApplyBtnStateByCurrentState()
    }

    /**
     * Adapter for the OnRangeBarChangeListener
     * @author VE00YM023
     */
    private abstract class RangeBarChangeAdapter: RangeBar.OnRangeBarChangeListener{
        override fun onTouchEnded(rangeBar: RangeBar?) {}

        override fun onRangeChangeListener(
            bar: RangeBar?,
            lPIndex: Int,
            rPIndex: Int,
            lPValue: String?,
            rPValue: String?) = onValuesChanged(lPValue, rPValue)

        override fun onTouchStarted(rangeBar: RangeBar?) {}

        abstract fun onValuesChanged(from: String?, to: String?)
    }
}