package ymsli.com.ea1h.di.module

import androidx.lifecycle.LifecycleRegistry
import dagger.Module
import dagger.Provides
import ymsli.com.ea1h.base.BaseItemViewHolder
import ymsli.com.ea1h.di.ViewModelScope

/**
 * Project Name : EA1H
 * @company YMSLI
 * @author VE00YM129
 * @date   February 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ViewHolderModule : This is the view holder module of dagger2 framework. This is
 *                      responsible for providing objects with @Inject annotation
 *                      in the view holder.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
@Module
class ViewHolderModule(private val viewHolder: BaseItemViewHolder<*, *>)  {

    @Provides
    @ViewModelScope
    fun provideLifecycleRegistry(): LifecycleRegistry = LifecycleRegistry(viewHolder)
}