package ymsli.com.ea1h.views.sync;

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_sync.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseActivity
import ymsli.com.ea1h.di.component.ActivityComponent
import ymsli.com.ea1h.views.home.HomeActivity


/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  VE00YM023
 * @date    21/08/2020 2:45 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * SyncViewModel : This is view model for SyncActivity.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

class SyncActivity: BaseActivity<SyncViewModel>(){
    override fun provideLayoutId() = R.layout.activity_sync
    override fun injectDependencies(ac: ActivityComponent) = ac.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {
        window?.statusBarColor = getColor(R.color.bg_status_bar)
        Glide.with(this).asGif().load(R.drawable.sync_loader).into(progress_bar)

        setupClickListeners()
        viewModel.startSync()
    }

    override fun setupObservers() {
        super.setupObservers()

        /* If network is not available and application is already initialized (Once synced)
           then move to next screen. otherwise show the sync failure options */
        viewModel.noNetwork.observe(this, Observer { noNetwork ->
            val appInitStatus = viewModel.getAppInitStatus()
            if(appInitStatus) { openHomeScreen() }
            else if(noNetwork) { showSyncFailureUI() }
        })

        viewModel.apiCallFailed.observe(this, Observer { showSyncFailureUI() })

        /* When sync finishes move to the home screen */
        viewModel.syncFinished.observe(this, Observer { openHomeScreen() })

        viewModel.syncActive.observe(this, Observer {
            tv_sync_active.visibility = if(it) View.VISIBLE else View.GONE
            progress_bar.visibility = if(it) View.VISIBLE else View.GONE
        })
    }

    /**
     * Displays the Sync Failure UI elements,
     * 'Retry' and 'Logout' buttons and sync failure text.
     * @author VE00YM023
     */
    private fun showSyncFailureUI(){
        btn_retry.visibility = View.VISIBLE
        tv_sync_failed.visibility = View.VISIBLE
        tv_sync_active.visibility = View.GONE
    }

    /**
     * Setup click listeners for 'Retry' button.
     * @author VE00YM023
     */
    private fun setupClickListeners(){
        btn_retry.setOnClickListener {
            btn_retry.visibility = View.GONE
            tv_sync_failed.visibility = View.GONE
            viewModel.startSync()
        }
    }

    /** Starts the home activity */
    private fun openHomeScreen(){
        viewModel.setAppInitStatus(true)
        val homeIntent = Intent(this, HomeActivity::class.java)
        //val homeIntent = Intent(this, ExperimentActivity::class.java)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(homeIntent)
        finish()
    }
}