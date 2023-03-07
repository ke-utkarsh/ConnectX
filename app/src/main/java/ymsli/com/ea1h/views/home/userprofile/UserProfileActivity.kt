/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM023)
 * @date    28/02/2020 11:45 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * UserProfileFragment : This fragment represents the user's profile section.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

package ymsli.com.ea1h.views.home.userprofile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_user_profile.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseFragment
import ymsli.com.ea1h.database.EA1HSharedPreferences.Companion.EMPTY_STRING
import ymsli.com.ea1h.di.component.FragmentComponent
import ymsli.com.ea1h.utils.common.ImagePickerActivity


class UserProfileActivity : BaseFragment<UserProfileViewModel>() {

    private companion object {
        private const val KEY_PATH = "path"
        private const val PACKAGE = "package"
        private const val REQUEST_IMAGE_CAPTURE = 0
        private const val IMAGE_LOADING_FAILED = "Failed to load image, please try again"
        private const val NAME_INPUT_ERROR = "Please Enter your name"
        private const val ASPECT_RATIO_X = 1
        private const val ASPECT_RATIO_Y = 1
        private const val BITMAP_MAX_WIDTH =  1000
        private const val BITMAP_MAX_HEIGHT = 1000
    }

    override fun provideLayoutId() = R.layout.fragment_user_profile
    override fun injectDependencies(fc: FragmentComponent) = fc.inject(this)

    override fun setupView(view: View) {
        setClickListeners()
        viewModel.messageString.value = null
        viewModel.messageStringId.value = null
        viewModel.loadDetailsFromLocalStorage()
        viewModel.tryToUpdateDetailsFromRemote()
        ImagePickerActivity.clearCache(context)
    }


    /**
     * Setup click listeners for edit buttons
     * @author VE00YM023
     */
    private fun setClickListeners() {
        btn_edit_user_image.setOnClickListener {
            showNameInputUI(false)
            checkPermissionAndContinue()
        }
        btn_edit_user_name.setOnClickListener {
            input_user_name.setText(EMPTY_STRING)
            showNameInputUI(true)
        }
        btn_cancel.setOnClickListener { showNameInputUI(false) }
        btn_save.setOnClickListener {
            val name = input_user_name.text?.toString()
            if(validateUserInput(name)) {
                showNameInputUI(false)
                viewModel.userName = name
                viewModel.performUpdate()
            }
            else { input_user_name.error = NAME_INPUT_ERROR; input_user_name.requestFocus() }
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        /* Update the image view when bitmap is updated */
        viewModel.imageBitmap.observe(this, Observer { iv_user_profile.setImageBitmap(it) })

        /* Observe API response and update UI */
        viewModel.profileData.observe(this, Observer {
            it?.fullName?.let { userName -> tv_user_name.text = userName }
            it?.email?.let { email -> tv_user_email.text = viewModel.decryptData(email) }
        })

        /* When update API call succeeds then update the user name and profile image */
        viewModel.apiCallSuccess.observe(this, Observer { apiCallSuccess ->
            if (apiCallSuccess) {
                viewModel.userName?.let { tv_user_name.text = it }
                viewModel.bitmap?.let { iv_user_profile.setImageBitmap(it) }
            }
            /* Api call finished, set the request data to null */
            viewModel.userName = null
            viewModel.bitmap = null
        })

        viewModel.showProgress.observe(this, Observer { enableInteraction(it) })

        viewModel.apiSuccessMessage.observe(this, Observer {
            if(!it.isNullOrEmpty()) {
                showNotificationDialog(it, R.drawable.ic_success) {}
                viewModel.apiSuccessMessage.postValue(null)
            }
        })

        viewModel.apiErrorMessage.observe(this, Observer {
            if(!it.isNullOrEmpty()) {
                showNotificationDialog(it, R.drawable.ic_error) {}
                viewModel.apiErrorMessage.postValue(null)
            }
        })
    }

    /**
     * Enables/Disables the screen interactivity.
     * @author VE00YM023
     */
    private fun enableInteraction(flag: Boolean) = when (flag){
        true -> {
            requireActivity().window.setFlags(FLAG_NOT_TOUCHABLE, FLAG_NOT_TOUCHABLE)
            pb_profile.visibility = View.VISIBLE
        }
        else -> {
            requireActivity().window.clearFlags(FLAG_NOT_TOUCHABLE)
            pb_profile.visibility = View.GONE
        }
    }

    /**
     * When we receive activity results load the selected image in the image view,
     * and store it in local storage as well as the remove server.
     *
     * @author VE00YM023
     */
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            try {
                val uri: Uri? = data?.getParcelableExtra(KEY_PATH)
                viewModel.bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                viewModel.performUpdate()
            }
            catch (e: java.lang.Exception) { showMessage(IMAGE_LOADING_FAILED) }
        }
    }

    /**
     * Continues with image picker if the required permission for image selection (Camera, Gallery)
     * are granted, otherwise asks the user for specified permissions.
     *
     * @author VE00YM023
     */
    private fun checkPermissionAndContinue() {
        Dexter.withActivity(requireActivity()).withPermissions(Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) { showImagePickerOptions() }
                    if (report.isAnyPermissionPermanentlyDenied) { showSettingsDialog() }
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
        val message = getString(R.string.dialog_camera_storage_permission)
        val actionOk = { openSettings() }
        val actionCancel = { }
        val positiveLabel = getString(R.string.go_to_settings)
        showConfirmationDialog(message, positiveLabel, actionCancel, actionOk)
    }

    /**
     * Opens the application settings system dialog.
     * @author Balraj VE00YM023
     */
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts(PACKAGE, requireActivity().packageName, null)
        }
        startActivityForResult(intent, 101)
    }

    /**
     * Show the dialog with custom view when edit profile image button is clicked.
     * This dialog has two actions.
     *    1. Use Camera -> opens the camera for image capture
     *    2. Use Gallery -> opens gallery for image selection
     *
     * @author VE00YM023
     */
    private fun showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(requireActivity(),  object: ImagePickerActivity.PickerOptionListener {
            override fun onChooseGallerySelected() = launchGalleryIntent()
            override fun onTakeCameraSelected() = launchCameraIntent()
        })
    }

    /**
     * Starts camera activity to capture the profile picture.
     * @author VE00YM023
     */
    private fun launchCameraIntent() {
        val intent = Intent(requireActivity(), ImagePickerActivity::class.java)
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE)
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, ASPECT_RATIO_X)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, ASPECT_RATIO_Y)
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, BITMAP_MAX_WIDTH)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, BITMAP_MAX_HEIGHT)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    /**
     * Opens the gallery to pickup the profile picture.
     * @author VE00YM023
     */
    private fun launchGalleryIntent() {
        val intent = Intent(requireActivity(), ImagePickerActivity::class.java)
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE)
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, ASPECT_RATIO_X)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, ASPECT_RATIO_Y)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    /**
     * Show/Hide the name input field and the update buttons.
     * @author VE00YM023
     */
    private fun showNameInputUI(flag: Boolean){
        tv_user_name.visibility = if(flag) View.INVISIBLE else View.VISIBLE
        input_user_name.visibility = if(flag) View.VISIBLE else View.INVISIBLE
        container_btns.visibility = if(flag) View.VISIBLE else View.GONE
    }

    /**
     * Validates the name input by the user for empty or blank values.
     * @author VE00YM023
     */
    private fun validateUserInput(name: String?) = (!name.isNullOrEmpty() && !name.isNullOrBlank())
}
