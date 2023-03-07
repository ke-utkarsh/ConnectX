package ymsli.com.ea1h.views.home.drivinghistory

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author   (VE00YM129)
 * @date    22/2/20 3:10 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * TripEntityViewHolder : View holder to show the item of trip history
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import android.content.Intent
import android.location.Geocoder
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.trip_recycler_item.view.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseItemViewHolder
import ymsli.com.ea1h.database.EA1HSharedPreferences.Companion.EMPTY_STRING
import ymsli.com.ea1h.database.entity.BikeEntity
import ymsli.com.ea1h.database.entity.TripEntity
import ymsli.com.ea1h.di.component.ViewHolderComponent
import ymsli.com.ea1h.utils.common.Utils
import ymsli.com.ea1h.utils.common.toUIView
import java.lang.Exception
import java.util.*

class TripEntityViewHolder(parent: ViewGroup, var bikeEntityMap: Map<String, BikeEntity>)
    : BaseItemViewHolder<TripEntity, TripItemViewModel>(R.layout.trip_recycler_item,parent) {

    companion object{
        private const val TAG = "TripEntityViewHolder"
        const val TRIP_DETAIL_INTENT = "TRIP_DETAIL_INTENT"
    }

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) = viewHolderComponent.inject(this)

    override fun setupView(view: View) {
        val tripDetailIntent = Intent(itemView.context,TripHistoryDetailActivity::class.java)
        /* Setup the click listener to open the TripHistoryDetailActivity */
        view.setOnClickListener {
            val selectedTrip = (viewModel.data.value as TripEntity)
            tripDetailIntent.putExtra(TRIP_DETAIL_INTENT,selectedTrip)
            tripDetailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            itemView.context.startActivity(tripDetailIntent)
        }
    }

    /**
     * Setup LiveData observers to update the TripHistory list item UI,
     * as the data set changes.
     */
    override fun setupObservers() {
        super.setupObservers()
        viewModel.bikeChassisNumber.observe(this, Observer {
            bikeEntityMap[it]?.let { bike ->
                itemView.tv_bike_model.text = bike.bkModNm.toUIView()
                if(bike.bdp != null){
                    Picasso.get().load(bike.bdp).fit().centerCrop().into(itemView.iv_bike_dp)
                }
                /* Set the bike/scooter icon in header depending on the type of vehicle */
                itemView.ic_bike.setImageResource(when(bike.vehType){
                    BikeEntity.VEHICLE_TYPE_SCOOTER -> {R.drawable.ic_riding_history_scooter}
                    else -> {R.drawable.ic_bike}
                })
            }
        })

        viewModel.source.observe(this, Observer {
            try {
                if(it.isNullOrBlank() || it.contains("null",true)) throw  Exception()
                itemView.tv_trip_from.text = it
            }
            catch (ex: Exception){
                try{
                    itemView.tv_trip_from.text = getSourceAddressLine()
                }
                catch (excep: Exception){
                    itemView.tv_trip_from.text = itemView.context.resources.getString(R.string.na)
                    //FirebaseCrashlytics.getInstance().recordException(ex)
                }
            }
        })

        viewModel.destination.observe(this, Observer {
            try{
                if(it.isNullOrBlank() || (it.contains("null",true))) throw  Exception()
                itemView.tv_trip_to.text = it
            }
            catch (ex: Exception){
                try{
                    itemView.tv_trip_to.text = getDestinationAddressLine()
                }
                catch (excep:Exception){
                    itemView.tv_trip_to.text = itemView.context.resources.getString(R.string.na)
                    //FirebaseCrashlytics.getInstance().recordException(ex)
                }
            }
        })

        viewModel.startDateTime.observe(this, Observer {
            if(it == null){
                itemView.tv_trip_date.text = EMPTY_STRING.toUIView()
                itemView.tv_trip_time.text = EMPTY_STRING.toUIView()
            }
            else{
                itemView.tv_trip_date.text = Utils.getTripDate(it)
                itemView.tv_trip_time.text = Utils.getTripTime(it)
            }
        })

        viewModel.isActiveTrip.observe(this, Observer {
            if(it!=null && it){
                itemView.tv_trip_status.text = itemView.context.resources.getString(R.string.labelTripStatusOnGoing)
            }
        })
    }

    /**
     * avoids NA string for the address in the trip history list
     */
    private fun getSourceAddressLine(): String?{
        if(viewModel.trip.value!=null && (viewModel.trip.value)?.startLat!=null && (viewModel.trip.value)?.startLon!=null && viewModel.checkInternet()){
            SystemClock.sleep(1500)
            val geocoder = Geocoder(itemView.context, Locale.getDefault())
            val addressString = viewModel.getSourceAddress(geocoder).blockingGet()//geocoder.getFromLocation((viewModel.trip.value)?.startLat!!,(viewModel.trip.value)?.startLon!!,1)
            viewModel.updateTripSourceAddress(addressString).blockingGet()
            return addressString
        }
        return itemView.context.resources.getString(R.string.na)
    }

    /**
     * avoids NA string for the address in the trip history list
     */
    private fun getDestinationAddressLine(): String?{
        if(viewModel.trip.value!=null && (viewModel.trip.value)?.endLat!=null && (viewModel.trip.value)?.endLon!=null && viewModel.checkInternet()){
            SystemClock.sleep(1500)
            val geocoder = Geocoder(itemView.context, Locale.getDefault())
            val addressString = viewModel.getAddress(geocoder).blockingGet()
            viewModel.updateTripDestinationAddress(addressString).blockingGet()
            return addressString
        }
        return itemView.context.resources.getString(R.string.na)
    }
}