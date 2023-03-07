package ymsli.com.ea1h.di

/**
 * Project Name : EA1H
 * @company YMSLI
 * @author VE00YM129
 * @date   February 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * Scopes : Define the scope of an objcet injected via dagger2 framework
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.SOURCE)
annotation class ActivityScope

@Scope
@Retention(AnnotationRetention.SOURCE)
annotation class FragmentScope

@Scope
@Retention(AnnotationRetention.SOURCE)
annotation class ViewModelScope

@Scope
@Retention(AnnotationRetention.SOURCE)
annotation class IntentServiceScope

@Scope
@Retention(AnnotationRetention.SOURCE)
annotation class JobServiceScope