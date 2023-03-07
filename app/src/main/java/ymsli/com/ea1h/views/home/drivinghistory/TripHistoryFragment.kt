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
 * TripHistoryActivity : This activity shows the trips by user in the
 * form of recycler view.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.trip_history_activity.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.adapters.TripThisWeekAdapter
import ymsli.com.ea1h.base.BaseFragment
import ymsli.com.ea1h.database.entity.TripEntity
import ymsli.com.ea1h.di.component.FragmentComponent
import ymsli.com.ea1h.views.home.HomeViewModel
import ymsli.com.ea1h.views.home.drivinghistory.filter.TripFilterDialogFragment
import javax.inject.Inject

class TripHistoryFragment: BaseFragment<HomeViewModel>(){

    private lateinit var localTrips: Array<TripEntity>
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var tripThisWeekAdapter: TripThisWeekAdapter

    override fun provideLayoutId(): Int = R.layout.trip_history_activity
    override fun injectDependencies(ac: FragmentComponent) = ac.inject(this)

    override fun setupView(view: View) {
        linearLayoutManager = LinearLayoutManager(requireActivity())
        tripThisWeekAdapter = TripThisWeekAdapter(this.lifecycle, ArrayList())

        viewModel.getBikeEntityMap()
        tv_no_trips.visibility = View.GONE
        tripThisWeekAdapter.bikeEntityMap = viewModel.bikeEntityMap
        setupRecyclerView()
        viewModel.getVehicleListForFilter()
        setupPullToRefresh()
    }

    /** Sets up the pull to feature to update the trip list */
    private fun setupPullToRefresh(){
        swipe_refresh_layout.setOnRefreshListener {
            viewModel.fetchTripsFromServer()
            swipe_refresh_layout.isRefreshing = false
        }
    }

    /** Sets up the recycler view with layout animation and adapter */
    private fun setupRecyclerView(){
        val animController = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down)
        recycler_view_trip_this_week.layoutManager = linearLayoutManager
        recycler_view_trip_this_week.adapter = tripThisWeekAdapter
        recycler_view_trip_this_week.layoutAnimation = animController
    }

    /** Setup the LiveData observers */
    override fun setupObservers() {
        super.setupObservers()

        viewModel.activateFilter.observe(this, Observer {
            if(it) {
                viewModel.activateFilter.value = false
                openFilterDialogFragment()
            }
        })
        /* This value changes when 'Apply' button of FilterDialog is clicked */
        viewModel.applyFilter.observe(this, Observer {
            if(this::localTrips.isInitialized && !localTrips.isNullOrEmpty()) {
                tripThisWeekAdapter.clearData()
                /* Filter the list if predicate is set */
                if(it.isSet()){
                    val filteredList = viewModel.filterTrips(localTrips.toCollection(ArrayList()), it)
                    if(filteredList.isNotEmpty()) {
                        tv_no_trips.visibility = View.GONE
                        tripThisWeekAdapter.loadData(filteredList.toCollection(ArrayList()))
                    }
                    else tv_no_trips.visibility = View.VISIBLE
//                    iv_filter.setColorFilter(COLOR_FILTER_HIGHLIGHT, android.graphics.PorterDuff.Mode.SRC_IN)
                }
                /* Reset the filter if predicate is not set */
                else {
                    tv_no_trips.visibility = View.GONE
                    tripThisWeekAdapter.loadData(localTrips.toCollection(ArrayList()))
//                    iv_filter.setColorFilter(COLOR_FILTER, android.graphics.PorterDuff.Mode.SRC_IN)
                }
            }
        })

        /* Observe the trips LiveData object and update the UI */
        viewModel.getLastTripsOfUser().observe(this, Observer {
            if(!it.isNullOrEmpty()){
                tripThisWeekAdapter.clearData()
                localTrips = it
                tripThisWeekAdapter.loadData(it.toCollection(ArrayList()))
                tv_no_trips.visibility = View.GONE
                viewModel.applyFilter.value = viewModel.filterState
            }
            else {
                viewModel.filterState.resetToDefault()
                tv_no_trips.visibility = View.VISIBLE
            }
        })

        /* When API Call is active show the progress bar and make the UI not touchable */
        viewModel.apiCallActive.observe(this, Observer {
            progress_bar.visibility = if(it) View.VISIBLE else View.GONE
            if(it) { activity?.window?.setFlags(FLAG_NOT_TOUCHABLE, FLAG_NOT_TOUCHABLE) }
            else{
                activity?.window?.clearFlags(FLAG_NOT_TOUCHABLE)
            }
        })
    }

    private companion object {
        private const val TITLE = "DRIVING HISTORY"
        private const val FILTER_TAG = "Filter"
        private val COLOR_FILTER_HIGHLIGHT = Color.parseColor("#0392FA")
        private val COLOR_FILTER = Color.parseColor("#FFFFFF")
        private const val BUNDLE_KEY_NOT_ALERT_DIALOG = "notAlertDialog"
        private const val TAG_DIALOG = "dialog"
    }

    /** opens filter dialog upon tap of filter icon on toolbar */
    private fun openFilterDialogFragment(){
        val dialogFragment = TripFilterDialogFragment(viewModel)
        val bundle = Bundle();
        bundle.putBoolean(BUNDLE_KEY_NOT_ALERT_DIALOG, true)
        dialogFragment.arguments = bundle
        val ft = childFragmentManager.beginTransaction()
        val prev = childFragmentManager.findFragmentByTag(TAG_DIALOG)
        if (prev != null) { ft.remove(prev) }
        ft.addToBackStack(null)
        dialogFragment.show(ft, TAG_DIALOG)
    }
}