/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   5/2/20 10:17 AM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * ActivityComponent : This acts as the Dagger2 component of DI framework
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *               10/02/2020          Added injector function SplashActivity
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.di.component

import dagger.Component
import ymsli.com.ea1h.di.ActivityScope
import ymsli.com.ea1h.di.module.ActivityModule
import ymsli.com.ea1h.views.home.HomeActivity
import ymsli.com.ea1h.views.entrance.EntranceActivity
import ymsli.com.ea1h.views.home.drivinghistory.TripHistoryDetailActivity
import ymsli.com.ea1h.views.addbike.ChassisNumberActivity
import ymsli.com.ea1h.views.otp.OTPActivity
import ymsli.com.ea1h.views.password.forgot.ForgotPasswordActivity
import ymsli.com.ea1h.views.addbike.ScanQRCodeActivity
import ymsli.com.ea1h.views.splash.SplashActivity
import ymsli.com.ea1h.views.addbike.TermsAndConditionsActivity
import ymsli.com.ea1h.views.entrance.SignupSuccessActivity
import ymsli.com.ea1h.views.splash.LaunchActivity
import ymsli.com.ea1h.views.sync.SyncActivity

@ActivityScope
@Component(dependencies = [ApplicationComponent::class],modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(entranceActivity: EntranceActivity)

    fun inject(otpActivity: OTPActivity)

    fun inject(forgotPasswordActivity: ForgotPasswordActivity)

    fun inject(splashActivity: SplashActivity)

    fun inject(tripHistoryDetailActivity: TripHistoryDetailActivity)

    fun inject(chassisNumberActivity: ChassisNumberActivity)

    fun inject(scanQRCodeActivity: ScanQRCodeActivity)

    fun inject(termsAndConditionsActivity: TermsAndConditionsActivity)

    fun inject(launchActivity: LaunchActivity)

    fun inject(newHomeActivity: HomeActivity)
    fun inject(signupSuccessActivity: SignupSuccessActivity)
    fun inject(syncActivity: SyncActivity)
}