/**
 * Project Name : EA1H
 * @company YMSLI
 * @author  VE00YM129
 * @date   February 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * BaseAdapter : This abstract class is the base adapter of all the RV adapters
 *                  and contains common to provide in recycler view adapters
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
package ymsli.com.ea1h.base

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.gigya.android.sdk.Gigya
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dmax.dialog.SpotsDialog
import ymsli.com.ea1h.EA1HApplication
import ymsli.com.ea1h.GigyaResponse
import ymsli.com.ea1h.R
import ymsli.com.ea1h.database.entity.BTCommandsLogEntity
import ymsli.com.ea1h.database.entity.UserEntity
import ymsli.com.ea1h.di.component.ActivityComponent
import ymsli.com.ea1h.di.component.DaggerActivityComponent
import ymsli.com.ea1h.di.module.ActivityModule
import ymsli.com.ea1h.services.ECUParameters
import ymsli.com.ea1h.utils.common.Constants
import ymsli.com.ea1h.utils.common.Constants.NULL
import ymsli.com.ea1h.utils.common.Event
import ymsli.com.ea1h.utils.common.Utils
import ymsli.com.ea1h.views.ackdialogs.*
import ymsli.com.ea1h.views.entrance.EntranceActivity
import ymsli.com.ea1h.views.experiment.ForegroundOnlyLocationService
import ymsli.com.ea1h.views.splash.LaunchActivity
import ymsli.com.ea1h.views.userengagement.ErrorAckFragment
import ymsli.com.ea1h.views.userengagement.MFECUProgressFragment
import ymsli.com.ea1h.views.userengagement.ProgressFragment
import javax.inject.Inject

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity(){

    /* BT Connectivity parameters */
    lateinit var ecuParameters: ECUParameters

    private var userEntity: UserEntity? = null
    @Inject
    lateinit var viewModel: VM
    @Inject
    lateinit var gigya: Gigya<GigyaResponse>
    @Inject
    lateinit var locationManager: LocationManager
    @Inject
    lateinit var userAckFragment: ErrorAckFragment
    @Inject
    lateinit var progressFragment: ProgressFragment
    @Inject
    lateinit var mfecuProgressFragment: MFECUProgressFragment

    private lateinit var currentActivity: BaseActivity<*>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var commandDialog: SpotsDialog

    companion object {
        const val ANSWER_BACK_DIALOG = 101
        const val LOCATE_BIKE_DIALOG = 102
        const val ELOCK_DIALOG = 103
        const val DISCONNECT_DIALOG = 104
        private const val LOCATION_REQUEST_CODE = 1000
        private const val GPS_REQUEST_CODE = 100
        private const val TAG_GLOBAL_EXCEPTION = "Global Exception"
    }

    @LayoutRes
    protected abstract fun provideLayoutId(): Int

    protected abstract fun injectDependencies(activityComponent: ActivityComponent)
    protected abstract fun setupView(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        globalExceptionHandler() //prevents app crash for non fatal errors
        injectDependencies(buildActivityComponent())
        super.onCreate(savedInstanceState)

        if(viewModel.getHardLogout()){
            ecuParameters = ECUParameters.getEcuParametersInstance()
            //perform logout operations and reset hard logout in shared prefs
            performHardLogout()
            viewModel.setHardLogout(false)
        }

        setContentView(provideLayoutId())
        setupObservers()
        setupView(savedInstanceState)
        viewModel.onCreate()

        // prevents screenshot of activity containing sensitive information
        // based on flag passed through API
        val flag = viewModel.getKeyMiscData()
        if(flag.isEmpty()){
            window?.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
        else{
            val miscData = flag.get(0)
            val descValue = miscData.descriptionValue
            if(descValue.contains("false",true)){
                window?.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }
        }
    }

    private fun buildActivityComponent() =
        DaggerActivityComponent
            .builder()
            .applicationComponent((application as EA1HApplication).applicationComponent)
            .activityModule(ActivityModule(this))
            .build()

    protected open fun setupObservers() {
        /* Observers to post messages to the UI */
        viewModel.messageString.observe(this, Observer {
            it?.data?.run { showMessage(this) }
        })

        viewModel.messageStringEvent.observe(this, Observer {
            it.getIfNotHandled().let {
                // show BT Connection ack to user
                if(!it.isNullOrBlank()){
                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.messageStringId.observe(this, Observer {
            it?.data?.run { showMessage(this) }
        })

        viewModel.message.observe(this, Observer {
            showMessage(it)
        })

        viewModel.userAckSuccessDialog.observe(this, Observer {
            it.getIfNotHandled()?.let {
                showAckDialog("Successful", it)
            }
        })

        viewModel.userBlockerDialog.observe(this, Observer {
            it.getIfNotHandled()?.let {
                showBlockerDialog("FATAL ERROR",resources.getString(it))
            }
        })

        viewModel.userAlertDialog.observe(this, Observer {
            it.getIfNotHandled()?.let {
                showAckDialog(it)
            }
        })

        viewModel.userErrorDialog.observe(this, Observer {
            it.getIfNotHandled()?.let {
                showErrorDialog(resources.getString(R.string.error_label), it)
            }
        })

        viewModel.disconnectECUDialog.observe(this, Observer {
            it.getIfNotHandled()?.let {
                showECUDisconnectDialog()
            }
        })

        viewModel.showBLEInfoDialog.observe(this, Observer {
            it.getIfNotHandled()?.let {
                showBLEInfoDialog()
            }
        })

        viewModel.disconnectConfirmation.observe(this, Observer {
            it.getIfNotHandled()?.let {
                if (it) {
                    disconnectMFECU()
                }
            }
        })

        /** If re-login fails then redirect user to login activity */
        viewModel.getUserEntityLive().observe(this, Observer {
            if (it.isNotEmpty() && it[0].reLoginFailure) {
                val thisIsLoginActivity = (this as? EntranceActivity) != null
                if (!thisIsLoginActivity) {
                    viewModel.updateReLoginFailureStatus(false)
                    val intent = Intent(this, EntranceActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })

        //Below observer is to implement force logout via silent notification
        viewModel.getMiscDataByKeyLiveData(resources.getString(R.string.logout_push_notif_key)).observe(this, Observer {
            //Checks change in value of logout
            if(it.isNotEmpty()){
                performLogout()
            }
        })
    }

    /**
     * used for logging out the user
     * from the application
     */
    fun performLogout(){
        try {
            if (ecuParameters.isConnected) {
                viewModel.disconnectConfirmation.value = Event(true)
            }
            disconnectMFECU()
            viewModel.removeLastConnectedBikeDuringLogout()
            stopForegroundService()
            viewModel.logoutCustomUser()
            val btCommandsLogEntity = BTCommandsLogEntity(
                cmdType = 21,
                chassNum = Constants.NA,
                btAdd = Constants.NA,
                triggeredOn = Utils.getTimeInMilliSec(),
                isSuccessful = true,
                isCommit = true,
                deviceId = viewModel.getAndroidIdFromSharedPref()
            )
            viewModel.logBTCommandInRoom((btCommandsLogEntity))
            val entranceIntent = Intent(this, EntranceActivity::class.java)
            entranceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(entranceIntent)
            finish()
        }
        catch (ex: java.lang.Exception){
            FirebaseCrashlytics.getInstance().recordException(ex)
        }
    }

    /**
     * used for hard logging out the user
     * from the application
     */
    fun performHardLogout(){
        try {
            if (ecuParameters.isConnected) {
                viewModel.disconnectConfirmation.value = Event(true)
            }
            disconnectMFECU()
            viewModel.removeLastConnectedBikeDuringLogout()
            stopForegroundService()
            viewModel.logoutCustomUser()

            if((this::class != LaunchActivity::class)){
                val entranceIntent = Intent(this, EntranceActivity::class.java)
                entranceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(entranceIntent)
                finishAffinity()
                finish()
            }
        }
        catch (ex: java.lang.Exception){
            FirebaseCrashlytics.getInstance().recordException(ex)
        }
    }

    /**
     * disconnects MFECU from the phone
     */
    fun disconnectMFECU(){
        viewModel.removeLastConnectedBike()
    }

    /** show or hide progress based on input parameter */
    fun showProgress(showProgress: Boolean) {

        if (showProgress) {
            if (!progressFragment.isAdded) {
                val frag = supportFragmentManager.findFragmentByTag(ProgressFragment.TAG)
                if (!(frag != null && frag is ProgressFragment)) {
                    progressFragment.show(supportFragmentManager, ProgressFragment.TAG)
                    progressFragment.isCancelable = false
                }
            }
        } else if ((progressFragment.isResumed))
        {
            progressFragment.dismiss()
        }
    }

    /**
     * shows progress along with message, used specifically
     * for MFECU Commands
     */
    fun showProgressOnECUCommands(showProgress: Boolean) {
        if (showProgress) {
            if (!mfecuProgressFragment.isAdded) {
                val frag = supportFragmentManager.findFragmentByTag(MFECUProgressFragment.TAG)
                if (!(frag != null && frag is MFECUProgressFragment)) {
                    mfecuProgressFragment.show(supportFragmentManager, MFECUProgressFragment.TAG)
                    mfecuProgressFragment.isCancelable = false
                }
            }
        } else if (mfecuProgressFragment.isResumed) mfecuProgressFragment.dismiss()
    }

    /**
     * acknowldege user about any error/issue
     * using dialog fragment
     */
    protected open fun showErrorsViaSnackbar(@StringRes messageId: Int) {
        showNotificationDialog(getString(messageId), R.drawable.ic_error){}
    }

    /**
     * acknowldege user about any error/issue
     * using dialog fragment
     */
    protected open fun showErrorsMessageViaSnackbar(message: String) {
        showNotificationDialog(message, R.drawable.ic_error){}
    }

    /** Post a message to the UI using Toast */
    fun showMessage(message: String) =
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()

    fun showMessage(@StringRes resId: Int) = showMessage(getString(resId))

    /** tells if GPS is active or not */
    fun isGPSEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /** tells if app is granted location permission or not */
    private fun isLocationPermitted(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    /** checks location permission for the activity */
    protected open fun getLocationPermission(activity: BaseActivity<*>) {
        currentActivity = activity
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGpsEnabled) buildAlertMessageGPS()
        else {
            val provideRationale = foregroundAndBackgroundPermissionApproved()
            val locationPermission = if (Build.VERSION.SDK_INT >= 29) Manifest.permission.ACCESS_FINE_LOCATION else Manifest.permission.ACCESS_COARSE_LOCATION
            val permissionRequests = arrayListOf(locationPermission)
            if (!provideRationale) {
                ActivityCompat.requestPermissions(this,
                    permissionRequests.toTypedArray(), LOCATION_REQUEST_CODE

                )
            }
        }
    }



    private fun foregroundAndBackgroundPermissionApproved(): Boolean {
        val foregroundLocationApproved = foregroundPermissionApproved()
        val backgroundPermissionApproved =
            ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        return foregroundLocationApproved && backgroundPermissionApproved
    }

    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    }

    private fun buildAlertMessageGPS() {
        val locationSettingsBuilder: LocationSettingsRequest.Builder =
            LocationSettingsRequest.Builder().addLocationRequest(LocationRequest.create())
        locationSettingsBuilder.setAlwaysShow(true)
        val result = LocationServices.getSettingsClient(this)
            .checkLocationSettings(locationSettingsBuilder.build())

        result.addOnCompleteListener { p0 ->
            try {
                p0.getResult(ApiException::class.java)
            } catch (excep: ApiException) {
                when ((excep).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        val resolvable: ResolvableApiException = excep as ResolvableApiException
                        resolvable.startResolutionForResult(currentActivity, GPS_REQUEST_CODE)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val mainHandler = Handler(this.mainLooper)
                    mainHandler.post { getLocationPermission(this) }
                } else {
                    getLocationPermission(currentActivity)
                }
            }
        }
    }

    /** used for hiding keyboard upon button click */
    fun hideKeyboard(viewId: ViewGroup) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow((viewId as View).windowToken, 0)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty()) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    if (this::fusedLocationClient.isInitialized) {
                        fusedLocationClient.lastLocation
                            .addOnSuccessListener { viewModel.initiateDeviceDiscovery.postValue(true) }
                    }
                    requestForBluetooth()
                } else {
                    val errorAlert = LocationPermissionDeniedDailog()
                    val ft = supportFragmentManager.beginTransaction().addToBackStack(null)
                    errorAlert.show(ft,null)
                    //viewModel.userAlertDialog.postValue(Event(R.string.location_permission_required))
                }
            }
        }
    }

    private fun requestForBluetooth() {
       val currentapiVersion = Build.VERSION.SDK_INT
       if (currentapiVersion >= Build.VERSION_CODES.S) {
           // Do something for lollipop and above versions
           checkBluetoothPermission()
       } else {
           checkBluetoothPermissionFor11();
       }
    }
    /*
    * Added by @ve00ym430
    * Bluetooth connection for Android 11.
    * */
    private fun checkBluetoothPermissionFor11() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_SCAN
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) { /* ... */
                }
            }).check()

    }

    /*
    * Added by @ve00ym430
    * Bluetooth connection for Android 13 and lower.
    * */
    private fun checkBluetoothPermission() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) { /* ... */
                }
            }).check()
    }

    fun provideUserAckDialog(
        @StringRes messageId: Int,
        onConfirm: DialogInterface.OnClickListener,
        @StyleRes theme: Int
    ): AlertDialog? {
        val userAckDialogBuilder = AlertDialog.Builder(this, theme)
        userAckDialogBuilder.setMessage(this.resources.getString(messageId))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.ACTION_OK), onConfirm)
        return userAckDialogBuilder.create()
    }

    override fun onStart() {
        super.onStart()
        val userEnt = viewModel.getUserEntity()
        if (userEnt.token != NULL) {
            userEntity = userEnt
        }
    }

    /** shows both type of ack dialogs to user -failed/successful */
    private fun showBlockerDialog(title: String, message: String) {
        (AlertDialog.Builder(this))
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .show()
    }

    /** shows both type of ack dialogs to user -failed/successful */
    private fun showAckDialog(title: String, message: String) {
        (AlertDialog.Builder(this))
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.ACTION_OK)) { dialog, _ ->
                dialog?.dismiss()
                viewModel.dialogPositiveButtonAction.postValue(Event(true))
            }.show()
    }

    /** shows successful ack dialogs to user */
    private fun showAckDialog(messageId: Int) {
        // show custom dialog for disconnection
        val genericAckDialog = GenericAckDialog(this.resources.getString(messageId))
        val ft = supportFragmentManager.beginTransaction().addToBackStack(null)
        genericAckDialog.show(ft,null)
    }

    /**
     * shows failed ack dialogs to user
     * After tapping OK button, no event is captured
     */
    private fun showErrorDialog(title: String, message: String) {
        (AlertDialog.Builder(this))
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.ACTION_OK)) { dialog, _ ->
                dialog?.dismiss()
            }.show()
    }

    /** this function handles disconnection of MFECU from BT icon in toolbar */
    private fun showECUDisconnectDialog() {
        // show custom dialog for disconnection
        val disconnectionConfirmationDialog = DisconnectionConfirmationDialog()
        val ft = supportFragmentManager.beginTransaction().addToBackStack(null)
        disconnectionConfirmationDialog.show(ft,null)
    }

    /** this function handles disconnection of MFECU from BT icon in toolbar */
    private fun showBLEInfoDialog() {
        // show custom dialog for disconnection
        val bleInfoDialog = BLEInfoDialog(viewModel)
        val ft = supportFragmentManager.beginTransaction().addToBackStack(null)
        bleInfoDialog.show(ft,null)
    }

    /** this is global exception handler and prevents app at any stage from crashing */
    private fun globalExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler { _, paramThrowable ->
            FirebaseCrashlytics.getInstance().recordException(Exception("Global Exception thrown" +paramThrowable.message))
            FirebaseCrashlytics.getInstance().recordException(paramThrowable)
        }
    }

    fun stopForegroundService(){
        val foregroundIntent = Intent(this,ForegroundOnlyLocationService::class.java)
        stopService(foregroundIntent)
    }

    /** displays progress of BT Action commands */
    fun showCommandProgress(showProgress: Boolean, dialogType: Int) {
        runOnUiThread {
            if (showProgress) {
                when (dialogType) {
                    ANSWER_BACK_DIALOG -> {
                        commandDialog = (SpotsDialog.Builder().setContext(this).setMessage(this.resources.getString(R.string.progress_answer_back)).setCancelable(false).build() as SpotsDialog)
                    }
                    LOCATE_BIKE_DIALOG -> {
                        commandDialog = (SpotsDialog.Builder().setContext(this).setMessage(this.resources.getString(R.string.progress_locate_my_bike)).setCancelable(false).build() as SpotsDialog)
                    }
                    ELOCK_DIALOG -> {
                        commandDialog = (SpotsDialog.Builder().setContext(this).setMessage(this.resources.getString(R.string.progress_elock)).setCancelable(false).build() as SpotsDialog)
                    }

                    DISCONNECT_DIALOG -> {
                        commandDialog = (SpotsDialog.Builder().setContext(this).setMessage(this.resources.getString(R.string.disconnecting_bike)).setCancelable(false).build() as SpotsDialog)
                    }
                }
                if(this::commandDialog.isInitialized) {
                    commandDialog.setCancelable(false)
                    if (commandDialog.isShowing) {
                        commandDialog.dismiss()
                    }
                    commandDialog.show()
                }
            } else {
                if (this::commandDialog.isInitialized && commandDialog.isShowing) {
                    commandDialog.dismiss()
                }
            }
        }
    }

    /** shows GPS Disabled alert dialog */
    fun showGPSDisabledAlert() {
        if (!isGPSEnabled()) {
            viewModel.userErrorDialog.postValue(Event(resources.getString(R.string.turn_on_gps_dialog)))
        }
    }

    /** shows user the alert to access location */
    fun showLocationPermissionAlert() {
        if (!isLocationPermitted()) {
            viewModel.userErrorDialog.postValue(Event(resources.getString(R.string.access_location_dialog)))
        }
    }

    /**
     * Inflates the UI for a confirmation dialog, which contains message text and
     * two action buttons.
     * @param message confirmation message to display
     * @param labelOk label for the 'POSITIVE' action button
     * @return Triple containing dialog view as first, 'POSITIVE BTN' as second
     *         and 'CANCEL BTN' as third
     *
     * @author VE00YM023
     */
    private fun inflateConfirmationDialogView(message: String, labelOk: String)
            : Triple<View, Button, Button>{
        val dialogView = layoutInflater.inflate(R.layout.dialog_confirmation, null, false)
        val tvMessage = dialogView.findViewById(R.id.tv_message) as TextView
        val btnCancel = dialogView.findViewById(R.id.btn_cancel) as Button
        val btnOk = dialogView.findViewById(R.id.btn_ok) as Button
        btnOk.text = labelOk
        tvMessage.text = message
        return Triple(dialogView, btnOk, btnCancel)
    }

    /**
     * Displays a confirmation dialog containing a message and two actions buttons.
     * @param msg confirmation message to be displayed
     * @param labelAction label of 'POSITIVE' Action
     * @param actionCancel
     * @param actionOk
     * @author VE00YM023
     */
    protected fun showConfirmationDialog(msg: String, labelAction: String = getString(R.string.ACTION_OK),
                                        actionCancel: () -> Unit, actionOk: () -> Unit){
        val (dialogView, btnOk, btnCancel) = inflateConfirmationDialogView(msg, labelAction)
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setCancelable(false)
            .setView(dialogView)
            .create()
        btnCancel.setOnClickListener { actionCancel(); dialog.dismiss() }
        btnOk.setOnClickListener { dialog.dismiss(); actionOk() }
        dialog.show()
    }

    /**
     * Displays a notification dialog containing a message and an action button.
     * @param message notification message to be displayed
     * @param iconId resource id of icon to be used
     * @param actionOk
     * @author VE00YM023
     */
    protected fun showNotificationDialog(message: String, iconId: Int,
                                         actionOk: () -> Unit){
        val dialogView = layoutInflater.inflate(R.layout.dialog_notification, null, false)
        val tvMessage = dialogView.findViewById(R.id.tv_message) as TextView
        val icon = dialogView.findViewById(R.id.iv_icon) as ImageView
        val btnOk = dialogView.findViewById(R.id.btn_ok) as Button
        tvMessage.text = message
        icon.setImageResource(iconId)
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setCancelable(false)
            .setView(dialogView)
            .create()
        btnOk.setOnClickListener {
            dialog.dismiss()
            actionOk()
        }
        dialog.show()
    }


    /**
     * Displays a notification dialog containing a message, title and an action button.
     * @param title notification title to be displayed
     * @param message notification message to be displayed
     * @param actionOk
     * @author VE00YM023
     */
    protected fun showNotificationDialogWithTitle(title: String, message: String,
                                                  actionOk: () -> Unit){
        val dialogView = layoutInflater.inflate(R.layout.dialog_notification_dialog_title, null, false)
        val tvTitle = dialogView.findViewById(R.id.tv_title) as TextView
        val tvMessage = dialogView.findViewById(R.id.tv_message) as TextView
        val btnOk = dialogView.findViewById(R.id.btn_ok) as Button
        tvTitle.text = title
        tvMessage.text = message
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setCancelable(false)
            .setView(dialogView)
            .create()
        btnOk.setOnClickListener {
            dialog.dismiss()
            actionOk()
        }
        dialog.show()
    }

    /**
     * open app specific settings in phone
     */
    fun openPowerSettings(message: String?) {
        // show alert dialog
        val backgroundAlert = BackgroundSettingsDialog(message)
        val ft = supportFragmentManager.beginTransaction().addToBackStack(null)
        backgroundAlert.show(ft,null)
    }
}