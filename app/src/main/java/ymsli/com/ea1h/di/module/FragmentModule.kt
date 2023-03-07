/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   5/2/20 10:16 AM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * FragmentModule : This acts as the Dagger2 module of DI framework
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  * (VE00YM023)   25/02/2020          Added provider for ChangePasswordViewModel
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.di.module

import android.content.Context
import android.location.LocationManager
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.gigya.android.sdk.Gigya
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.GigyaResponse
import ymsli.com.ea1h.adapters.TripThisWeekAdapter
import ymsli.com.ea1h.adapters.YourBikesAdapter
import ymsli.com.ea1h.base.BaseFragment
import ymsli.com.ea1h.views.home.HomeViewModel
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.ViewModelProviderFactory
import ymsli.com.ea1h.utils.rx.SchedulerProvider
import ymsli.com.ea1h.views.entrance.EntranceViewModel
import ymsli.com.ea1h.views.home.aboutus.AboutUsViewModel
import ymsli.com.ea1h.views.home.changepassword.ChangePasswordViewModel
import ymsli.com.ea1h.views.home.drivinghistory.TripHistoryBluetoothViewModel
import ymsli.com.ea1h.views.home.privacypolicy.PrivacyPolicyViewModel
import ymsli.com.ea1h.views.home.unregisteruser.UnregisterUserViewModel
import ymsli.com.ea1h.views.home.userprofile.UserProfileViewModel
import ymsli.com.ea1h.views.userengagement.ProgressFragment
import ymsli.com.ea1h.views.yourvehicles.YourBikesFragment
import ymsli.com.ea1h.views.yourvehicles.YourBikesViewModel

@Module
class FragmentModule(private val fragment: BaseFragment<*>) {

    @Provides
    fun provideEntranceViewModel(
        schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper, ea1hRepository: EA1HRepository
    ): EntranceViewModel =
        ViewModelProviders.of(fragment.activity!!,
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
    fun provideAboutUsViewModel(
        schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper, ea1hRepository: EA1HRepository
    ): AboutUsViewModel =
        ViewModelProviders.of(fragment.activity!!,
            ViewModelProviderFactory(AboutUsViewModel::class) {
                AboutUsViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    ea1hRepository
                )
            }).get(AboutUsViewModel::class.java)

    @Provides
    fun provideUnregisterUserViewModel(
        schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper, ea1hRepository: EA1HRepository
    ): UnregisterUserViewModel =
        ViewModelProviders.of(fragment.activity!!,
            ViewModelProviderFactory(UnregisterUserViewModel::class) {
                UnregisterUserViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    ea1hRepository
                )
            }).get(UnregisterUserViewModel::class.java)


    @Provides
    fun providePrivacyPolicyViewModel(
        schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper, ea1hRepository: EA1HRepository
    ): PrivacyPolicyViewModel =
        ViewModelProviders.of(fragment.activity!!,
            ViewModelProviderFactory(PrivacyPolicyViewModel::class) {
                PrivacyPolicyViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    ea1hRepository
                )
            }).get(PrivacyPolicyViewModel::class.java)

    @Provides
    fun provideTripHistoryBluetoothViewModel(
        schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper, ea1hRepository: EA1HRepository
    ): TripHistoryBluetoothViewModel =
        ViewModelProviders.of(fragment.activity!!,
            ViewModelProviderFactory(TripHistoryBluetoothViewModel::class) {
                TripHistoryBluetoothViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    ea1hRepository
                )
            }).get(TripHistoryBluetoothViewModel::class.java)

    @Provides
    fun provideChangePasswordViewModel(
        schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper, ea1hRepository: EA1HRepository
    ): ChangePasswordViewModel =
        ViewModelProviders.of(fragment.activity!!,
            ViewModelProviderFactory(ChangePasswordViewModel::class) {
                ChangePasswordViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    provideGigya(),
                    ea1hRepository
                )
            }).get(ChangePasswordViewModel::class.java)


    @Provides
    fun provideUserProfileViewModel(
        schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper, ea1hRepository: EA1HRepository
    ): UserProfileViewModel =
        ViewModelProviders.of(fragment.activity!!,
            ViewModelProviderFactory(UserProfileViewModel::class) {
                UserProfileViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    ea1hRepository
                )
            }).get(UserProfileViewModel::class.java)

    @Provides
    fun provideNewHomeViewModel(
        schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper, ea1hRepository: EA1HRepository
    ): HomeViewModel =
        ViewModelProviders.of(fragment.activity!!,
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
    fun provideYourBikesViewModel(schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
                                  networkHelper: NetworkHelper, ea1hRepository: EA1HRepository): YourBikesViewModel =

        ViewModelProviders.of(fragment.activity!!,
            ViewModelProviderFactory(YourBikesViewModel::class) {
                YourBikesViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    ea1hRepository
                )
            }).get(YourBikesViewModel::class.java)


    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(fragment.activity!!)

    @Provides
    fun provideTripThisWeekAdapter(): TripThisWeekAdapter = TripThisWeekAdapter(fragment.lifecycle,ArrayList())

    @Provides
    fun provideGigya(): Gigya<GigyaResponse> = Gigya.getInstance(GigyaResponse::class.java)

    @Provides
    fun provideGson(): Gson = Gson()

    @Provides
    fun provideLocationManager(): LocationManager = fragment.activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @Provides
    fun provideProgressFragment(): ProgressFragment = ProgressFragment.newInstance()
}
