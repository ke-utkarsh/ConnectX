package ymsli.com.ea1h.views.home

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM023)
 * @date    13/02/2020 2:00 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * HomeFragment : This is the home fragment, it implements the user's home UI.
 * it contains the main interaction buttons as well as the last parking location map.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fragment_home.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseFragment
import ymsli.com.ea1h.di.component.FragmentComponent
import ymsli.com.ea1h.services.ECUParameters
import ymsli.com.ea1h.utils.common.Event
import ymsli.com.ea1h.views.home.parkinglocation.ParkingLocationFragment
import java.lang.Exception


class HomeFragment : BaseFragment<HomeViewModel>() {

    private val parkingLocationRecordAvailable = true
    /** Following arrays are used to store the ids as well as the
     *  child views of button container, we need them when we setup the button colors*/
    private var buttonIds = intArrayOf(0)
    private val buttonComponents = mutableMapOf<Int, Array<View>>()
    private var blueColor = Color.BLUE

    private val bikeAdded = true
    private val deviceConnected = false

    override fun provideLayoutId() = R.layout.fragment_home

    override fun injectDependencies(fragmentComponent: FragmentComponent) =
        fragmentComponent.inject(this)

    override fun setupView(view: View) {
        ecuParameters = ECUParameters.getEcuParametersInstance()
        setupActionButtonColorConfiguration(view)
        setupObservers()

        button_container_row_1?.visibility = View.GONE
        if(ecuParameters.hazardActivated){
            btn_hazard?.setCardBackgroundColor(Color.RED)
        }
        else{
            btn_hazard?.setCardBackgroundColor(Color.WHITE)
        }

        changeCardBackgroud(ecuParameters.isConnected)
        if(!ecuParameters.hazardActivated){
            changeButtonColor(hazard_icon,hazard_text)

        }
        changeButtonColor(answer_back_icon,answer_back_text)
        changeButtonColor(locate_bike_icon,locate_bike_text)
        changeButtonColor(e_lock_icon,e_lock_text)

        viewModel.hazardActivateField.postValue(false)
        viewModel.hazardDeactivateField.postValue(false)

        btn_answer_back.setOnClickListener {
            //Utils.logToFile("Answer Back Clicked!!")
            viewModel.answerBackButton.postValue(Event(true))
            btn_answer_back.isClickable = false
            changeButtonColor(answer_back_icon,answer_back_text)
            val handler = Handler()
            handler.postDelayed({
                btn_answer_back.isClickable = true
            }, 1000)
        }

        btn_elock.setOnClickListener {
            changeButtonColor(e_lock_icon,e_lock_text)
            viewModel.eLockButton.postValue(Event(true))
        }

        btn_locate_bike.setOnClickListener {
            btn_locate_bike.isClickable = false
            changeButtonColor(locate_bike_icon,locate_bike_text)
            viewModel.locateBikeButton.postValue(Event(true))
            val handler = Handler()
            handler.postDelayed({
                btn_locate_bike.isClickable = true
            }, 1000)
        }

        btn_hazard.setOnClickListener {
            btn_hazard.isClickable = false
            val handler = Handler()
            handler.postDelayed({
                btn_hazard.isClickable = true
            }, 1000)
            viewModel.hazardButton.postValue(Event(true))
            changeButtonColor(hazard_icon,hazard_text)
            if(ecuParameters.isConnected && ecuParameters.hazardActivated && ecuParameters.isIgnited){
                btn_hazard?.setCardBackgroundColor(Color.WHITE)
            }
            else if(ecuParameters.isConnected && !ecuParameters.hazardActivated && ecuParameters.isIgnited){
                btn_hazard?.setCardBackgroundColor(Color.RED)
            }
            else if(ecuParameters.isConnected){
                btn_hazard?.setCardBackgroundColor(Color.WHITE)
            }
        }

        if(viewModel.isBikeAdded.value!=null && viewModel.isBikeAdded.value!!){
            first_container.visibility = View.VISIBLE
            second_container.visibility = View.VISIBLE
            button_container_row_1.visibility = View.VISIBLE
            container_no_bike.visibility = View.GONE
            loadHeaderFragment()
            loadMiddleFragment()
        }
        else{
            viewModel.getBikes().observe(viewLifecycleOwner, Observer {
                if(it.isEmpty()){
                    first_container.visibility = View.GONE
                    second_container.visibility = View.GONE
                    button_container_row_1.visibility = View.GONE
                    container_no_bike.visibility = View.VISIBLE

                    //OPEN fragment to welcome user in the app
                    val fragmentInstance = HomeNoBikeFragment.newInstance()
                    childFragmentManager.beginTransaction()
                        .replace(R.id.container_no_bike,
                            fragmentInstance, HomeNoBikeFragment.TAG)
                        .commitAllowingStateLoss()
                }
                else{
                    viewModel.isBikeAdded.value = true
                    first_container.visibility = View.VISIBLE
                    second_container.visibility = View.VISIBLE
                    button_container_row_1.visibility = View.VISIBLE
                    container_no_bike.visibility = View.GONE
                    loadHeaderFragment()
                    loadMiddleFragment()
                }
            })
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.bikeConnected.observe(this, Observer {
            //it.getIfNotHandled()?.let {
            if(it.peek()){
                //handle button colors on connectivity change
                btn_answer_back.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
                btn_locate_bike.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
                btn_elock.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
                btn_hazard.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
            }
            else{
                btn_answer_back.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.bt_command_btn_text_disabled))
                btn_locate_bike.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.bt_command_btn_text_disabled))
                btn_elock.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.bt_command_btn_text_disabled))
                btn_hazard.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.bt_command_btn_text_disabled))
            }
            //}
        })

        viewModel.iconColorChangeOnConnected.observe(this, Observer {
            it.getIfNotHandled()?.let {
                loadHeaderFragment()
                if(ecuParameters.hazardActivated){
                    ecuParameters.hazardActivated = false
                    btn_hazard.setCardBackgroundColor(Color.WHITE)
                }
                if(it || !ecuParameters.isConnected){
                    changeButtonColor(hazard_icon,hazard_text)
                    changeButtonColor(answer_back_icon,answer_back_text)
                    changeButtonColor(locate_bike_icon,locate_bike_text)
                    changeButtonColor(e_lock_icon,e_lock_text)
                }

                changeCardBackgroud(ecuParameters.isConnected)
            }
        })

        viewModel.isHazardActive.observe(this, Observer {
            it?.getIfNotHandled()?.let {
                try{
                    if(it){
                        btn_hazard.setCardBackgroundColor(Color.RED)
                        hazard_icon.setImageResource(R.drawable.icon_hazard_white)
                        hazard_icon.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
                        hazard_text.setTextColor(ContextCompat.getColor(requireActivity().applicationContext, R.color.white))
                    }
                    else{
                        btn_hazard.setCardBackgroundColor(Color.WHITE)
                        hazard_icon.setImageResource(R.drawable.ic_hazard_new)
                        hazard_icon.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext, R.color.label_trip_detail_blue), android.graphics.PorterDuff.Mode.MULTIPLY)
                        hazard_text.setTextColor(ContextCompat.getColor(requireActivity().applicationContext, R.color.bt_command_btn_text))
                    }
                }
                catch (ex: Exception){

                }
            }
        })

        viewModel.removeELockIcon.observe(this, Observer {
            if(it.peek()) btn_elock.visibility = View.GONE
            else btn_elock.visibility = View.VISIBLE
        })

        viewModel.loadBikeIcons.observe(this, Observer { if(it) { loadBikeIcons() } })
        viewModel.loadScooterIcons.observe(this, Observer { if(it) { loadScooterIcons()} })

    }

    private fun changeCardBackgroud(isConnected: Boolean){
        if(isConnected){
            btn_answer_back.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
            btn_locate_bike.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
            btn_elock.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
            if(ecuParameters.hazardActivated){
                btn_hazard.setCardBackgroundColor(Color.RED)
                hazard_icon.setImageResource(R.drawable.icon_hazard_white)
                hazard_text.setTextColor(ContextCompat.getColor(requireActivity().applicationContext, R.color.white))
            }
            else{
                btn_hazard.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
                hazard_icon.setImageResource(R.drawable.ic_hazard_new)
                hazard_text.setTextColor(ContextCompat.getColor(requireActivity().applicationContext, R.color.bt_command_btn_text))
            }
        }
        else{
            answer_back_icon.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext, R.color.bt_command_btn_text_disabled), android.graphics.PorterDuff.Mode.MULTIPLY)
            locate_bike_icon.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext, R.color.bt_command_btn_text_disabled), android.graphics.PorterDuff.Mode.MULTIPLY)
            e_lock_icon.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext, R.color.bt_command_btn_text_disabled), android.graphics.PorterDuff.Mode.MULTIPLY)
            //hazard_icon.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext, R.color.label_trip_detail_blue), android.graphics.PorterDuff.Mode.MULTIPLY)
            hazard_icon.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext, R.color.label_trip_detail_blue), android.graphics.PorterDuff.Mode.MULTIPLY)
            hazard_icon.setImageResource(R.drawable.ic_hazard_new)

            btn_hazard.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.bt_command_btn_text_disabled))
            btn_answer_back.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.bt_command_btn_text_disabled))
            btn_locate_bike.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.bt_command_btn_text_disabled))
            btn_elock.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.bt_command_btn_text_disabled))
        }
    }

    private fun setupActionButtonColorConfiguration(view: View){
        blueColor = requireContext().getColor(R.color.facebookBlue)
        initButtonIdsArray()
        setupButtonComponents()
        setupColorChangeListeners(view)
    }

    /**
     * This function initializes the array of button ids.
     * @author VE00YM023
     */
    private fun initButtonIdsArray(){//R.id.btn_driving_history,
        buttonIds = intArrayOf(R.id.btn_answer_back,
            R.id.btn_elock,
            R.id.btn_hazard, R.id.btn_locate_bike)
    }

    /**
     * This function sets up the map of card view id -> button component views.
     * it iterates over the array of all button ids and for every id it inserts
     * an entry in the map.
     *
     * currently there are two button components for every button.
     * 1. ImageView for button icon
     * 2. TextView for button text
     *
     * @author VE00YM023
     */
    private fun setupButtonComponents() {
        buttonIds.forEach { buttonComponents[it] =
            getChildViews(it) }
    }

    /**
     * Returns an array of button component views for given cardViewId.
     *
     * @param cardViewId id of card view
     * @author VE00YM023
     */
    private fun getChildViews(cardViewId: Int): Array<View>{
        var childContainer = requireActivity().findViewById<CardView>(cardViewId)!!.getChildAt(0) as ViewGroup
        return arrayOf(childContainer.getChildAt(0), childContainer.getChildAt(1))
    }

    /**
     * Sets up the touch listener to detect the different motion events.
     * based on the type of motion event we change the colors of button components.
     * @author VE00YM023
     */
    private fun setupColorChangeListeners(parent: View){
        buttonIds.forEach {
            var cardView = parent.findViewById<View>(it)
            cardView.setOnTouchListener{ v, e ->
                when(e.action){
                    MotionEvent.ACTION_UP -> changeButtonTextAndIconColor(it, Color.WHITE)
                    MotionEvent.ACTION_DOWN -> changeButtonTextAndIconColor(it, blueColor)
                    MotionEvent.ACTION_MOVE -> {
                        /** if user has dragged out of view, then set default color */
                        var rect = Rect(v.left, v.top, v.right, v.bottom)
                        if (!rect.contains(v.left + e.x.toInt(), v.top + e.y.toInt())) {
                            changeButtonTextAndIconColor(it, Color.WHITE)
                        }
                    }
                }
                false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(ecuParameters.isConnected && ecuParameters.hazardActivated){
            btn_hazard.setCardBackgroundColor(Color.RED)
            hazard_icon.setImageResource(R.drawable.icon_hazard_white)
        }
        else if(ecuParameters.isConnected && !ecuParameters.hazardActivated){
            btn_hazard.setCardBackgroundColor(Color.WHITE)
            hazard_icon.setImageResource(R.drawable.ic_hazard_new)
        }
    }

    /**
     * Opens the parking location map if parking location is available,
     * otherwise shows appropriate error message.
     *
     * @author VE00YM023
     */
    private fun parkingLocationAction() = when(parkingLocationRecordAvailable) {
        false -> showMessage(R.string.no_parking_location)
        else -> {
            val action = HomeFragmentDirections
                .actionNavHomeToParkingLocationFragment(false)
            NavHostFragment.findNavController(this).navigate(action)
        }
    }

    /**
     * This function changes the color of button text as well as the icon of button.
     * it is used to change the button color when button is clicked.
     * @param btnId id of card view
     * @param color new color
     *
     * @author VE00YM023
     */
    private fun changeButtonTextAndIconColor(btnId: Int, color: Int){
        var imageView = buttonComponents[btnId]!![0] as AppCompatImageView
        var textView = buttonComponents[btnId]!![1] as TextView
        imageView.setColorFilter(color)
        textView.setTextColor(color)
    }

    /**
     * This function loads the header fragment. it passes the argument to header fragment,
     * so that it can set up its view according to the state of application.
     *
     * @author VE00YM023
     */
    private fun loadHeaderFragment(){
        var bundle = Bundle().apply { putBoolean(HomeHeaderFragment.DEVICE_CONNECTED, deviceConnected) }
        var fragmentInstance = HomeHeaderFragment.newInstance().apply { arguments = bundle }
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_home_header,
                fragmentInstance, HomeHeaderFragment.TAG)
            .commitAllowingStateLoss()
    }

    /**
     * This function loads the appropriate fragment in the middle section of home fragment.
     * if user have not added any bike yet then we show image slide show otherwise
     * we show the last parking location record and map.
     *
     * @author VE00YM023
     */
    private fun loadMiddleFragment(){
        val ecuParameters = ECUParameters.getEcuParametersInstance()
        viewModel.getLastTripLiveData().observe(this, Observer {
            //this case handles when last trip is there
            if(it!=null && !ecuParameters.isIgnited && (viewModel.getUserId() == it.userId))
            {
                val fragInstance: Fragment = ParkingLocationFragment()
                val tag = "ParkingLocationFragment"
                childFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_home_screen, fragInstance, tag)
                    .commitAllowingStateLoss()

            }
            //below case handles when bike is added but no trip has been created so far
            else if(it==null && bikeAdded){

                //OPEN fragment to welcome user in the app
                val fragmentInstance = HomeNoTripFragment.newInstance()
                childFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_home_screen,
                        fragmentInstance, HomeNoTripFragment.TAG)
                    .commitAllowingStateLoss()
            }

            //when last trip is not empty and ignition is on.
            else if (it != null && ecuParameters.isIgnited && bikeAdded) {
                val fragInstance: Fragment = ParkingLocationFragment()
                val tag = "ParkingLocationFragment"
                childFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_home_screen, fragInstance, tag)
                    .commitAllowingStateLoss()
            }
        })
    }

    private lateinit var ecuParameters: ECUParameters

    /**
     * changes the color based on connected or disconnected
     */
    private fun changeButtonColor(iv: AppCompatImageView,tv: TextView){
        if(ecuParameters.isConnected){
            iv.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext, R.color.callBlueTextColorHint), android.graphics.PorterDuff.Mode.MULTIPLY)
            tv.setTextColor(ContextCompat.getColor(requireActivity().applicationContext, R.color.bt_command_btn_text))
        }
        else{
            tv.setTextColor(ContextCompat.getColor(requireActivity().applicationContext, R.color.bt_command_btn_text))
            iv.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext, R.color.bt_command_btn_text), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.hazardActivateField.postValue(false)
        viewModel.hazardDeactivateField.postValue(false)
    }

    private fun loadScooterIcons(){
        locate_bike_icon.setImageResource(R.drawable.ic_locate_scooter)
        answer_back_icon.setImageResource(R.drawable.ic_answer_back_scooter)
    }

    private fun loadBikeIcons(){
        locate_bike_icon.setImageResource(R.drawable.ic_locate_my_bike_new)
        answer_back_icon.setImageResource(R.drawable.ic_answer_back_new)
    }
}