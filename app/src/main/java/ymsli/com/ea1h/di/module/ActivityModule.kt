/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   31/1/20 11:14 AM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * ActivityModule : This acts as the Dagger2 module of DI framework
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *               10/02/2020          Added provider for SplashViewModel
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.di.module

import android.content.Context
import android.location.LocationManager
import androidx.appcompat.app.AlertDialog
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.gigya.android.sdk.Gigya
import com.google.gson.Gson
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.GigyaResponse
import ymsli.com.ea1h.R
import ymsli.com.ea1h.adapters.TripThisWeekAdapter
import ymsli.com.ea1h.base.BaseActivity
import ymsli.com.ea1h.views.home.HomeViewModel
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.ViewModelProviderFactory
import ymsli.com.ea1h.utils.rx.SchedulerProvider
import ymsli.com.ea1h.views.entrance.EntranceViewModel
import ymsli.com.ea1h.views.home.drivinghistory.TripHistoryDetailViewModel
import ymsli.com.ea1h.views.addbike.ChassisNumberViewModel
import ymsli.com.ea1h.views.password.forgot.ForgotPasswordViewModel
import ymsli.com.ea1h.views.addbike.ScanQRCodeViewModel
import ymsli.com.ea1h.views.splash.SplashViewModel
import ymsli.com.ea1h.views.addbike.TermsAndConditionsViewModel
import ymsli.com.ea1h.views.home.userprofile.UserProfileViewModel
import ymsli.com.ea1h.views.otp.OTPViewModel
import ymsli.com.ea1h.views.splash.LaunchViewModel
import ymsli.com.ea1h.views.sync.SyncViewModel
import ymsli.com.ea1h.views.userengagement.ProgressFragment
import ymsli.com.ea1h.views.userengagement.ErrorAckFragment
import ymsli.com.ea1h.views.userengagement.MFECUProgressFragment

@Module
class ActivityModule(private val activity: BaseActivity<*>) {

    @Provides
    fun provideEntranceViewModel(
        schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper, ea1hRepository: EA1HRepository
    ): EntranceViewModel=
        ViewModelProviders.of(activity,
            ViewModelProviderFactory(EntranceViewModel::class) {
                EntranceViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    provideGigya(),
                    ea1hRepository,
                    provideGson()
                )
            }).get(EntranceViewModel::class.java)

    @Provides
    fun provideLaunchViewModel(
        schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper, ea1hRepository: EA1HRepository
    ): LaunchViewModel =
        ViewModelProviders.of(activity,
            ViewModelProviderFactory(LaunchViewModel::class) {
                LaunchViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    ea1hRepository
                )
            }).get(LaunchViewModel::class.java)

    @Provides
    fun provideForgotPasswordViewModel(
        schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper, ea1hRepository: EA1HRepository
    ): ForgotPasswordViewModel =
        ViewModelProviders.of(activity,
            ViewModelProviderFactory(ForgotPasswordViewModel::class) {
                ForgotPasswordViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    provideGigya(),
                    ea1hRepository
                )
            }).get(ForgotPasswordViewModel::class.java)


    @Provides
    fun provideSplashViewModel(
        schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper, ea1hRepository: EA1HRepository
    ): SplashViewModel =
        ViewModelProviders.of(activity,
            ViewModelProviderFactory(SplashViewModel::class) {
                SplashViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    ea1hRepository
                )
            }).get(SplashViewModel::class.java)

    @Provides
    fun provideTripHistoryDetailViewModel(
        schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper, ea1hRepository: EA1HRepository
    ): TripHistoryDetailViewModel =
        ViewModelProviders.of(activity,
            ViewModelProviderFactory(TripHistoryDetailViewModel::class) {
                TripHistoryDetailViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    ea1hRepository
                )
            }).get(TripHistoryDetailViewModel::class.java)

    @Provides
    fun provideOTPViewModel(
        schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper, ea1hRepository: EA1HRepository
    ): OTPViewModel=
        ViewModelProviders.of(activity,
            ViewModelProviderFactory(OTPViewModel::class) {
                OTPViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    ea1hRepository
                )
            }).get(OTPViewModel::class.java)

    @Provides
    fun provideYourVehiclesViewModel(
        schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper, ea1hRepository: EA1HRepository
    ): ChassisNumberViewModel =
        ViewModelProviders.of(activity,
            ViewModelProviderFactory(ChassisNumberViewModel::class) {
                ChassisNumberViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    ea1hRepository
                )
            }).get(ChassisNumberViewModel::class.java)

    @Provides
    fun provideScanQRCodeViewModel(
        schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper, ea1hRepository: EA1HRepository
    ): ScanQRCodeViewModel =
        ViewModelProviders.of(activity,
            ViewModelProviderFactory(ScanQRCodeViewModel::class) {
                ScanQRCodeViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    ea1hRepository
                )
            }).get(ScanQRCodeViewModel::class.java)

    @Provides
    fun provideTermsAndConditionsViewModel(
        schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper, ea1hRepository: EA1HRepository
    ): TermsAndConditionsViewModel =
        ViewModelProviders.of(activity,
            ViewModelProviderFactory(TermsAndConditionsViewModel::class) {
                TermsAndConditionsViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    ea1hRepository
                )
            }).get(TermsAndConditionsViewModel::class.java)

    @Provides
    fun provideUserProfileViewModel(schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
                              networkHelper: NetworkHelper, ea1hRepository: EA1HRepository): UserProfileViewModel =

        ViewModelProviders.of(activity,
            ViewModelProviderFactory(UserProfileViewModel::class) {
                UserProfileViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    ea1hRepository
                )
            }).get(UserProfileViewModel::class.java)

    @Provides
    fun provideNewHomeViewModel(schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
                                    networkHelper: NetworkHelper, ea1hRepository: EA1HRepository): HomeViewModel =

        ViewModelProviders.of(activity,
            ViewModelProviderFactory(HomeViewModel::class) {
                HomeViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    provideGigya(),
                    networkHelper,
                    ea1hRepository
                )
            }).get(HomeViewModel::class.java)

    @Provides
    fun provideSyncViewModel(schedulerProvider: SchedulerProvider,
                             compositeDisposable: CompositeDisposable,
                             networkHelper: NetworkHelper,
                             ea1hRepository: EA1HRepository): SyncViewModel =
        ViewModelProviders.of(activity,
            ViewModelProviderFactory(SyncViewModel::class) {
                SyncViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    ea1hRepository
                )
            }).get(SyncViewModel::class.java)

    @Provides
    fun provideProgressFragment(): ProgressFragment = ProgressFragment.newInstance()

    @Provides
    fun provideMFECUProgressFragment(): MFECUProgressFragment = MFECUProgressFragment.newInstance()

    @Provides
    fun provideUserAckFragment(): ErrorAckFragment = ErrorAckFragment.newInstance()

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(activity)

    @Provides
    fun provideTripThisWeekAdapter(): TripThisWeekAdapter = TripThisWeekAdapter(activity.lifecycle,ArrayList())

    @Provides
    fun provideAlertDialogBuilder(): AlertDialog.Builder = AlertDialog.Builder(activity, R.style.AckDialogTheme)



    @Provides
    fun provideGigya(): Gigya<GigyaResponse> = Gigya.getInstance(GigyaResponse::class.java)

    @Provides
    fun provideLocationManager(): LocationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @Provides
    fun provideGson(): Gson = Gson()
}