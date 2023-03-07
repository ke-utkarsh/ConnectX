package ymsli.com.ea1h.views.addbike

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  VE00YM023
 * @date    10/02/2020 2:45 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ScanQRCodeActivity : This is class which will scan the QR code to add the bike
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import android.Manifest
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.MenuItem
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener.Builder.withContext
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener.Builder.withContext
import kotlinx.android.synthetic.main.scan_qr_activity.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseActivity
import ymsli.com.ea1h.di.component.ActivityComponent
import ymsli.com.ea1h.utils.common.Constants.INTENT_KEY_CHASSIS_NO
import ymsli.com.ea1h.utils.common.Constants.INTENT_KEY_PHONE_NO
import ymsli.com.ea1h.utils.common.Constants.INTENT_KEY_QR_CODE
import ymsli.com.ea1h.utils.common.Constants.INTENT_KEY_REQUEST_TYPE
import ymsli.com.ea1h.utils.common.RequestType


class ScanQRCodeActivity : BaseActivity<ScanQRCodeViewModel>(), BarcodeCallback {

    companion object {
        private const val PREFIX_SUCCESSFUL = "Scanning "
        private const val PERMISSION_REQUEST = 101
        private const val PACKAGE = "package"
    }

    override fun provideLayoutId(): Int = R.layout.scan_qr_activity
    override fun injectDependencies(ac: ActivityComponent) = ac.inject(this)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){ android.R.id.home -> onBackPressed() }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        barcode_scanner.resume()
        barcode_scanner.decodeSingle(this)
    }
    override fun onPause()  { super.onPause(); barcode_scanner.pause() }

    override fun setupView(savedInstanceState: Bundle?) {
        setupToolbar()
        extractIntentParams()
        setupScanner()
        checkPermissionAndContinue()
    }

    /**
     * Sets up the toolbar with activity TITLE and configure it
     * to display the back button.
     * @author VE00YM023
     */
    private fun setupToolbar(){
        window?.statusBarColor = getColor(R.color.bg_status_bar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.bg_toolbar)))
    }

    /**
     * Extract the intent parameters received from the the ChassisNumberActivity.
     * @author VE00YM023
     */
    private fun extractIntentParams(){
        viewModel.chassisNumber = intent.getStringExtra(ChassisNumberActivity.CHASSIS_NUMBER_TAG)!!
        viewModel.phoneNumber = intent.getStringExtra(ChassisNumberActivity.PHONE_NUMBER_TAG)!!
    }

    /**
     * Sets up the QRCode scanner view
     * @author VE00YM023
     */
    private fun setupScanner(){
        barcode_scanner.barcodeView.decoderFactory = DefaultDecoderFactory()
        barcode_scanner.initializeFromIntent(intent)
        barcode_scanner.decodeSingle(this)
    }

    /**
     * After visiting settings check if user has granted the required
     * permissions, if not then show the permissions required dialog
     * @author VE00YM023
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PERMISSION_REQUEST) {
            super.onActivityResult(requestCode, resultCode, data)
            checkPermissionAndContinue()
            return
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.isQrCodeValid.observe(this, Observer {
            it.getIfNotHandled()?.let { event ->
                if(event){
                    val intent = Intent(this, TermsAndConditionsActivity::class.java)//open T&C once QR code is validated
                    intent.putExtra(INTENT_KEY_CHASSIS_NO, viewModel.chassisNumber)
                    intent.putExtra(INTENT_KEY_QR_CODE, viewModel.qrCodeField.value)
                    intent.putExtra(INTENT_KEY_REQUEST_TYPE, RequestType.VEHICLE_REGISTRATION.code)
                    intent.putExtra(INTENT_KEY_PHONE_NO, viewModel.phoneNumber)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        })

        viewModel.showProgress.observe(this, Observer { showProgress(it) })

        /* When API fails show api error in dialog and start bar code scanning,
           if no message is received then show generic message toast and start scanning */
        viewModel.apiFailed.observe(this, Observer { if(it) {
            val errorMessage = viewModel.apiErrorMessage?:getString(R.string.something_went_wrong)
            showNotificationDialog(errorMessage, R.drawable.ic_error){
                barcode_scanner.decodeSingle(this)
            }
        } })
    }

    /**
     * Continues with image picker if the required permission for image selection (Camera, Gallery)
     * are granted, otherwise asks the user for specified permissions.
     *
     * @author VE00YM023
     */
    private fun checkPermissionAndContinue() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.CAMERA)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (!report.areAllPermissionsGranted()) { showSettingsDialog() }
                }
                override fun onPermissionRationaleShouldBeShown(perms: List<PermissionRequest>,
                                                                token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).check()


    }


    /**
     * If user have denied any permission request then we show the appropriate rational.
     * @author Balraj VE00YM023
     */
    private fun showSettingsDialog() {
        val message = getString(R.string.dialog_camera_permission)
        val actionOk = { openSettings() }
        val actionCancel = { finish() }
        val positiveLabel = getString(R.string.go_to_settings)
        showConfirmationDialog(message, positiveLabel, actionCancel, actionOk)
    }

    /**
     * Opens the application settings system dialog.
     * @author Balraj VE00YM023
     */
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts(PACKAGE, packageName, null)
        }
        startActivityForResult(intent, PERMISSION_REQUEST)
    }

    /** Callback methods for Barcode scanner */
    override fun barcodeResult(result: BarcodeResult) {
        viewModel.qrCodeField.value = result.text
        showConfirmationDialog()
    }
    override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}


    /**
     * When scanning completes successfully.
     * show confirmation dialog with scanned bluetooth address.
     * @author VE00YM023
     */
    private fun showConfirmationDialog(){
        val dialogView = layoutInflater.inflate(R.layout.dialog_scan_qr_confirm, null, false)
        val tvScanned = dialogView.findViewById(R.id.tv_scanned_qr) as TextView
        val btnCancel = dialogView.findViewById(R.id.btn_cancel) as Button
        val btnConfirm = dialogView.findViewById(R.id.btn_confirm) as Button

        val qrConfirmDialog = AlertDialog.Builder(this, R.style.DialogSlideAnim)
            .setCancelable(false)
            .setView(dialogView)
            .create()

        val msg = PREFIX_SUCCESSFUL + viewModel.qrCodeField.value
        tvScanned.text = toUIView(msg)
        btnCancel.setOnClickListener {
            qrConfirmDialog.dismiss()
            barcode_scanner.decodeSingle(this)
        }
        btnConfirm.setOnClickListener {
            qrConfirmDialog.dismiss()
            viewModel.validateQRCode()
        }
        qrConfirmDialog.show()
        qrConfirmDialog?.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
        val params = qrConfirmDialog.window?.attributes
        params?.gravity = Gravity.BOTTOM
        params?.flags = params!!.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        qrConfirmDialog.window?.attributes = params
    }

    /**
     * Returns this string in specified color span.
     * @param color color to be used for the span
     * @author VE00YM023
     */
    private fun toUIView(str: String): SpannableString{
        val color = getColor(R.color.text_scanned_qr_code)
        val spannable = SpannableString(str)
        spannable.setSpan(ForegroundColorSpan(color), 9, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
    }
}