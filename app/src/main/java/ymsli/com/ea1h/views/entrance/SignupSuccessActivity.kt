package ymsli.com.ea1h.views.entrance

/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM023
 *  * @date   21/8/20 11:13 AM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * SignupSuccessActivity : This Activity show UI to the the user that signup is
 *      successful and will guide for further steps
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *
 *  * -----------------------------------------------------------------------------------
 *
 */
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_signup_success.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseActivity
import ymsli.com.ea1h.di.component.ActivityComponent

class SignupSuccessActivity :BaseActivity<EntranceViewModel>() {

    override fun provideLayoutId() = R.layout.activity_signup_success
    override fun injectDependencies(ac: ActivityComponent) { ac.inject(this) }

    /**
     * Setup the click listener for the 'GO TO Login',
     * to redirect the user to Login Screen.
     * @author VE00YM023
     */
    override fun setupView(savedInstanceState: Bundle?) {
        window?.statusBarColor = getColor(R.color.bg_status_bar)
        btn_signup_success.setOnClickListener {
            val intent = Intent(this, EntranceActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
