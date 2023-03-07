package ymsli.com.ea1h.di.component

/**
 * Project Name : EA1H
 * @company YMSLI
 * @author VE00YM129
 * @date   August 23, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * BottomFilterComponent : This is bottom filter component and injects object in bottom fragment
 * component via dagger2
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

import dagger.Component
import ymsli.com.ea1h.di.FragmentScope
import ymsli.com.ea1h.di.module.BottomFilterModule

@FragmentScope
@Component(dependencies = [ApplicationComponent::class],modules = [BottomFilterModule::class])
interface BottomFilterComponent {

}