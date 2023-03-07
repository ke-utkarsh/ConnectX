package ymsli.com.ea1h.views.home

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM023)
 * @date    18/02/2020 9:50 AM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * HomeHeaderFragment : This fragment is used to provide the header UI in the home
 * fragment. it should contain all the logic associated with the header, such as
 * animating the header and setting listeners on header if any.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.shape.CornerFamily
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home_header_double.*
import okhttp3.OkHttpClient
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseFragment
import ymsli.com.ea1h.di.component.FragmentComponent
import ymsli.com.ea1h.services.ECUParameters
import ymsli.com.ea1h.utils.common.Constants
import ymsli.com.ea1h.utils.common.Utils
import ymsli.com.ea1h.utils.common.ViewUtils
import java.util.*
import java.util.concurrent.TimeUnit

class HomeHeaderFragment : BaseFragment<HomeViewModel>(){

    /** Fragment object creation factory. */
    companion object {
        private const val HEADER_CORNER_RADIUS_DP = 0f
        const val TAG = "HomeHeaderFragment"
        const val DEVICE_CONNECTED = "deviceConnected"
        fun newInstance(): HomeHeaderFragment {
            val args = Bundle()
            val fragment =
                HomeHeaderFragment()
            fragment.arguments = args
            return fragment
        }
    }

    /** This argument is used by the fragment to decide which layout to load */
    private var deviceConnected = false
    private lateinit var ecuParameters: ECUParameters

    /**
     * Depending on the status of device connection, load the appropriate layout.
     * if no device is connected then we show a single header (I Love Yamaha),
     * otherwise we divide header in two parts(I Love Yamaha and device info)
     * @author VE00YM023
     */
    override fun provideLayoutId(): Int {
        ecuParameters = ECUParameters.getEcuParametersInstance()
        deviceConnected = ecuParameters.isConnected
        return when{
            deviceConnected -> R.layout.fragment_home_header_double
            else -> R.layout.fragment_home_header_single
        }
    }

    override fun injectDependencies(fragmentComponent: FragmentComponent) =
        fragmentComponent.inject(this)

    override fun setupView(view: View) {

    }

    override fun onStart() {
        super.onStart()
        viewModel.getHeaderImageURL()
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.headerImageURL.observe(this, Observer {
            if(!it.isNullOrEmpty()){
                val cornerRadius = ViewUtils.dpToFloat(requireContext(), HEADER_CORNER_RADIUS_DP)
                setHeaderImageCorners(cornerRadius)
                imageHomeHeader.visibility = View.VISIBLE
                if(viewModel.getIfScooter()){
                    Picasso.get().load(viewModel.getMiscDataByKey(Constants.SCOOTER_BG_KEY).get(0).descriptionValue).fit().placeholder(R.drawable.scooter_header).into(imageHomeHeader)
                }
                else{
                    Picasso.get().load(viewModel.getMiscDataByKey(Constants.BIKE_DP_ANDROID_KEY).get(0).descriptionValue).fit().placeholder(R.drawable.bike_header).into(imageHomeHeader)
                }
            }
        })

        viewModel.batteryVoltage.observe(this, Observer {
            it.getIfNotHandled()?.let {
                if(!it.isNullOrBlank()){
                    tv_battery_value?.setText(Utils.getBatteryVoltageFormatted(it))
                }
            }
        })

        viewModel.brakeCount.observe(this, Observer {
            tv_brake_value?.setText(it.toString()?:"0")
        })

        viewModel.activeBikeEntity.observe(this, Observer {
            if(it!=null && deviceConnected){
                val bikeInfo = if(it.bkModNm == null || it.mod == null) { Constants.NA } else { "${it.bkModNm} ${it.mod}" }
                tv_home_header_bike_name?.text = bikeInfo
                tv_home_header_bike_no?.text = viewModel.decryptData(it.regNum ?: Constants.NA)
                tv_home_header_current_time?.text = Utils.getTimeInDateTimeFormat(Calendar.getInstance().timeInMillis)
            }
        })

        viewModel.setScooterImage.observe(this, Observer {
            it.getIfNotHandled()?.let {
                if(it){
                    Picasso.get().load(viewModel.getMiscDataByKey(Constants.SCOOTER_BG_KEY).get(0).descriptionValue).fit().placeholder(R.drawable.bike_header).into(imageHomeHeader)
                }
                else{
                    Picasso.get().load(viewModel.getMiscDataByKey(Constants.BIKE_DP_ANDROID_KEY).get(0).descriptionValue).fit().placeholder(R.drawable.bike_header).into(imageHomeHeader)
                }
            }
        })
    }

    /**
     * Sets the corners of header ImageView.
     * if bike is not connected then all four corners are rounded
     * otherwise only left corners are rounded.
     * @param radius to be applied on different corners of image
     * @author VE00YM023
     */
    @SuppressLint("UnsafeExperimentalUsageError")
    private fun setHeaderImageCorners(radius: Float) = if(deviceConnected){
        imageHomeHeader.shapeAppearanceModel = imageHomeHeader.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, radius)
            .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
            .build()
        }
        else{
        imageHomeHeader.shapeAppearanceModel = imageHomeHeader.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(CornerFamily.ROUNDED, radius)
            .setTopLeftCorner(CornerFamily.ROUNDED, radius)
            .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
            .setBottomRightCorner(CornerFamily.ROUNDED, radius)
            .build()
    }
/*    private lateinit var mPreferences: SharedPreferences
    private val sharedPrefFile = Constants.SHARED_PREF_REALTIME
    private var editor: SharedPreferences.Editor? = null*/
}