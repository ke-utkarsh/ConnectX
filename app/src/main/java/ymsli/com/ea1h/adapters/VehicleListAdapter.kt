package ymsli.com.ea1h.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.bike_make_recycler_item.view.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.utils.common.Constants.NA
import ymsli.com.ea1h.utils.common.CryptoHandler
import ymsli.com.ea1h.views.home.drivinghistory.filter.TripFilterDialogFragment.Companion.COLOR_VEHICLE
import ymsli.com.ea1h.views.home.drivinghistory.filter.TripFilterDialogFragment.Companion.COLOR_VEHICLE_SELECTED

/**
 * Project Name : HRIS Mobile Application
 *
 * @author VE00YM023
 * @company YMSLI
 * @date AUG 10, 2020
 * Copyright (c) 2020, Yamaha Motor Solution (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * VehicleListAdapter : Adapter for the Vehicle list RecyclerView in Trip Filter.
 * -----------------------------------------------------------------------------------
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 *
 * -----------------------------------------------------------------------------------
 */

class VehicleListAdapter(private var dataSet: ArrayList<VehicleModel>,
                         private val statusListener: StatusListener):
    RecyclerView.Adapter<VehicleListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.bike_make_recycler_item, parent, false)
        return ViewHolder(viewHolder)
    }

    /**
     * Bind the text view with vehicle registration no and
     * setup click listener to change item color to highlight it as selected,
     * and notify the activity so that it can mark this item as selected.
     * @author VE00YM023
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataSet[position]
        val v = holder.itemView
        if(!currentItem.regNo.isNullOrBlank()) v.make_tv.text = CryptoHandler.decrypt(currentItem.regNo)
        else v.make_tv.text = NA
        if(currentItem.selected) v.make_tv.setTextColor(COLOR_VEHICLE_SELECTED)
        v.setOnClickListener {
            v.make_tv.setTextColor(if(currentItem.selected) COLOR_VEHICLE else COLOR_VEHICLE_SELECTED)
            currentItem.selected = !currentItem.selected
            statusListener.onStatusChanges()
        }
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

    /** Used to maintain an item and its current state (Selected, Unselected) */
    class VehicleModel(val regNo: String?, var chassisNo: String?, var selected: Boolean)

    /** Interface to notify the activity of item's state change */
    interface StatusListener{ fun onStatusChanges() }
}