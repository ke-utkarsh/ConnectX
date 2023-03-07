package ymsli.com.ea1h.views.home.parkinglocation

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM023)
 * @date    14/02/2020 3:00 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ParkingLocationFragment : This fragment is used to show the user last parking location
 * of their bike on a GoogleMap instance.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * VE00YM023            11/08/2020          Removed the tripEntity constructor param.
 *                                          it was causing issues with navController's
 *                                          directions API.
 *                                          Now this fragment automatically loads the last
 *                                          trip data from repo, when required.
 * -----------------------------------------------------------------------------------
 */

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.android.synthetic.main.fragment_parking_location.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseFragment
import ymsli.com.ea1h.database.entity.BikeEntity
import ymsli.com.ea1h.database.entity.TripEntity
import ymsli.com.ea1h.di.component.FragmentComponent
import ymsli.com.ea1h.utils.common.Constants
import ymsli.com.ea1h.utils.common.Constants.NA
import ymsli.com.ea1h.utils.common.Utils
import ymsli.com.ea1h.utils.common.ViewUtils
import ymsli.com.ea1h.utils.common.formatForUI
import ymsli.com.ea1h.views.home.HomeFragmentDirections
import ymsli.com.ea1h.views.home.HomeViewModel
import ymsli.com.ea1h.views.home.drivinghistory.TripEntityViewHolder
import ymsli.com.ea1h.views.home.drivinghistory.TripHistoryDetailActivity
import java.text.DecimalFormat


class ParkingLocationFragment : BaseFragment<HomeViewModel>(), OnMapReadyCallback {

    companion object{
        const val LOADED_IN_HOME = "loadedInHome"
        private const val NO_VALUE = "NA"
        private const val PIN_DESCRIPTION = "Address"
        private const val DECIMAL_FORMAT_ZERO = "0.00"
        private const val SUFFIX_METER = "M"
    }

    private lateinit var map: GoogleMap
    private var loadedInHome = true
    private var tripEntity: TripEntity? = null

    override fun provideLayoutId() = R.layout.fragment_parking_location

    override fun injectDependencies(fragmentComponent: FragmentComponent) =
        fragmentComponent.inject(this)

    override fun setupView(view: View) {
        cl_parking_location_text.setOnClickListener {
            val intent = Intent(activity,TripHistoryDetailActivity::class.java)
            intent.putExtra(TripEntityViewHolder.TRIP_DETAIL_INTENT,tripEntity)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    /**
     * Binds the trip details data on UI,
     * these details are only visible when this fragment is loaded in the home screen.
     */
    private fun bindTripDetails(trip: TripEntity){
        trip.chassisNumber?.let {
            viewModel.getBikeRegNum(it).observe(viewLifecycleOwner, Observer { regNo ->
                if(regNo!=null){
                    tv_reg_num.text = viewModel.decryptData(regNo)
                    tv_reg_num .setTextColor(ContextCompat.getColor(requireActivity().applicationContext, R.color.white))
                }
                else{
                    tv_reg_num .setTextColor(ContextCompat.getColor(requireActivity().applicationContext, R.color.colorAddBike))
                    tv_reg_num.text = NA
                }
            })

            /* Set vehicle icon depending on the type(scooter/bike) of vehicle */
            val vehicle = viewModel.getVehicleByChassisNo(trip.chassisNumber!!)
            val vehicleIcon = when(vehicle.vehType){
                BikeEntity.VEHICLE_TYPE_SCOOTER -> R.drawable.ic_last_ride_scooter_vector
                else -> R.drawable.icon_bike_blue
            }
            tv_reg_num.setCompoundDrawablesWithIntrinsicBounds(vehicleIcon, 0, 0, 0)

        }
        val tripTiming = "${Utils.getDayTime(trip.startTime ?: 0)} - ${Utils.getDayTime(trip.endTime ?: trip.potentialEndTime?:Utils.getTimeInMilliSec())}"
        tv_trip_time.text = tripTiming
        if(!trip.endAddress.isNullOrBlank()) tv_trip_end.text = trip.endAddress
        else tv_trip_end.text = NO_VALUE

        if(!trip.startAddress.isNullOrBlank()) tv_trip_start.text = trip.startAddress
        else tv_trip_start.text = NO_VALUE
        val df = DecimalFormat(DECIMAL_FORMAT_ZERO)

        if(trip.distanceCovered?:0f<1000f){
            tv_trip_distance.text = "${df.format(trip.distanceCovered)} $SUFFIX_METER"
        }
        else{
            val tempDistance = (trip.distanceCovered ?: 0f) / 1000
            tv_trip_distance.text = tempDistance.formatForUI("Km")
        }

        //format last trip start time and show it to the user
        if(trip.startTime!=null) tv_last_ride_date.text = Utils.getTimeFormatTripStartParkingDetail(trip.startTime!!)
        else tv_last_ride_date.text = Constants.NA

        try {
            tv_time_taken.text = Utils.getTripDuration(trip.startTime!!, trip.endTime!!)
            trip.updatedOn?.let { tv_trip_duration.text = Utils.getTimeDiffFromNow(it) }
        }
        catch (ex: Exception){ }
    }

    /**
     * Here we retrieve the fragment argument loadedInHome.
     * this argument lets us decide if the map is loaded in the home screen
     * or its loaded in an independent fragment.
     *
     * When map fragment is loaded independently then we have to hide the
     * parking record text content.
     *
     * @author VE00YM023
     *
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadedInHome = arguments?.getBoolean(LOADED_IN_HOME) ?: true
        /** Only show trip details, when this fragment is loaded in home screen */
        if (!loadedInHome) {
            hideTextContent()
            card_map_container.radius = 0f
            cl_container_parking_location_map.setPadding(0,0,0,0)
            val params = cl_container_parking_location_map.layoutParams as ViewGroup.MarginLayoutParams
            params.leftMargin = 0
            params.rightMargin = 0
        }
        viewModel.getLastTripLiveData().observe(viewLifecycleOwner, Observer {
            /* If Last trip is not available then display the 'No Parking Record' Text */
            if(it == null){
                cl_container_parking_location_map.visibility = View.GONE
                tv_no_parking_record.visibility = View.VISIBLE
                root_container_parking_location.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_app)
            }
            else{
                this.tripEntity = it
                if(loadedInHome && !it.isActive) { bindTripDetails(it) }
                initMap()
            }
        })
    }

