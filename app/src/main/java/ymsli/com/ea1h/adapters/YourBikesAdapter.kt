package ymsli.com.ea1h.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.your_bikes_item.view.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.database.entity.BikeEntity
import ymsli.com.ea1h.utils.common.CryptoHandler
import ymsli.com.ea1h.utils.common.toUIView

/*
 * Project Name : EA1h
 * @company YMSLI
 * @author  Balraj (VE00YM023)
 * @date    18/12/2020 09:00 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * YourBikesAdapter : Recycler Adapter for the Your vehicles screen.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

class YourBikesAdapter(private val selectedPosition: Int,
                       private var dataSet: List<BikeEntity>,
                       private var itemClickListener: BikeSelectionListener)
    : RecyclerView.Adapter<YourBikesAdapter.ViewHolder>() {

    companion object { const val NO_BIKE_SELECTED = -1 }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.your_bikes_item, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataSet[position]
        val v = holder.itemView
        v.setOnClickListener { itemClickListener.selectedBike(currentItem.chasNum) }
        val regNo = currentItem.regNum
        v.tv_registration_no.text = if(regNo.isNullOrEmpty()) regNo.toUIView() else CryptoHandler.decrypt(regNo)
        v.tv_bike_model.text = currentItem.bkModNm.toUIView()

        if(!currentItem.bdp.isNullOrEmpty()) {
            Picasso.get().load(currentItem.bdp).fit().centerCrop().into(v.iv_bike)
        }

        /* Highlight selected bike */
        if(position == selectedPosition){
            v.tv_bike_model.setTextColor(ContextCompat.getColor(v.context, R.color.text_bike_list_item_highlight))
            v.tv_registration_no.setTextColor(ContextCompat.getColor(v.context, R.color.text_bike_list_item_highlight))
        }
        else{
            v.tv_bike_model.setTextColor(ContextCompat.getColor(v.context, R.color.text_bike_list_item))
            v.tv_registration_no.setTextColor(ContextCompat.getColor(v.context, R.color.text_bike_list_item))
        }
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) { fun bindItems() {
        //method is empty as it will be used by android OS during runtime to
        // bind RV items
    } }

    interface BikeSelectionListener{ fun selectedBike(chassisNumber: String) }
}