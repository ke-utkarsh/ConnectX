package ymsli.com.ea1h.views.yourvehicles

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author   (VE00YM129)
 * @date    24/7/20 3:10 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * YourBikesViewHolder : This is the view holder of bikes in bikes recycler view
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.your_bikes_item.view.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseItemViewHolder
import ymsli.com.ea1h.database.entity.BikeEntity
import ymsli.com.ea1h.di.component.ViewHolderComponent
import ymsli.com.ea1h.utils.common.Utils
import ymsli.com.ea1h.utils.common.toUIView

class YourBikesViewHolder(parent: ViewGroup,private val bikeSelectionListener: BikeSelectionListener): BaseItemViewHolder<BikeEntity,YourBikesListItemViewModel>(
    R.layout.your_bikes_item,parent) {

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) = viewHolderComponent.inject(this)

    override fun setupView(view: View) {
        view.setOnClickListener {
            bikeSelectionListener.selectedBike(viewModel.data.value!!.chasNum) //triggers callback method in activity when item in bikes list is selected
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.bikeModelName.observe(this, Observer {
            itemView.tv_bike_model.text = it.toUIView()
        })

        viewModel.regNum.observe(this, Observer {
            val regNum = if(it.isNullOrEmpty()) it.toUIView() else viewModel.decryptData(it)
            itemView.tv_registration_no.text = regNum
        })

        viewModel.bikePic.observe(this, Observer {
            if(!it.isNullOrEmpty()) Glide.with(itemView.context).load(it).into(itemView.iv_bike)
        })
    }

    /*Bike selection listener interface*/
    interface BikeSelectionListener{
        fun selectedBike(chassisNumber: String)
    }
}

