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
 * YourBikesActivity : This activity is responsible for showing all the bikes which
 * can be connected by a particular user. User can also add new bike using FAB.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.provider.Settings
import ymsli.com.ea1h.utils.NetworkHelper
import android.view.MenuItem
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.your_bikes_activity.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.adapters.YourBikesAdapter
import ymsli.com.ea1h.adapters.YourBikesAdapter.Companion.NO_BIKE_SELECTED
import ymsli.com.ea1h.base.BaseFragment
import ymsli.com.ea1h.database.entity.BikeEntity
import ymsli.com.ea1h.di.component.FragmentComponent
import ymsli.com.ea1h.services.ECUParameters
import ymsli.com.ea1h.utils.common.Constants
import ymsli.com.ea1h.utils.common.Constants.ACTION_OK
import ymsli.com.ea1h.utils.common.Event
import ymsli.com.ea1h.views.ackdialogs.ConnectionConfirmationDialog
import ymsli.com.ea1h.views.addbike.ChassisNumberActivity
import ymsli.com.ea1h.views.home.HomeViewModel

class YourBikesFragment : BaseFragment<HomeViewModel>(),
    YourBikesAdapter.BikeSelectionListener {

    lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var bikes: Array<BikeEntity>

    private lateinit var ecuParameters: ECUParameters
    private lateinit var activeBike: BikeEntity

    override fun provideLayoutId(): Int {
        return R.layout.your_bikes_activity
    }

    override fun injectDependencies(fragmentComponent: FragmentComponent) = fragmentComponent.inject(this)

    override fun setupView(view: View) {
        linearLayoutManager = LinearLayoutManager(requireActivity())
        fab_add_bike.setOnClickListener {
            val chassisNumberActivityIntent = Intent(activity, ChassisNumberActivity::class.java)//add new bike on FAB tap
            chassisNumberActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(chassisNumberActivityIntent)
        }
        rv_bikes.adapter = YourBikesAdapter(NO_BIKE_SELECTED, ArrayList(), this)
        rv_bikes.layoutManager = linearLayoutManager
        setupListItemSwipeHandler()
        ecuParameters = ECUParameters.getEcuParametersInstance()
        setupPullToRefresh()
        setupObservers()
        showBackgroundSettingsDialogIfRequired()
    }

    /** Sets up the pull to feature to update the trip list */
    private fun setupPullToRefresh(){
        swipe_refresh_layout.setOnRefreshListener {
            linearLayoutManager = LinearLayoutManager(requireActivity())
            rv_bikes.adapter = YourBikesAdapter(NO_BIKE_SELECTED, ArrayList(), this)
            viewModel.fetchBikesFromServer()
            swipe_refresh_layout.isRefreshing = false
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.getBikes().observe(this, Observer {
            bikes = it
            val connectedBike = viewModel.getLastConnectedBikeForService()
            if (it.isEmpty()) tv_no_bike.visibility = View.VISIBLE
            else if(connectedBike != null && ecuParameters.isConnected){
                tv_no_bike.visibility = View.GONE
                loadDataWithSelectedBike(connectedBike)
            }
            else{
                tv_no_bike.visibility = View.GONE
                rv_bikes.adapter = YourBikesAdapter(NO_BIKE_SELECTED, bikes.toCollection(ArrayList()), this)
            }
        })

        /* When API Call is active show the progress bar and make the UI not touchable */
        viewModel.apiCallActive.observe(this, Observer {
            progress_bar.visibility = if(it) View.VISIBLE else View.GONE
            if(it) { requireActivity().window.setFlags(FLAG_NOT_TOUCHABLE, FLAG_NOT_TOUCHABLE) }
            else{
                requireActivity().window.clearFlags(FLAG_NOT_TOUCHABLE)
            }
        })

        viewModel.isConnectionEstablished.observe(this, Observer {
            it.getIfNotHandled()?.let {
                if(this::bikes.isInitialized){
                    showProgress(false)
                    if(it) isConnected(it)
                }
            }
        })

        viewModel.isConnectionDestablished.observe(this, Observer {
            it.getIfNotHandled()?.let {
                if(this::bikes.isInitialized && it && !ecuParameters.isConnected){
                    rv_bikes.adapter = YourBikesAdapter(NO_BIKE_SELECTED, bikes.toCollection(ArrayList()), this)
                }
            }
        })

        viewModel.messageStringIdEvent.observe(this, Observer {
            it?.getIfNotHandled()?.let {
                if(it.data!=null){
                    val message = requireContext().resources.getString(it.data)
                    Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.proceedBikeConnection.observe(this, Observer {
            it.getIfNotHandled()?.let {
                proceedBikeConnection(it)
            }
        })

        viewModel.activeBikeEntity.observe(this, Observer {
            if(it!=null) activeBike = it
        })

        viewModel.isConnectionTimeout.observe(this, Observer {
            it.getIfNotHandled()?.let {
                if(it){
                    try {
                        val explicitDisconnect: Boolean = viewModel.explicitDisconnect.value?.getIfNotHandled()?:false
                        if (!ecuParameters.isConnected) {
                            rv_bikes.adapter = YourBikesAdapter(NO_BIKE_SELECTED, bikes.toCollection(ArrayList()), this)
                            if(explicitDisconnect) {
                                viewModel.explicitDisconnect.value = Event(false)
                            }
                            showProgress(false)
                            showNotificationDialog(resources.getString(R.string.unable_to_connect), R.drawable.ic_error){
                            }
                        }
                    } catch (ex: Exception) {

                    }
                }
            }
        })

        viewModel.bikeDeletionAck.observe(this, Observer {
            it.getIfNotHandled()?.let {
                if(it){
                    //ack user that bike has been deleted
                    val message = requireActivity().resources.getString(R.string.bike_deletion_ack)
                    showNotificationDialog(message, R.drawable.ic_success){
                        //User acknowledged already
                        // no further action required
                    }
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) requireActivity().onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun loadDataWithSelectedBike(bikeEntity: BikeEntity?) {
        var position = 0
        if (this::bikes.isInitialized && bikes.isNotEmpty() && bikeEntity != null) {
            for (b in bikes) {
                if (b.bikeId == bikeEntity.bikeId) {
                    break
                }
                position++
            }
            rv_bikes.adapter = YourBikesAdapter(position, bikes.toCollection(arrayListOf()), this)
        }
        else if(this::bikes.isInitialized){
            rv_bikes.adapter = YourBikesAdapter(NO_BIKE_SELECTED, bikes.toCollection(arrayListOf()), this)
        }
        else{
            rv_bikes.adapter = YourBikesAdapter(NO_BIKE_SELECTED, arrayListOf(), this)
        }
    }

    /**
     * this is a callback method
     * which is triggered when any bike
     * is selected from the recycler view
     */
    override fun selectedBike(chassisNumber: String) {
        // confirmation dialog to connect bike
        if(this::activeBike.isInitialized && activeBike.chasNum.equals(chassisNumber,true) && ecuParameters.isConnected){
            return
        }
        requireActivity().runOnUiThread {
            showECUConnectDialog(chassisNumber)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getLastConnectedBike().observe(this, Observer {
            if (it != null && ecuParameters.isConnected) {
                loadDataWithSelectedBike(it)
            }
        })
    }

    /**
     * callback method of BTEventListener
     * when bike is connected to phone
     */
    fun isConnected(isConnected: Boolean) {
        if(!isConnected) {
            showProgress(false)
            val explicitDisconnect: Boolean = viewModel.explicitDisconnect.value?.getIfNotHandled()?:false
            rv_bikes.adapter = YourBikesAdapter(NO_BIKE_SELECTED, bikes.toCollection(ArrayList()), this)
            if(!explicitDisconnect) {
            }
            else{
                viewModel.explicitDisconnect.value = Event(false)
            }
        } else {
            if (ecuParameters.isConnected) {
                if(!isBikeConnectedDialogShown) showBikeIsConnected(resources.getString(R.string.able_to_connect))
                isBikeConnectedDialogShown = true
                val connectedBike = viewModel.getLastConnectedBikeForService()
                loadDataWithSelectedBike(connectedBike)
                showProgress(false)
            }
        }
    }

    private var isBikeConnectedDialogShown: Boolean = false

    override fun onDetach() {
        super.onDetach()
        isBikeConnectedDialogShown = false
    }

    /**
     * this function handles connection confirmation
     * of MFECU from BT icon in toolbar
     */
    private fun showECUConnectDialog(chassisNumber: String){
        // show custom dialog for connection
        val connectionConfirmationDialog = ConnectionConfirmationDialog(chassisNumber)
        val ft = childFragmentManager.beginTransaction().addToBackStack(null)
        connectionConfirmationDialog.show(ft,null)
    }

    /**
     * triggers bike connection once user
     * confirms it on the dialog box
     */
    private fun proceedBikeConnection(chassisNumber: String){
        viewModel.setLastConnectedBike(chassisNumber)
        showProgress(true)
        isBikeConnectedDialogShown = false
        val mainHandler = Handler(requireActivity().mainLooper)
        mainHandler.post(Runnable {
            val connectedBike = viewModel.getLastConnectedBikeForService()
            if(!(bikes.get(0).chasNum==chassisNumber) || (connectedBike == null)){
                ecuParameters.isIgnited = false
                ecuParameters.isConnected = false
                viewModel.showProgress.postValue(true)
            }
         })
    }

    /**
     * Displays the background settings dialog if value of dontAskAgain pref is
     * not set.
     */
    private fun showBackgroundSettingsDialogIfRequired() {
        val dontDisplayDialog = viewModel.getDontAskAgain()
        if(dontDisplayDialog) { return }

        showBackgroundSettingsDialog{type, dontAskAgain ->
            if(dontAskAgain) { viewModel.setDontAskAgain(true) }
            if(type == ACTION_OK){ startSettingsActivity() }
        }
    }

    private fun startSettingsActivity(){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", requireActivity().packageName, null)
        try { startActivityForResult(intent, 101) }
        catch (ex: java.lang.Exception){ showMessage(Constants.APP_SETTINGS_ERROR) }
    }

    /************************ FUNCTIONS FOR SWIPE TO DELETE FUNCTIONALITY ************************/

    /**
     * Configures the bikes list recycler view to provide swipe to delete functionality.
     * @author VE00YM023
     */
    private fun setupListItemSwipeHandler(){
        val bgIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)
        val deleteSwipeHandler = object : ListItemSwipeHandler(bgIcon!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val selectedBike = bikes[viewHolder.adapterPosition]
                /* If bike is connected to ECU then notify user to disconnect */
                if(selectedBike.isLastConnected && ecuParameters.isConnected){
                    reloadBikeList()
                    val message = getString(R.string.dialog_disconnect_before_delete)
                    showNotificationDialog(message, R.drawable.ic_error){}
                }
                else {
                    showDeleteConfirmationDialogAndContinue(viewHolder.adapterPosition)
                }
            }
        }
        val itemPickHelper = ItemTouchHelper(deleteSwipeHandler)
        itemPickHelper.attachToRecyclerView(rv_bikes)
    }

    /**
     * When user left swipes an item to delete, first we have to show the confirmation.
     * @author  VE00YM023
     */
    private fun showDeleteConfirmationDialogAndContinue(itemPosition: Int) {
        val message = getString(R.string.dialog_bike_delete_confirmation)
        val actionOk: () -> Unit = {
            if(NetworkHelper(requireContext()).isNetworkConnected()){
                rv_bikes.adapter = YourBikesAdapter(NO_BIKE_SELECTED, ArrayList(), this)
                val selectedBike = bikes[itemPosition]
                viewModel.removeBikeMapping(selectedBike)
            }
            else{
                showMessage(getString(R.string.network_connection_error))
                reloadBikeList()
            }
        }
        val actionCancel = { reloadBikeList() }
        val positiveLabel = getString(R.string.ACTION_OK)
        showConfirmationDialog(message, positiveLabel, actionCancel, actionOk)
    }

    private fun reloadBikeList(){
        val connectedBike = viewModel.getLastConnectedBikeForService()
        tv_no_bike.visibility = View.GONE
        if(connectedBike != null && ecuParameters.isConnected){
            loadDataWithSelectedBike(connectedBike)
        }
        else{
            rv_bikes.adapter = YourBikesAdapter(NO_BIKE_SELECTED, bikes.toCollection(ArrayList()), this)
        }
    }
}