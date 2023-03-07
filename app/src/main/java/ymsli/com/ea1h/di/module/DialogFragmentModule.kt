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
 *  * DialogFragmentModule : This acts as the Dagger2 module of DI framework
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.di.module

import androidx.lifecycle.ViewModelProviders
import com.gigya.android.sdk.Gigya
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.GigyaResponse
import ymsli.com.ea1h.base.BaseDialogFragment
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.ViewModelProviderFactory
import ymsli.com.ea1h.utils.rx.SchedulerProvider
import ymsli.com.ea1h.views.entrance.EntranceViewModel

@Module
class DialogFragmentModule(private val dialogFragment: BaseDialogFragment<*>) {

    @Provides
    fun provideEntranceViewModel(
        schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper, ea1hRepository: EA1HRepository
    ): EntranceViewModel =
        ViewModelProviders.of(dialogFragment.activity!!,
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
    fun provideGigya(): Gigya<GigyaResponse> = Gigya.getInstance(GigyaResponse::class.java)

    @Provides
    fun provideGson(): Gson = Gson()
}