    /**
     * Initialize the Google map instance and register this fragment
     * to receive notifications when the map instance becomes ready.
     *
     * @author VE00YM023
     */
    private fun initMap() {
        (childFragmentManager.findFragmentById(R.id.fragment_google_maps)
                as SupportMapFragment).onResume()
        (childFragmentManager.findFragmentById(R.id.fragment_google_maps)
                as SupportMapFragment).getMapAsync(this)
    }

    /**
     * Hide the parking location text content when the map is opened in
     * full screen.
     *
     * @author VE00YM023
     */
    private fun hideTextContent(){
        cl_parking_location_text.visibility = View.GONE
        val lp: ConstraintLayout.LayoutParams = (cl_container_parking_location_map.layoutParams as ConstraintLayout.LayoutParams)
        lp.matchConstraintPercentHeight = 1f
        cl_container_parking_location_map.layoutParams = lp
    }

    /**
     * When map instance is ready populate it to show the last parking location.
     * and setup the click listener to open the full screen view.
     * currently we show the default location.
     *
     * @author VE00YM023
     */
    override fun onMapReady(googleMap: GoogleMap) {
        // show the actual parking location
        try {
            map = googleMap
            if (tripEntity != null) {
                val lastLat = tripEntity?.endLat ?: tripEntity?.potentialLastLatitude
                val lastLong = tripEntity?.endLon ?: tripEntity?.potentialLastLongitude

                if (lastLat != null && lastLong != null) {
                    val parkingLocation = LatLng(lastLat, lastLong) //show this location over map
                    val pinResource = if(loadedInHome){
                        BitmapDescriptorFactory.fromResource(R.drawable.icon_parking)
                    }
                    else { BitmapDescriptorFactory.fromBitmap(loadMapPinBitmap()) }

                    map.addMarker(MarkerOptions().position(parkingLocation)
                        .draggable(true)
                            .title(tripEntity?.endAddress?.trim() ?: PIN_DESCRIPTION)
                            .icon(pinResource))

                    map.moveCamera(CameraUpdateFactory.newLatLng(parkingLocation))
                    map.animateCamera(CameraUpdateFactory.zoomTo(10f))

                    /** Open the full screen view when map clicked and it is loaded in the home screen */
                    if (loadedInHome) {
                        map.setOnMapClickListener {
                            val action =
                                HomeFragmentDirections.actionNavHomeToParkingLocationFragment(false)
                            findNavController(this).navigate(action)
                        }
                    }
                }
            }
        }
        catch (ex: java.lang.Exception){
            FirebaseCrashlytics.getInstance().recordException(ex)
        }
    }

    private fun loadMapPinBitmap(): Bitmap? {
        val drawable = activity?.getDrawable(R.drawable.ic_map_pin_parking_record)!!
        val size = ViewUtils.dpToFloat(requireContext(), 1f).toInt()
        drawable.setBounds(size, size, size, size)
        return (drawable as BitmapDrawable).bitmap
    }
}