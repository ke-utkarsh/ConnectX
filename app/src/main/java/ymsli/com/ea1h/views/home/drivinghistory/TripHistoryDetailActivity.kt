package ymsli.com.ea1h.views.home.drivinghistory

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author   (VE00YM129)
 * @date    22/7/20 3:10 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * TripHistoryDetailActivity : This activity shows the trip detail to the user
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.trip_detail_activity.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseActivity
import ymsli.com.ea1h.database.EA1HSharedPreferences.Companion.EMPTY_STRING
import ymsli.com.ea1h.database.entity.LatLongEntity
import ymsli.com.ea1h.database.entity.TripEntity
import ymsli.com.ea1h.di.component.ActivityComponent
import ymsli.com.ea1h.services.ECUParameters
import ymsli.com.ea1h.utils.common.Utils
import ymsli.com.ea1h.utils.common.formatForUI
import ymsli.com.ea1h.utils.common.toUIView
import java.lang.Exception

class TripHistoryDetailActivity : BaseActivity<TripHistoryDetailViewModel>(),
    OnMapReadyCallback  {

    private companion object {
        private const val TRIP_START_HEADER = "TRIP START ADDRESS"
        private const val TRIP_END_ADDRESS = "TRIP END ADDRESS"
        private const val GRAPH_ERROR = "Unable to plot graph, information not available"
        private const val PADDING_MULTIPLIER = 0.14
        private const val DISTANCE_SUFFIX = "M"
        private const val DISTANCE_SUFFIX_KM = "Km"
        private const val SPEED_SUFFIX = "Km/h"
        private const val MAP_PIN_SIZE_DP = 1f
        private const val BATTERY_VOLTAGE_SUFFIX = " v"
    }

    private var mMap: GoogleMap? = null
    private lateinit var destCoor: LatLng
    private lateinit var sourceCoor: LatLng
    private lateinit var selectedTrip: TripEntity
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var coordinates: Array<LatLongEntity>
    private lateinit var mapPinBitmap: Bitmap

    override fun provideLayoutId(): Int = R.layout.trip_detail_activity
    override fun injectDependencies(ac: ActivityComponent) = ac.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {
        window?.statusBarColor = getColor(R.color.bg_status_bar)
        loadMapPinDrawable()
        selectedTrip = (intent.getSerializableExtra(TripEntityViewHolder.TRIP_DETAIL_INTENT) as TripEntity)
        viewModel.tripId = selectedTrip.tripId
        viewModel.getTripDetailFromRoom()
        ecuParameters = ECUParameters.getEcuParametersInstance()
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        if(selectedTrip.chassisNumber!=null) {
            viewModel.getBikeRegNumModel(selectedTrip.chassisNumber!!).observe(this, Observer {
                if(it!=null){
                    tv_bike_model.text = it.mod
                    tv_vehicle_reg.text = viewModel.decryptData(it.regNum ?: EMPTY_STRING).toUIView()
                }
            })
        }

        tv_from.text = selectedTrip.startAddress.toUIView()
        tv_to.text = selectedTrip.endAddress.toUIView()
        if(selectedTrip.distanceCovered?:0f<1000f) tv_distance.text = selectedTrip.distanceCovered.formatForUI(DISTANCE_SUFFIX)
        else{
            val tempDistance = selectedTrip.distanceCovered?:0f
            selectedTrip.distanceCovered = (tempDistance/1000)
            tv_distance.text = selectedTrip.distanceCovered.formatForUI(DISTANCE_SUFFIX_KM)
        }
        try {
            tv_speed.text = selectedTrip.averageSpeed.formatForUI(SPEED_SUFFIX)
        }
        catch (ex: Exception){
            tv_speed.text = "0 ${SPEED_SUFFIX}"
        }
        tv_brake_count.text = selectedTrip.breakCount?.toString()?.toUIView()
        tv_battery_voltage.text = Utils.getBatteryVoltageFormatted(selectedTrip.battery?.toString(), BATTERY_VOLTAGE_SUFFIX)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        tv_start_time.text = Utils.formatForTripDetail(selectedTrip.startTime?:Utils.getTimeInMilliSec())
        tv_end_time.text = Utils.formatForTripDetail(selectedTrip.endTime?:selectedTrip.potentialEndTime?:Utils.getTimeInMilliSec())
        setupAddressClickListeners()
        if(ecuParameters.isConnected){
            changeBTIconColor(true)
        }
    }

    /**
     * When map is ready we try to plot the Trip graph,
     * if required information (Lat, Long) are not available then we
     * notify the user and finish the activity.
     */
     override fun onMapReady(googleMap: GoogleMap?) {
         mMap = googleMap
         mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
         mMap?.uiSettings?.isZoomControlsEnabled = true
         mMap?.uiSettings?.isScrollGesturesEnabled = true

        /* If latitude and longitude information of the trip is available
         *  then we plot the graph on the map */
             sourceCoor = LatLng(coordinates[0].latitude.toDouble(), coordinates[0].longitude.toDouble())
             destCoor = LatLng(coordinates[coordinates.size - 1].latitude.toDouble(), coordinates[coordinates.size - 1].longitude.toDouble())
             val source = sourceCoor
             val destination = destCoor

//             val marker1 = MarkerOptions().position(source).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_map))
//
//             val marker2 = MarkerOptions().position(destination)
//                 .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_map))

//             mMap?.addMarker(marker1)
//             mMap?.addMarker(marker2)
             val markers: ArrayList<MarkerOptions> = ArrayList()
//             markers.add(marker1)
//             markers.add(marker2)
             val latitudeList = ArrayList<Double>()
             val longitudeList = ArrayList<Double>()

             for (i in coordinates.indices) {//plots these location in that order over a map
                 latitudeList.add(coordinates.get(i).latitude.toDouble())
                 longitudeList.add(coordinates.get(i).longitude.toDouble())
             }
             for (i in 0 until latitudeList.size) {
                 val coordinate = LatLng(latitudeList[i], longitudeList[i])
                 val tempMarker = MarkerOptions().position(coordinate)
                     .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_map))
                 tempMarker.visible(false)
                 markers.add(tempMarker)
             }

             val builder = LatLngBounds.Builder()
             for (marker in markers) {
                 builder.include(marker.position)
             }
             val bounds = builder.build()

             val width = resources.displayMetrics.widthPixels;
             val height = resources.displayMetrics.heightPixels;
             val minMetric = width.coerceAtMost(height)
             val padding = (minMetric * PADDING_MULTIPLIER)
             val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, (height / 3), padding.toInt())
             mMap?.moveCamera(cu)

             val polyLines = PolylineOptions().add(sourceCoor)
             for (i in 0 until latitudeList.size) {
                 val tempCoordinates = LatLng(latitudeList[i], longitudeList[i])
                 polyLines.add(tempCoordinates)
             }
             polyLines.add(destCoor)
             mMap?.addPolyline(polyLines)

             //* Show pins for start and end address of the trip
             val startLatLng = LatLng(latitudeList[0], longitudeList[0])
             val endLatLng = LatLng(latitudeList[latitudeList.size-1], longitudeList[longitudeList.size-1])
             mMap?.addMarker(MarkerOptions().position(startLatLng)
                 .title(selectedTrip.startAddress))
                 ?.setIcon(BitmapDescriptorFactory.fromBitmap(mapPinBitmap))
             mMap?.addMarker(MarkerOptions().position(endLatLng)
                 .title(selectedTrip.endAddress))
                 ?.setIcon(BitmapDescriptorFactory.fromBitmap(mapPinBitmap))
     }


    /**
     * Setup the click listeners from trip address text views.
     * when clicked it displays a popup of address.
     * @author VE00YM023
     */
    private fun setupAddressClickListeners(){
        tv_from.setOnClickListener { showAddressPopup(TRIP_START_HEADER, tv_from.text?.toString() ?: EMPTY_STRING) }
        tv_to.setOnClickListener   { showAddressPopup(TRIP_END_ADDRESS, tv_to.text?.toString() ?: EMPTY_STRING) }
    }

    /**
     * Displays the trip address in a popup.
     * this is done to show large address which cant fit on the Trip detail screen.
     * @author VE00YM023
     */
    private fun showAddressPopup(header: String, address: String){
        showNotificationDialogWithTitle(header, address) {}
    }

    override fun setupObservers() {
        super.setupObservers()

        /* First try to load trip detail (Lats, Longs) from local DB,
          if not available then query the remote server. */
        viewModel.tripLocations.observe(this, Observer {
            if(it != null && it.isNotEmpty()){
                coordinates = it
                mapFragment.getMapAsync(this)
            }
            else { showMessage(GRAPH_ERROR) }
        })

        viewModel.graphError.observe(this, Observer {
            it.getIfNotHandled()?.let {
                showMessage(GRAPH_ERROR)
            }
        })
    }

    /**
     * changes the color of bluetooth icon in
     * the toolbar
     */
    private fun changeBTIconColor(isConnected: Boolean){
        if(isConnected){
            iv_connection_bt.setColorFilter(ContextCompat.getColor(this, R.color.selected_bike_color), android.graphics.PorterDuff.Mode.SRC_IN)
        }
        else{
            iv_connection_bt.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
        }
    }

    /**
     * Loads the map pin drawable and adjusts the size for UI.
     * @author VE00YM023
     */
    private fun loadMapPinDrawable(){
        val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_map_pin_yamaha)
        mapPinBitmap = Bitmap.createScaledBitmap(originalBitmap, 75, 80, false)
    }
}