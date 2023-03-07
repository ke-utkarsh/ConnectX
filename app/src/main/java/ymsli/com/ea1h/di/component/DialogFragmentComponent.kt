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
 *  * DialogFragmentComponent : This acts as the Dagger2 component of DI framework
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
import ymsli.com.ea1h.di.module.DialogFragmentModule
import ymsli.com.ea1h.views.userengagement.ErrorAckFragment
import ymsli.com.ea1h.views.userengagement.SuccessAckFragment

@FragmentScope
@Component(dependencies = [ApplicationComponent::class], modules = [DialogFragmentModule::class])
interface DialogFragmentComponent {

    fun inject(userAckFragment: ErrorAckFragment)

    fun inject(successAckFragment: SuccessAckFragment)

}