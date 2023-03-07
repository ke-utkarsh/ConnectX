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
 * HomeActivity : This is the home activity where user lands once login in is done
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.*
import android.bluetooth.le.*
import android.content.*
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseActivity
import ymsli.com.ea1h.database.entity.BTCommandsLogEntity
import ymsli.com.ea1h.database.entity.BikeEntity
import ymsli.com.ea1h.di.component.ActivityComponent
import ymsli.com.ea1h.services.ECUParameters
import ymsli.com.ea1h.utils.common.Constants
import ymsli.com.ea1h.utils.common.Event
import ymsli.com.ea1h.utils.common.Utils
import ymsli.com.ea1h.utils.common.ViewUtils
import ymsli.com.ea1h.views.ackdialogs.*
import ymsli.com.ea1h.views.experiment.ForegroundOnlyLocationService
import ymsli.com.ea1h.views.experiment.SharedPreferenceUtil
import java.util.*


class HomeActivity: BaseActivity<HomeViewModel>(),
    ConnectionConfirmationDialog.ConnectionProceedListerner,
    DisconnectionConfirmationDialog.DisconnectionListener,
    LocationPermissionDeniedDailog.LocationPermissionDeniedListener,
    GenericAckDialog.OKClickListener, BLEInfoDialog.BTPermissionRefused {

    companion object {
        private val TAG = HomeActivity::class.java.simpleName
        private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
        private const val ANSWER_BACK_COMMAND_ID = 1
        private const val SOFTWARE_VER_COMMAND_ID = 10
        private const val LOCATE_BIKE_COMMAND_ID = 4
        private const val DISCONNET_MFECU_COMMAND_ID = 9
        const val ELOCK_COMMAND_ENABLE_ID = 6
        const val ELOCK_SOFTWARE_THRESHOLD = 1.29
        private const val ELOCK_STATUS_COMMAND_ID = 8
        const val REQUEST_ENABLE_BT = 109
    }

    /** Fields for the navigation controller and its dependencies */
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private var mIsDeviceConnected = false
    //foreground service parameters
    private var foregroundOnlyLocationService: ForegroundOnlyLocationService? = null
    private var foregroundOnlyLocationServiceBound = false
    private var foregroundAndBackgroundLocationEnabled = false

    //shared pref parameter for foreground location service
    private lateinit var sharedPreferences: SharedPreferences

    //will fetch address based on location
    private lateinit var geoCoder: Geocoder

    private var disableELock = false
    private var userAckSet: HashSet<String> = HashSet()

    var mBluetoothGatt: BluetoothGatt? = null
    private var isConnectedToECU: Boolean = false
    private lateinit var activeBike: BikeEntity

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    //value 0 denotes app enquiry of elock state
    //value 1 denotes user enquiry of elock state
    private var eLockTrigger: Int = 0

    //private lateinit var firebaseAnalytics: FirebaseAnalytics
    // Monitors connection to the while-in-use service.
    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundOnlyLocationService.LocalBinder
            foregroundOnlyLocationService = binder.service
            foregroundOnlyLocationServiceBound = true
            startForeService()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundOnlyLocationService = null
            foregroundOnlyLocationServiceBound = false
        }
    }

    override fun provideLayoutId(): Int = R.layout.activity_home

    override fun injectDependencies(activityComponent: ActivityComponent) = activityComponent.inject(this)

    private val mPairingRequestBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                when(state){
                    BluetoothAdapter.STATE_OFF -> {
                        ecuParameters.isConnected = false
                        ////Utils.writeToFile("--------------Bluetooth turned off : "+ Timestamp(System.currentTimeMillis()) +" ---------------------- ", application);
                    }

                    BluetoothAdapter.STATE_TURNING_OFF -> {
                        Log.d("BT","Turning off")
                        //("--------------Bluetooth turning off : "+ Timestamp(System.currentTimeMillis()) +" ---------------------- ", application);
                        runOnUiThread {
                            handleDisconnectUI()
                        }
                    }

                    BluetoothAdapter.STATE_ON -> {
                        ////Utils.writeToFile("--------------Bluetooth turned on : "+ Timestamp(System.currentTimeMillis()) +" ---------------------- ", application);
                    }

                    BluetoothAdapter.STATE_TURNING_ON -> {
                        ////Utils.writeToFile("--------------Bluetooth turning on : "+ Timestamp(System.currentTimeMillis()) +" ---------------------- ", application);
                    }
                }
            }
        }
    }

    override fun setupView(savedInstanceState: Bundle?) {
        window?.statusBarColor = getColor(R.color.bg_status_bar)
        viewModel.getUserProfileDetails()
        viewModel.getAllBluetoothCommands()

        ecuParameters =
            ECUParameters.getEcuParametersInstance()
        setupToolbarAndNavController()

        geoCoder = Geocoder(this, Locale.getDefault())
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        viewModel.getLastConnectedBike().observe(this, Observer {
            // connect using BLEScanDevice class
            if(it!=null){
                viewModel.setBikeBluetoothAddress(it.btAdd.toString().toUpperCase(Locale.ROOT))
                activeBike = it
                viewModel.activeBikeEntity.postValue(activeBike)

                /*Log.e("Observer Log","Observable Called")*/

                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                triggerConnection()
                startService(Intent(applicationContext, ForegroundOnlyLocationService::class.java))
                triggerService()

                val vehicleType = activeBike.vehType
                if(!vehicleType.equals(Constants.BIKE_STRING,true)){
                    // set image of scooter
                    setScooterUI()
                }
                else{
                    // set image of bike
                    setBikeUI()

                }
            }
            else{
                if(viewModel.getIfScooter()){
                    // set image of scooter
                    setScooterUI()
                }
                else{
                    // set image of bike
                    setBikeUI()
                }
            }
        })

        //Show progress for 3 seconds
    /*    showProgress(true)
        val handler = Handler()
        handler.postDelayed({
            showProgress(false)
        }, 3000)
*/
        registerReceiver(mPairingRequestBroadcastReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }

    private var mAlertDialog: SpotsDialog? = null

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val handler = Handler()
        handler.postDelayed({
            showProgress(false)
        }, 3000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_ENABLE_BT -> {
                if(resultCode == Activity.RESULT_OK){
                    //start scanning BT Device
                    scanLeDevice()
                }
                else{
                    // ask user to enable BT
                    val connectionConfirmationDialog = BLEInfoDialog(viewModel)
                    val ft = supportFragmentManager.beginTransaction().addToBackStack(null)
                    connectionConfirmationDialog.show(ft,null)
                    val handler = Handler()
                    handler.postDelayed({
                        showProgress(false)
                    }, 3000)
                }
            }
        }
        if(resultCode == Activity.RESULT_OK){
            val handler = Handler()
            handler.postDelayed({
                showProgress(false)
            }, 3000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mPairingRequestBroadcastReceiver)
    }



    override fun onStart() {
        super.onStart()
        getLocationPermission(this)
      //  checkBluetoothPermission()
       /* val currentapiVersion = Build.VERSION.SDK_INT
        if (currentapiVersion >= Build.VERSION_CODES.S) {
            // Do something for lollipop and above versions
            checkBluetoothPermission()
        } else {
            checkBluetoothPermissionFor11();
        }*/

        add_bike_iv.setOnClickListener {
            navController.navigate(R.id.nav_your_vehicles)
        }

        connection_bt_iv.setOnClickListener {
            //This will disconnect the bike from device upon BT Icon tap
            if(ecuParameters.isConnected){
                viewModel.disconnectECUDialog.postValue(Event(true))
            }
        }

        iv_info.setOnClickListener {
            //this will show user the info about BLE connection
            viewModel.showBLEInfoDialog.postValue(Event(true))
        }

        iv_filter.setOnClickListener { viewModel.activateFilter.postValue(true) }
    }

    /**
     * This function sets up the toolbar and the navigation controller
     * for base activity.
     * @author VE00YM023
     */
    private fun setupToolbarAndNavController() {
        setSupportActionBar(toolbar)
        nav_view.itemIconTintList = null
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home, R.id.nav_driving_history,
            R.id.nav_inner_parking_location, R.id.nav_your_vehicles,
            R.id.nav_user_profile, R.id.nav_about_us, R.id.nav_privacy_policy), drawer_layout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
        setupDestinationChangeListener()
        setupNavigationViewItemsSelectedListener()
        setupHeaderClickListener()
        setDrawerHeaderText()
    }

    /**
     * Sets up the Destination Change Listener for the navController,
     * we have to update the UI depending on the selected Nav item.
     * @author VE00YM023
     */
    private fun setupDestinationChangeListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            setDrawerHeaderText()
            if(destination.label!=null && destination.label!!.equals(resources.getString(R.string.menu_home))){
                add_bike_iv.visibility = View.VISIBLE
            }
            else{
                add_bike_iv.visibility = View.GONE
            }
            tv_toolbar_lavel.text = destination.label
            if (destination.id == R.id.nav_home) //hideKeyboard()
                add_bike_iv.isVisible =
                    destination.id == R.id.nav_home
            connection_bt_iv.isVisible =
                destination.id != R.id.nav_user_profile
            iv_filter.isVisible = destination.id == R.id.nav_driving_history
            if(destination.id == R.id.nav_driving_history) {
                viewModel.filterState.resetToDefault()
                viewModel.activateFilter.postValue(false)
                connection_bt_iv.isVisible = false
            }
        }
    }

    /**
     * Set the navigation item selected listener, in order to intercept
     * the logout operation. because when user chooses logout option
     * we just show an confirmation dialog.
     *
     * @author VE00YM023
     */
    private fun setupNavigationViewItemsSelectedListener() {
        nav_view.setNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_logout -> {
                    drawer_layout.closeDrawer(nav_view)
                    logout()
                    true
                }
                R.id.nav_change_password -> {
                    drawer_layout.closeDrawer(nav_view)
                    showChangePasswordDialog()
                    true
                }
                else -> {
                    val handled = NavigationUI.onNavDestinationSelected(item, navController)
                    if (handled) drawer_layout.closeDrawer(nav_view)
                    handled
                }
            }
        }
    }

    /**
     * This function is used to perform the logout operation.
     * when user clicks the logout navigation item, user is shown an alert
     * and if user accepts to continue then we logout the user by performing
     * the required operations.
     * @author VE00YM023
     */
    private fun logout() {
        val message = getString(R.string.message_logout)
        val labelLogout = getString(R.string.label_logout)
        val actionCancel = {}
        val actionOk = {
            viewModel.setBikeBluetoothAddress(null)
            proceedDisconnection()
            performLogout()
        }
        showConfirmationDialog(message, labelLogout, actionCancel, actionOk)
    }

    /**
     * Setup the header click listener to open the user profile fragment.
     * @author VE00YM023
     */
    private fun setupHeaderClickListener() {
        val header = nav_view.getHeaderView(0)
        header.container_edit.setOnClickListener {
            drawer_layout.closeDrawer(nav_view)
            navController.navigate(R.id.nav_user_profile)
        }
    }

    /**
     * puts driver name and contact in the
     * header of the navigation drawer
     */
    private fun setDrawerHeaderText() {
        val header = nav_view.getHeaderView(0)
        val userProfileDetail = viewModel.getUserProfileDetail()
        if(userProfileDetail.profilePic.isNullOrEmpty()){
            header.img_nav_header.setImageResource(R.drawable.placeholder_image_rider)
        }
        else { viewModel.parseBase64String(userProfileDetail.profilePic) }
        header.nav_header_user_name.text = userProfileDetail.fullName ?: getString(R.string.place_holder_na)
        header.nav_header_user_email.text = viewModel.decryptData(userProfileDetail.email?:"")
    }

    override fun setupObservers() {
        super.setupObservers()

        /* Observe the response of change password API call,
           and notify user with appropriate dialog. */
        viewModel.changePasswordResponse.observe(this, Observer {
            if(it == Constants.SUCCESS) {
                showNotificationDialog(getString(R.string.email_check), R.drawable.ic_success){}
            }
            else { showNotificationDialog(it, R.drawable.ic_error){} }
        })

        viewModel.showProgress.observe(this, Observer { showProgress(it) })

        viewModel.profilePicBitmap.observe(this, Observer {
            val header = nav_view.getHeaderView(0)
            val drawable = RoundedBitmapDrawableFactory.create(resources, it)
            drawable.setAntiAlias(true)
            header.img_nav_header.setImageDrawable(drawable)
            viewModel.getLastTripLiveData()
        })

        viewModel.lastConnectedBikeRemoved.observe(this, Observer {
            GlobalScope.launch(Dispatchers.IO){
                viewModel.disconnectMFECUTrigger.postValue(Event(true))
            }
        })

        viewModel.answerBackButton.observe(this, Observer {
            it.getIfNotHandled()?.let {
                if (it) {
                    if (!ecuParameters.isConnected) {
                        showErrorsViaSnackbar(R.string.ecu_disconnected)
                    }
                    else if(ecuParameters.hazardActivated){
                        showErrorsViaSnackbar(R.string.hazard_already_active)
                    }
                    else if(ecuParameters.isIgnited){
                        showErrorsViaSnackbar(R.string.ignition_on)
                    }
                    else {
                        GlobalScope.launch(Dispatchers.IO){
                            val answerBackCommand = viewModel.getSpecificCommand(ANSWER_BACK_COMMAND_ID)
                            var cmd = ByteArray(8)
                            cmd = ViewUtils.prepareMFECUCommandNonEncrypted(answerBackCommand ?: "65, 2, 0, 0, 0, 0, 0, 0")
                            writeToBLEDevice(cmd)
                        }
                        showCommandProgress(true, ANSWER_BACK_DIALOG)
                        val handler = Handler()
                        runOnUiThread {
                            handler.postDelayed({
                                showCommandProgress(false, ANSWER_BACK_DIALOG)
                            }, 2000)
                        }
                        // log command in room
                        val btCommandsLogEntity = BTCommandsLogEntity(cmdType = ANSWER_BACK_COMMAND_ID,chassNum = viewModel.activeBikeEntity.value?.chasNum,
                            triggeredOn = Utils.getTimeInMilliSec(), isSuccessful = true,isCommit = false,btAdd = viewModel.getBikeBluetoothAddress()?:Constants.NA)
                        viewModel.logBTCommandInRoom(btCommandsLogEntity)
                    }
                }
            }
        })

        viewModel.eLockButton.observe(this, Observer {
            it.getIfNotHandled()?.let{
                if(it) {
                    navController.navigate(R.id.nav_elock)
                    getELockEnableDisableCheck()
                }}
        })

        viewModel.setELockPattern.observe(this, Observer {
            it.getIfNotHandled()?.let {
                //sends MFECU command to set ELock pattern
                if(it.isNotEmpty()){
                    showCommandProgress(true, ELOCK_DIALOG)
                    disableELock = false
                    //send ELock code to MFECU
                    var cmd = ByteArray(8)
                    cmd = ViewUtils.prepareELockPatterSetCommand(it)
                    writeToBLEDevice(cmd)
                    //Log Elock commands
                    val btCommandsLogEntity = BTCommandsLogEntity(
                        cmdType = ELOCK_COMMAND_ENABLE_ID,
                        chassNum = viewModel.activeBikeEntity.value?.chasNum,
                        triggeredOn = Utils.getTimeInMilliSec(),
                        isSuccessful = true, isCommit = false,
                        btAdd = viewModel.getBikeBluetoothAddress()?:Constants.NA
                    )
                    viewModel.logBTCommandInRoom(btCommandsLogEntity)
                }
            }
        })

        viewModel.getELockPattern.observe(this, Observer {
            val disableElockCommand = Constants.ELOCK_PATTERN_READ_COMMAND
            //send ELock read command to MFECU
            var cmd = ByteArray(8)
            cmd = ViewUtils.prepareMFECUCommandNonEncrypted(disableElockCommand)
            writeToBLEDevice(cmd)
        })

        viewModel.hazardButton.observe(this, Observer {
            it.getIfNotHandled()?.let {
                if (!isConnectedToECU) {
                    showErrorsViaSnackbar(R.string.ecu_disconnected)
                }
                else if(!ecuParameters.isIgnited){
                    showErrorsViaSnackbar(R.string.ignition_off)
                }
                else {
                    val activateHazardCommand: String
                    if(!ecuParameters.hazardActivated){
                        activateHazardCommand = viewModel.getSpecificCommand(2)?:"76, 1, 0, 0, 0, 0, 0, 0"
                        // log command in room
                        val btCommandsLogEntity = BTCommandsLogEntity(cmdType = 2,chassNum = viewModel.activeBikeEntity.value?.chasNum,
                            triggeredOn = Utils.getTimeInMilliSec(), isSuccessful = false,isCommit = false,btAdd = viewModel.getBikeBluetoothAddress()?:Constants.NA)
                        viewModel.logBTCommandInRoom(btCommandsLogEntity)
                    }
                    else {
                        activateHazardCommand = viewModel.getSpecificCommand(3)?:"76, 0, 0, 0, 0, 0, 0, 0"
                        // log command in room
                        val btCommandsLogEntity = BTCommandsLogEntity(cmdType = 3,chassNum = viewModel.activeBikeEntity.value?.chasNum,
                            triggeredOn = Utils.getTimeInMilliSec(), isSuccessful = false,isCommit = false,btAdd = viewModel.getBikeBluetoothAddress()?:Constants.NA)
                        viewModel.logBTCommandInRoom(btCommandsLogEntity)
                    }
                    var cmd = ByteArray(8)
                    cmd = ViewUtils.prepareMFECUCommandNonEncrypted(activateHazardCommand)
                    writeToBLEDevice(cmd)
                }
            }
        })

        viewModel.locateBikeButton.observe(this, Observer {
            it.getIfNotHandled()?.let{
                if(it) {
                    if (!ecuParameters.isConnected) {
                        showErrorsViaSnackbar(R.string.ecu_disconnected)
                    }
                    else if(ecuParameters.hazardActivated){
                        showErrorsViaSnackbar(R.string.hazard_already_active)
                    }
                    else if(ecuParameters.isIgnited){
                        showErrorsViaSnackbar(R.string.ignition_on)
                    }
                    else{
                        val locateBikeCommand = viewModel.getSpecificCommand(LOCATE_BIKE_COMMAND_ID)
                        var cmd = ByteArray(8)
                        cmd = ViewUtils.prepareMFECUCommandNonEncrypted(locateBikeCommand?:"66, 0, 0, 0, 0, 0, 0, 0")
                        showCommandProgress(true, LOCATE_BIKE_DIALOG)
                        writeToBLEDevice(cmd)
                        //Utils.writeToFile("***********LOCATE MY VEHICLE IN PROGRESS*********** "+ Timestamp(System.currentTimeMillis())+" ${Arrays.toString(cmd)}", application);
                        val handler = Handler()
                        runOnUiThread {
                            handler.postDelayed({
                                showCommandProgress(false, LOCATE_BIKE_DIALOG)
                            }, 10500)
                        }
                        // log command in room
                        val btCommandsLogEntity = BTCommandsLogEntity(cmdType = LOCATE_BIKE_COMMAND_ID,chassNum = viewModel.activeBikeEntity.value?.chasNum,
                            triggeredOn = Utils.getTimeInMilliSec(), isSuccessful = false,isCommit = false,btAdd = viewModel.getBikeBluetoothAddress()?:Constants.NA)
                        viewModel.logBTCommandInRoom(btCommandsLogEntity)
                    }
                }}
        })

        viewModel.parkingRecordButton.observe(this, Observer {
            it.getIfNotHandled()?.let{
                if(it) {
                    parkingLocationAction()
                }}
        })

        viewModel.drivingHistoryButton.observe(this, Observer {
            it.getIfNotHandled()?.let{
                if(it) { navController.navigate(R.id.nav_driving_history) }
            }
        })

        viewModel.getLastTripLiveData().observe(this, Observer {
            if(it!=null){
                parkingLocationRecordAvailable = true
            }
        })

        viewModel.dialogPositiveButtonAction.observe(this, Observer {
            // ask for permission again
            runOnUiThread {
                getLocationPermission(this)
            }
        })

        viewModel.showBackgroundSettingsDialog.observe(this, Observer {
            it.getIfNotHandled()?.let {
                val msg = viewModel.getContentValue(28)
                openPowerSettings(msg)
            }
        })

        viewModel.eLockWritten.observe(this, Observer {
            it.getIfNotHandled()?.let {
                // show ack dialog for ELock pattern success set
                showCommandProgress(false, ELOCK_DIALOG)
                SystemClock.sleep(500)
                if(viewModel.elockSettingState.value == 0) {//ELock disabled
                    viewModel.setELockEnabledStatus(false)
                    val elockAckDialog = ELockAckDialog(resources.getString(R.string.elock_disabled_message))
                    val ft = supportFragmentManager.beginTransaction().addToBackStack(null)
                    elockAckDialog.show(ft,null)
                }
                if(viewModel.elockSettingState.value == 1) {//ELock enabled
                    viewModel.setELockEnabledStatus(true)
                    val elockAckDialog = ELockAckDialog(resources.getString(R.string.elock_enabled_message))
                    val ft = supportFragmentManager.beginTransaction().addToBackStack(null)
                    elockAckDialog.show(ft,null)
                }
                if(viewModel.elockSettingState.value == 2) {// pattern changed
                    viewModel.setELockEnabledStatus(true)
                    val ackDialog = ELockSetAckDialog()
                    val ft = supportFragmentManager.beginTransaction().addToBackStack(null)
                    ackDialog.show(ft, null)
                }
            }
        })

        viewModel.enableELock.observe(this, Observer {
            it.getIfNotHandled()?.let {
                val eLockEnableDisableCmd: String
                if(it){
                    eLockEnableDisableCmd = viewModel.getSpecificCommand(ELOCK_COMMAND_ENABLE_ID)?:"67, 69, 0, 0, 0, 0, 0, 0"
                }
                else{
                    eLockEnableDisableCmd = viewModel.getSpecificCommand(7)?:"67, 69, 1, 0, 0, 0, 0, 0"
                }
                var cmd = ByteArray(8)
                cmd = ViewUtils.prepareMFECUCommandNonEncrypted(eLockEnableDisableCmd)
                Handler(Looper.getMainLooper()).postDelayed({
                    writeToBLEDevice(cmd)
                }, 700)
            }
        })
        viewModel.eLockStatus.observe(this, Observer {
            it.getIfNotHandled()?.let {
                if(it){
                    if (!ecuParameters.isConnected) {
                        showErrorsViaSnackbar(R.string.ecu_disconnected)
                    }
                    else{
                        eLockTrigger = 1
                        val elockStatusCommand = viewModel.getSpecificCommand(ELOCK_STATUS_COMMAND_ID)?:"68, 69, 0, 0, 0, 0, 0, 0"

                        var cmd = ByteArray(8)
                        cmd = ViewUtils.prepareMFECUCommandNonEncrypted(elockStatusCommand)
                        writeToBLEDevice(cmd)
                    }
                }
            }
        })


        viewModel.initialELockStatus.observe(this, Observer {
            it.getIfNotHandled()?.let {
                if(it){
                    if (ecuParameters.isConnected) {
                        eLockTrigger = 0
                        val elockStatusCommand = viewModel.getSpecificCommand(ELOCK_STATUS_COMMAND_ID)?:"68, 69, 0, 0, 0, 0, 0, 0"
                        var cmd = ByteArray(8)
                        cmd = ViewUtils.prepareMFECUCommandNonEncrypted(elockStatusCommand)
                        writeToBLEDevice(cmd)
                    }
                }
            }
        })

        viewModel.setScooterUI.observe(this, Observer {
            it.getIfNotHandled()?.let {
                viewModel.setIfScooter(it)
                if(it){
                    setScooterUI()
                }
                else{
                    setBikeUI()
                }
            }
        })
    }

    private var parkingLocationRecordAvailable = false

    /**
     * Opens the parking location map if parking location is available,
     * otherwise shows appropriate error message.
     *
     * @author VE00YM023
     */
    private fun parkingLocationAction() = when(parkingLocationRecordAvailable) {
        false -> showNotificationDialog(getString(R.string.label_no_parking_location), R.drawable.ic_error){}
        else -> {
            val action = HomeFragmentDirections
                .actionNavHomeToParkingLocationFragment(false)
            navController.navigate(action)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        setDrawerHeaderText()
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     * handles event related to ignition on
     */
    private fun ignitionOnHandler(){
        val enabled = sharedPreferences.getBoolean(
            SharedPreferenceUtil.KEY_FOREGROUND_ONLY_ENABLED, false)

        if (enabled) {
            foregroundOnlyLocationService?.stopTrackingLocation()
        } else {
            if (foregroundPermissionApproved()) {
                foregroundOnlyLocationService?.startTrackingLocation()
                    ?: Log.d(TAG, "Service Not Bound")
            } else {
                requestForegroundPermissions()
            }
        }
    }

    /**
     * handles event related to ignition off
     */
    private fun ignitionOffHandler(){
        foregroundOnlyLocationService?.stopTrackingLocation()
        when {
            foregroundAndBackgroundLocationEnabled -> stopForegroundAndBackgroundLocation()
        }
        viewModel.completeOnGoingTrip(geoCoder)
    }

    /**
     * check permission of access fine location
     */
    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    private fun stopForegroundAndBackgroundLocation() {
        Log.d(TAG, "stopForegroundAndBackgroundLocation()")
        foregroundAndBackgroundLocationEnabled = false
    }

    private fun startForeService(){
        ignitionOnHandler()
        if (foregroundOnlyLocationServiceBound) {
            unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }
    }
    /**
     * changes the color of bluetooth icon in
     * the toolbar
     */
    private fun changeBTIconColor(){
        runOnUiThread {
            if(ecuParameters.isConnected){
                connection_bt_iv.setColorFilter(ContextCompat.getColor(this, R.color.selected_bike_color), android.graphics.PorterDuff.Mode.SRC_IN)
            }
            else{
                connection_bt_iv.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
            }
        }
    }

    private fun changeConnectivityUI(isConnected: Boolean){
        viewModel.connectionStatus.postValue(isConnected)
        changeBTIconColor()
        viewModel.iconColorChangeOnConnected.postValue(Event(isConnected))//change icon color of button as per connection
    }

    /**
     * used to run foreground service so as to
     * enable trip creation or connectivity in background
     */
    private fun triggerService() {

        val serviceIntent = Intent(this, ForegroundOnlyLocationService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE) //service is binded but not started to track location and sensor data
    }

    /**
     * Display the change password dialog. it accepts user email as input.
     * @author VE00YM023
     */
    private fun showChangePasswordDialog(){
        val dialogView = layoutInflater.inflate(R.layout.dialog_change_password, null, false)
        val inputEmail = dialogView.findViewById(R.id.input_email) as EditText
        val btnCancel = dialogView.findViewById(R.id.btn_cancel) as Button
        val btnOk = dialogView.findViewById(R.id.btn_ok) as Button
        inputEmail.setText(viewModel.getUserEmail())

        val resetPasswordDialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setView(dialogView)
            .create()

        btnCancel.setOnClickListener {
            resetPasswordDialog.dismiss()
        }
        btnOk.setOnClickListener {
            viewModel.resetPasswordEmail = inputEmail.text?.toString()
            resetPasswordDialog.dismiss()
            viewModel.changePassword()
        }
        resetPasswordDialog.show()
    }

    override fun proceedConnection(chassisNumber: String) {
        // disconnect connected bike if any
        proceedDisconnection()
        viewModel.proceedBikeConnection.postValue(Event(chassisNumber))
    }

    override fun proceedDisconnection() {
        // send disconnect command to MFECU
        val disconnectCommand = viewModel.getSpecificCommand(DISCONNET_MFECU_COMMAND_ID)
        var cmd = ByteArray(8)
        cmd = ViewUtils.prepareMFECUCommandNonEncrypted(disconnectCommand?:"75, 0, 0, 0, 0, 0, 0, 0")
        writeToBLEDevice(cmd)
        isFirstConnection = true
        scanning = false
        if(this::bluetoothAdapter.isInitialized  && bluetoothAdapter.isEnabled){
            bluetoothAdapter.disable()
            SystemClock.sleep(1000)
        }
        viewModel.setBikeBluetoothAddress(null)
        mIsDeviceConnected = true
        viewModel.disconnectConfirmation.postValue(Event(true))
    }

    override fun locationPermissionDenied() {
        getLocationPermission(this)
    }

    override fun proceedWithAction() {

    }

    private var previousBytes: ByteArray?=null

    private fun bytesToUnsigned_Double(b: ByteArray): Double {
        return (b[0].toInt() shl 8 and 0x0000ff00 or (b[1].toInt() and 0x000000ff).toDouble()
            .toInt()).toDouble()
    }

    /**
     * returns pattern of ELock
     * @param pattern byte array
     * @return pattern string
     */
    private fun getElockPatternFormatted(pattern: ByteArray): String? {
        var patternString = ""
        for (i in 2..5) {
            val x = pattern[i]
            if (x.toInt() == 76) {
                patternString += "L"
            } else if (x.toInt() == 82) {
                patternString += "R"
            }
        }
        return patternString
    }

    private fun handleDisconnectUI(){
        ecuParameters.isConnected = false
        ignitionOffHandler()
        viewModel.connectionStatus.postValue(false)
        changeBTIconColor()
        isConnectedToECU = false
        viewModel.iconColorChangeOnConnected.postValue(Event(false))//change icon color of button as per connection
        viewModel.bikeConnected.postValue(Event(false))
        viewModel.isConnectionEstablished.postValue(Event(false))
        if(this::activeBike.isInitialized) viewModel.activeBikeEntity.postValue(activeBike)
    }

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var isFirstConnection = true

    private val ECUService =
        UUID.fromString("00001815-0000-1000-8000-00805f9b34fb")
    private val ECUCharacteristicsNotify =
        UUID.fromString("00002a5a-0000-1000-8000-00805f9b34fb")
    private val ECUCharacteristicsWrite =
        UUID.fromString("00002a56-0000-1000-8000-00805f9b34fb")
    /**
     * this method kicks off the BLE connection
     */
    var someActivityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // There are no request codes
            Toast.makeText(this@HomeActivity, "Bluetooth enabled", Toast.LENGTH_SHORT).show()
            scanLeDevice()
        }
        else{
            // ask user to enable BT
            val connectionConfirmationDialog = BLEInfoDialog(viewModel)
            val ft = supportFragmentManager.beginTransaction().addToBackStack(null)
            connectionConfirmationDialog.show(ft,null)
            val handler = Handler()
            handler.postDelayed({
                showProgress(false)
            }, 3000)
         }
    }
    private fun triggerConnection(){
        val bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager!!.adapter
        if(!bluetoothAdapter.isEnabled){
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            someActivityResultLauncher.launch(intent)
            //Utils.writeToFile("--------------Bluetooth turned off, REQUEST PERMISSION TO TURN IT ON "+ Timestamp(System.currentTimeMillis()) +" ---------------------- ", application);
           /* val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)*/
         /*   Log.e("Observer","Bluetooth turned off, REQUEST PERMISSION TO TURN IT ON")*/
        }
        else{
           ////Utils.writeToFile("--------------Bluetooth ALREADY ON, SCANNING BLE DEVICES... "+ Timestamp(System.currentTimeMillis()) +" ---------------------- ", application);
            scanLeDevice()
          /*  Log.e("Observer","Bluetooth ALREADY ON, SCANNING BLE DEVICES...")*/
        }
    }

    private var isDeviceFound = false
    // Device scan callback.
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
           // Log.d("Device found",result.device.address)
          /*  Log.e("Device found","Found")*/
            isDeviceFound = true
            var strength = result.rssi
            var strengthIndicator = "FAIR"
            if(strength<0) strength = strength * (-1)
            if(strength>85) strengthIndicator = "POOR"
            if(strength<80) strengthIndicator = "GOOD"
            // ack user about device found and trying to connect
            val msg = resources.getString(R.string.vehicle_found) + "\nSignal strength "+strengthIndicator
            if(!userAckSet.contains(msg)) {
                viewModel.messageStringEvent.postValue(Event(msg))
            }
            userAckSet.add(msg)
            //initiaite connection from here
            val device = result.device

            mBluetoothGatt =
                device.connectGatt(this@HomeActivity, true, btleGattCallback)
            mBluetoothGatt?.connect()
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
          /*  Log.e("Device found","Not Found")*/
            val msg = resources.getString(R.string.scanning_error)
            if(!userAckSet.contains(msg)) {
                viewModel.messageStringEvent.postValue(Event(msg))
            }
            userAckSet.add(msg)
            // ack user about scanning failed
            //Utils.writeToFile("------------------ SCAN CALLBACK RESULT FAILED : "+ Timestamp(System.currentTimeMillis()) +" ---------------------- ", application)
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
        }
    }

    // Stops scanning after 20 seconds.
    private val SCAN_PERIOD: Long = 20000

    private var scanning = false
    private val handler = Handler()

    private fun scanLeDevice() {
        val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter.bluetoothLeScanner
        bluetoothLeScanner?.let { scanner ->
            Log.e("scanning var value ", scanning.toString())
            if (!scanning) { // Stops scanning after a pre-defined scan period.
                //Utils.writeToFile("--------------SCANNING BLE DEVICES "+ Timestamp(System.currentTimeMillis()) +" ---------------------- ", application);
                val msg = resources.getString(R.string.scanning_devices)
                isDeviceFound = false
                if(!userAckSet.contains(msg)) {
                    viewModel.messageStringEvent.postValue(Event(msg))
                }
                userAckSet.add(msg)
                handler.postDelayed({
                    scanning = false
                    if (this::bluetoothAdapter.isInitialized && bluetoothAdapter.isEnabled) {
                        scanner.stopScan(leScanCallback)
                        // ack user about scanning timeout
                        viewModel.isConnectionTimeout.postValue(Event(true))
                        runOnUiThread {
                          /*  Log.e("Device Found ",ecuParameters.isConnected.toString()+" ,  "+isDeviceFound)*/
                            if(!ecuParameters.isConnected && !isDeviceFound){
                                val msg = resources.getString(R.string.out_of_range)
                                viewModel.messageStringEvent.postValue(Event(msg))
                                userAckSet.add(msg)
                            }
                        }
                    }
                }, SCAN_PERIOD)
                scanning = true
                scanMFECU(bluetoothLeScanner, leScanCallback)
            } else {
                scanning = false
                scanner.stopScan(leScanCallback)
            }
        }
    }

    private fun scanMFECU(mBluetoothLeScanner: BluetoothLeScanner, scanCallback: ScanCallback){
        val scanFiltersList: MutableList<ScanFilter> =
            ArrayList()
        val btAdd = viewModel.getBikeBluetoothAddress()
        val scanFilter = ScanFilter.Builder().setDeviceAddress(btAdd).build()
        scanFiltersList.add(scanFilter)
        val scanSettings =
            ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()

      /*  val currentapiVersion = Build.VERSION.SDK_INT
        if (currentapiVersion >= Build.VERSION_CODES.S) {
            // Do something for lollipop and above versions
            checkBluetoothPermission()
        }*/
        try {
            mBluetoothLeScanner.startScan(scanFiltersList, scanSettings, scanCallback)
        }catch (ex : Exception){
        }

       /* Log.e("Observer","ScanMFECU Called")*/
    }

    private var gattError = false
    private var btleGattCallback: BluetoothGattCallback? = object : BluetoothGattCallback() {


        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
         /*   Log.e("BLEDeviceConnect", "onConnectionStateChange: newState $newState")
            Log.e("Shivani", "onConnectionStateChange: newState $newState")*/
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mBluetoothGatt = gatt
                unencryptedHandshake()
                //Utils.writeToFile("4. NEW_STATE == STATE_CONNECTED", application);
                gatt.discoverServices()
                // handle connect UI
                runOnUiThread {
                    viewModel.connectionStatus.postValue(true)
                    changeBTIconColor()
                    isConnectedToECU = true
                    viewModel.iconColorChangeOnConnected.postValue(Event(true))//change icon color of button as per connection
                    viewModel.bikeConnected.postValue(Event(true))
                    viewModel.isConnectionEstablished.postValue(Event(true))
                    mIsDeviceConnected = false
                    Handler(Looper.getMainLooper()).postDelayed({
                        //get ELock enable/disable status
                        viewModel.initialELockStatus.postValue(Event(true))
                    }, 3000)
                }
                ecuParameters.isConnected = true
                gattError = false
                /*Log.e("BLEDeviceConnect", "onConnectionStateChange: " + true)*/
            } else {
                previousBytes = null
                //Utils.writeToFile("***********VEHICLE IS DISCONNECTED*********** "+ Timestamp(System.currentTimeMillis()), application)
                if(!mIsDeviceConnected) {
                    gatt.connect()
                }
                // handle disconnect UI
                isFirstConnection = true
                runOnUiThread {
                    handleDisconnectUI()
                }
                ecuParameters.isConnected = false
                userAckSet.clear()
                /*Log.e("onConnectionStateChange", ": " + false + " old : " + status + "new :" + newState)*/
                if(status==133 && !gattError){
                    gattError = true
                    userAckSet.clear()
                    runOnUiThread {
                        viewModel.messageStringEvent.postValue(Event(resources.getString(R.string.bluetooth_issue_retry)))
                    }
                    // Signify GATT ERROR, retry connection
                    if(bluetoothAdapter!=null && bluetoothAdapter.isEnabled) {
                        bluetoothAdapter.disable()
                        SystemClock.sleep(1000)
                    }
                    triggerConnection()
                }
            }
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
           /* Log.e("Shivani", "2")*/
            //Status code and their indication is mentioned
            //at the bottom of this class
            Log.e("onCharacteristicWrite", "" + characteristic.uuid.toString() + " " + characteristic.value + " " + status)
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            //config ack and unencrypted handshake
            super.onDescriptorWrite(gatt, descriptor, status)
            /*Log.e("Shivani", "3")*/
            unencryptedHandshake()
            //Utils.writeToFile("onDescriptorWrite, TIME : " + Timestamp(System.currentTimeMillis()), application)
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            /*Log.e("Shivani", "4")*/
            val responseBytes = characteristic.value
            //Utils.writeToFile("6. BLUETOOTH GATT CALLBACK, onCharacteristicChanged, TIME : "+ Timestamp(System.currentTimeMillis())+" ${Arrays.toString(responseBytes)}", application);
            Log.e("Response Bytes ",Arrays.toString(responseBytes))
            // enable handshake with MFECU
            if (isFirstConnection) {
                unencryptedHandshake()
                isFirstConnection = false
            }
            else{
                Log.e("Notification ", "bytesQuery" + Arrays.toString(responseBytes))
                //Utils.writeToFile("8. BLUETOOTH GATT CALLBACK - DATA PACKETS, onCharacteristicChanged, TIME : "+ Timestamp(System.currentTimeMillis())+" ${Arrays.toString(responseBytes)}", application);
                GlobalScope.launch(Dispatchers.IO) {
                    //connection ack
                    if(responseBytes!=null && responseBytes.get(0).toInt()==71 && responseBytes.get(1).toInt()==1){
                        runOnUiThread {
                            val msg = resources.getString(R.string.able_to_connect)
                            if(!userAckSet.contains(msg)) {
                                viewModel.messageStringEvent.postValue(Event(msg))
                            }
                            userAckSet.add(msg)
                        }
                        //Utils.writeToFile("***********VEHICLE IS CONNECTED*********** "+ Timestamp(System.currentTimeMillis())+" ${Arrays.toString(responseBytes)}", application);
                    }

                    userAckSet.clear()
                    //answer back ack
                    if(responseBytes!=null && responseBytes.get(0).toInt()==65 && responseBytes.get(1).toInt()==1){
                        //Utils.writeToFile("***********ANSWER BACK ACK RECEIVED*********** "+ Timestamp(System.currentTimeMillis())+" ${Arrays.toString(responseBytes)}", application);
                    }

                    if (responseBytes != null && responseBytes.get(0).toInt() == 69 && responseBytes.get(5).toInt() == 1 && previousBytes != null && previousBytes!![5].toInt() == 0) {
                        Log.d("Hazard", "Active")
                        ecuParameters.hazardActivated = true
                        viewModel.isHazardActive.postValue(Event(true))
                        //Utils.writeToFile("HAZARD ACTIVATED , TIME : "+ Timestamp(System.currentTimeMillis())+" ${Arrays.toString(responseBytes)}", application);
                    }

                    if (responseBytes != null && responseBytes.get(0).toInt() == 69 && responseBytes.get(5).toInt() == 0 && previousBytes != null && previousBytes!![5].toInt() == 1) {
                        Log.d("Hazard", "Off")
                        ecuParameters.hazardActivated = false
                        viewModel.isHazardActive.postValue(Event(false))
                        //Utils.writeToFile("HAZARD DEACTIVATED , TIME : "+ Timestamp(System.currentTimeMillis())+" ${Arrays.toString(responseBytes)}", application);
                    }

                    //Check if brakes applied
                    if (responseBytes != null && responseBytes[3].toInt() == 1 && previousBytes != null && responseBytes[4].toInt() == 1 && previousBytes!![3].toInt() == 0) {
                        Log.d("Brake applied", "Yes")
                        ecuParameters.isBrakeApplied = true
                        viewModel.increaseBrakeCountInLastTrip()
                        var lastCount = viewModel.brakeCount.value?:0
                        lastCount++
                        viewModel.brakeCount.postValue(lastCount)
                        //Utils.writeToFile("BRAKES APPLIED , TIME : "+ Timestamp(System.currentTimeMillis())+" ${Arrays.toString(responseBytes)}", application);
                    }

                    //determine if received array contains ELock info
                    if (responseBytes != null && responseBytes[0].toInt() == 68 && responseBytes[1].toInt() == 71) {
                        val eLockPattern = getElockPatternFormatted(responseBytes)
                        if (eLockPattern != null) viewModel.eLockPattern.postValue(Event(eLockPattern))
                    }

                    //determine software version of the MFECU
                    if (responseBytes != null && responseBytes[0].toInt() == 68 && responseBytes[1].toInt() == 56) {
                        val majVer = responseBytes[2].toInt()
                        val minVer = responseBytes[3].toInt()
                        val softwareVersion = "${majVer}.${minVer}"
                        //Utils.writeToFile("MFECU SOFTWARE VERSION onCharacteristicChanged, TIME : "+softwareVersion + Timestamp(System.currentTimeMillis())+" ${Arrays.toString(responseBytes)}", application);
                        if(softwareVersion.toDouble()>=ELOCK_SOFTWARE_THRESHOLD){
                            viewModel.setIfElockCanBeDisabled(true)
                        }
                        else {
                            viewModel.setIfElockCanBeDisabled(false)
                        }
                    }

                    //check ELock status command ack
                    if (responseBytes != null && responseBytes[0].toInt() == 68 && responseBytes[1].toInt() == 69) {
                        val elockStatus = responseBytes[2].toInt()
                        if(elockStatus == 0){
                            viewModel.setELockEnabledStatus(true)
                        }
                        else if(elockStatus ==1){
                            viewModel.setELockEnabledStatus(false)
                        }
                    }

                    //determine if eLock is successfully written
                    if (responseBytes != null && responseBytes[0].toInt() == 67 && responseBytes[1].toInt() == 1) {
                        viewModel.eLockWritten.postValue(Event(true))
                    }

                    //determine if eLock is writing has failed
                    if (responseBytes != null && responseBytes[0].toInt() == 67 && responseBytes[1].toInt() != 1) {
                        viewModel.eLockWritten.postValue(Event(false))
                    }
                    if (responseBytes != null && responseBytes[4].toInt() == 1 && previousBytes != null && previousBytes!![4].toInt() == 0 && !ecuParameters.isIgnited) {
                        Log.d("Ignition status", "On")
                        //Utils.writeToFile("IGNITION ON, TIME : "+ Timestamp(System.currentTimeMillis())+" ${Arrays.toString(responseBytes)}", application);
                        ecuParameters.isIgnited = true
                        if (!isGPSEnabled()) {
                            showGPSDisabledAlert()
                        } else {
                            ignitionOnHandler()
                        }
                    } else if (responseBytes != null && responseBytes[4].toInt() == 0 && previousBytes != null && previousBytes!![4].toInt() == 1 && ecuParameters.isIgnited) {
                        Log.d("Ignition status", "Off")
                        //Utils.writeToFile("IGNITION OFF, TIME : "+ Timestamp(System.currentTimeMillis())+" ${Arrays.toString(responseBytes)}", application);
                        ecuParameters.isIgnited = false
                        ignitionOffHandler()
                    }
                    //calculate battery value
                    val batteryValues = byteArrayOf(responseBytes!![1], responseBytes[2])
                    val battery: Double = bytesToUnsigned_Double(batteryValues) / 1000
                    ecuParameters.battery = battery.toFloat()
                    if (!battery.isNaN() && (Utils.getTimeInMilliSec())%5==0L) viewModel.batteryVoltage.postValue(Event(battery.toString()))
                    Log.d("Battery value", battery.toString())
                    previousBytes = responseBytes
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            /*Log.e("Shivani", "5")*/
            //Utils.writeToFile("5. BLUE TOOTH GATT CALLBACK, onServicesDiscovered, TIME : "+ Timestamp(System.currentTimeMillis()), application);
            mBluetoothGatt = gatt
            setNotifyCharacteristics()
            Log.e("onServicesDiscovered", "" + true)
        }
    }

    fun writeToBLEDevice(bytesQuery: ByteArray?) {
        try {
            if (mBluetoothGatt != null) {
                val bluetoothGattServices: BluetoothGattService? =
                    mBluetoothGatt?.getService(ECUService)
                if (bluetoothGattServices != null) {
                    val bluetoothGattCharacteristic =
                        bluetoothGattServices.getCharacteristic(ECUCharacteristicsWrite)
                    bluetoothGattCharacteristic.value = bytesQuery
                    bluetoothGattCharacteristic.writeType =
                        BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                    mBluetoothGatt?.writeCharacteristic(bluetoothGattCharacteristic)
                } else {
                    Log.d("error", "btGattSevice is null")
                }
            }
        }
        catch (ex: java.lang.Exception){
            Log.d("error", "btGattSevice is null")
        }
    }

    fun setNotifyCharacteristics(): Boolean {
        var notify = false
        val bluetoothGattServices = mBluetoothGatt!!.getService(ECUService?:UUID.fromString("00001815-0000-1000-8000-00805f9b34fb"))
        val gattCharacteristics =
            bluetoothGattServices.characteristics
        for (bluetoothGattCharacteristic in gattCharacteristics) {
            Log.e(
                "NotifyCharacteristic",
                "" + bluetoothGattCharacteristic.uuid
                    .toString() + " " + bluetoothGattCharacteristic.writeType
            )
            if (bluetoothGattCharacteristic.uuid == ECUCharacteristicsNotify) {
                for (descriptor in bluetoothGattCharacteristic.descriptors) {
                    Log.e("descriptor", "" + descriptor.uuid)
                    descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    /*if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return true
                    }*/
                    mBluetoothGatt!!.writeDescriptor(descriptor)
                }
                notify =
                    mBluetoothGatt!!.setCharacteristicNotification(
                        bluetoothGattCharacteristic,
                        true
                    )
            }
        }
        return notify
    }

    override fun askBTPermissionOnDenial() {
        //ask for BTPermission
        triggerConnection()
    }

    private fun getELockEnableDisableCheck(){
        //get software version and determine
        // if ELock can be disabled or not
        if (ecuParameters.isConnected) {
            GlobalScope.launch(Dispatchers.IO) {
                val softwareVersionCommand = viewModel.getSpecificCommand(SOFTWARE_VER_COMMAND_ID)?:"68, 56, 0, 0, 0, 0, 0, 0"
                var cmd = ByteArray(8)
                cmd = ViewUtils.prepareMFECUCommandNonEncrypted(softwareVersionCommand)
                writeToBLEDevice(cmd)
            }
        }
    }

    /**
     * sets the UI of bike
     * in home activity
     */
    private fun setBikeUI(){
        viewModel.setScooterImage.postValue(Event(false))
        viewModel.removeELockIcon.postValue(Event(false))
        viewModel.setIfScooter(false)
        viewModel.loadScooterIcons.postValue(false)
        viewModel.loadBikeIcons.postValue(true)
        val navMenu = nav_view.menu
        navMenu.findItem(R.id.nav_your_vehicles).setIcon(R.drawable.icon_bike_blue)
    }

    /**
     * sets the UI of scooter
     * in home activity
     */
    private fun setScooterUI(){
        viewModel.setScooterImage.postValue(Event(true))
        viewModel.removeELockIcon.postValue(Event(true))
        viewModel.setIfScooter(true)
        viewModel.loadBikeIcons.postValue(false)
        viewModel.loadScooterIcons.postValue(true)
        val navMenu = nav_view.menu
        navMenu.findItem(R.id.nav_your_vehicles).setIcon(R.drawable.icon_nav_drawer_scooter)
    }

    /**
     * this follows mechanism for devices
     * where SEED KEY is not received
     */
    private fun unencryptedHandshake(){
        Log.d("Handshake Format","Unencrypted")
        val cmd = ByteArray(8)
        cmd[0] = 104.toByte()
        cmd[1] = 112.toByte()
        cmd[2] = 109.toByte()
        cmd[3] = 0
        cmd[4] = 0
        cmd[5] = 0
        cmd[6] = 0
        cmd[7] = 0
        writeToBLEDevice(cmd)
    }
}