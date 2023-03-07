package ymsli.com.ea1h.adapters

/**
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM129)
 * @date   February 24, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * TripThisWeekAdapter : This adapter class is the responsible for rendering of trips
 *                      in recycler view.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import ymsli.com.ea1h.base.BaseAdapter
import ymsli.com.ea1h.database.entity.BikeEntity
import ymsli.com.ea1h.database.entity.TripEntity
import ymsli.com.ea1h.views.home.drivinghistory.TripEntityViewHolder

class TripThisWeekAdapter (parentLifeCycle : Lifecycle,
                           tasksLists:ArrayList<TripEntity>)
    : BaseAdapter<TripEntity, TripEntityViewHolder>(parentLifeCycle,tasksLists) {

    var bikeEntityMap: Map<String, BikeEntity> = mutableMapOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : TripEntityViewHolder = TripEntityViewHolder(parent, bikeEntityMap)
}