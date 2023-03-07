package ymsli.com.ea1h.di.component

import dagger.Component
import ymsli.com.ea1h.di.ViewModelScope
import ymsli.com.ea1h.di.module.ViewHolderModule
import ymsli.com.ea1h.views.home.drivinghistory.TripEntityViewHolder
import ymsli.com.ea1h.views.yourvehicles.YourBikesViewHolder

/**
 * Project Name : EA1H
 * @company YMSLI
 * @author VE00YM129
 * @date   February 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ViewHolderComponent : This is the viewholder component of dagger2 framework. This is
 *                      responsible for injecting objects in the viewholder of RV.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
@ViewModelScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ViewHolderModule::class]
)
interface ViewHolderComponent {
    fun inject(tripEntityViewHolder: TripEntityViewHolder)

    fun inject(yourBikesViewHolder: YourBikesViewHolder)
}