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
 *  * FragmentComponent : This acts as the Dagger2 component of DI framework
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.di.component

import dagger.Component
import ymsli.com.ea1h.di.FragmentScope
import ymsli.com.ea1h.di.module.FragmentModule
import ymsli.com.ea1h.views.entrance.SignInFragment
import ymsli.com.ea1h.views.entrance.SignUpFragment
import ymsli.com.ea1h.views.home.HomeFragment
import ymsli.com.ea1h.views.home.HomeHeaderFragment
import ymsli.com.ea1h.views.home.HomeNoBikeFragment
import ymsli.com.ea1h.views.home.HomeNoTripFragment
import ymsli.com.ea1h.views.home.aboutus.AboutUsFragment
import ymsli.com.ea1h.views.home.changepassword.ChangePasswordFragment
import ymsli.com.ea1h.views.home.drivinghistory.TripHistoryFragment
import ymsli.com.ea1h.views.home.elock.ELockFragment
import ymsli.com.ea1h.views.home.parkinglocation.ParkingLocationFragment
import ymsli.com.ea1h.views.home.privacypolicy.PrivacyPolicyFragment
import ymsli.com.ea1h.views.home.unregisteruser.UnregisterUserFragment
import ymsli.com.ea1h.views.home.userprofile.UserProfileActivity
import ymsli.com.ea1h.views.yourvehicles.YourBikesFragment

@FragmentScope
@Component(dependencies = [ApplicationComponent::class],modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(signupFragment: SignUpFragment)
    fun inject(signInFragment: SignInFragment)
    fun inject(aboutUsFragment: AboutUsFragment)
    fun inject(unregisterUserFragment: UnregisterUserFragment)
    fun inject(privacyPolicyFragment: PrivacyPolicyFragment)
    fun inject(homeFragment: HomeFragment)
    fun inject(parkingLocationFragment: ParkingLocationFragment)
    fun inject(homeHeaderFragment: HomeHeaderFragment)
    fun inject(changePasswordFragment: ChangePasswordFragment)
    fun inject(yourBikesFragment: YourBikesFragment)
    fun inject(eLockFragment: ELockFragment)
    fun inject(userProfileActivity: UserProfileActivity)
    fun inject(tripHistoryFragment: TripHistoryFragment)
    fun inject(homeNoBikeFragment: HomeNoBikeFragment)
    fun inject(homeNoTripFragment: HomeNoTripFragment)
}