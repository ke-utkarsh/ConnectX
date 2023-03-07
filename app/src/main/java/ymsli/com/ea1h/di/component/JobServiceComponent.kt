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
 * JobServiceComponent : This is job service component and injects object in job service
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
import ymsli.com.ea1h.di.JobServiceScope
import ymsli.com.ea1h.services.*

@JobServiceScope
@Component(dependencies = [ApplicationComponent::class])
interface JobServiceComponent {

    fun inject(zipUploadService: ZipUploadService)

    fun inject(uploadTripsWorkManager: UploadTripsWorkManager)

    fun inject(uploadBTLogsWorkManager: UploadBTLogsWorkManager)

    fun inject(uploadBTLogsWorkManager: ZipUploadWorkManager)
}